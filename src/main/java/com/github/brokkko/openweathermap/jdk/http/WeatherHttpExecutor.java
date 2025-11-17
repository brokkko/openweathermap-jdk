package com.github.brokkko.openweathermap.jdk.http;

import com.github.brokkko.openweathermap.jdk.exceptions.WeatherApiException;
import com.github.brokkko.openweathermap.jdk.exceptions.WeatherNetworkException;
import com.github.brokkko.openweathermap.jdk.exceptions.WeatherSdkException;
import com.github.brokkko.openweathermap.jdk.exceptions.WeatherTimeoutException;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.request.RequestSettings;
import com.github.brokkko.openweathermap.jdk.retries.RetryPolicy;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.stream.Collectors;

import java.net.ConnectException;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.*;

import static com.github.brokkko.openweathermap.jdk.constants.LogMessages.*;

/**
 * HTTP execution component responsible for performing outbound requests to the
 * OpenWeatherMap API using Java {@link HttpClient}.
 * <p>
 * This class acts as the low-level transport layer of the SDK:
 * <ul>
 *     <li>builds an HTTP GET request from {@link RequestSettings}</li>
 *     <li>executes the request using the configured {@link HttpClient}</li>
 *     <li>applies retry logic via {@link RetryPolicy}</li>
 *     <li>converts network/IO errors into SDK-specific exceptions</li>
 *     <li>logs all outgoing requests and error events</li>
 * </ul>
 *
 * <p>
 * All exceptions thrown by this executor are converted into SDK-specific
 * subclasses of {@link WeatherSdkException}, making it safe for use in public API layers.
 */
public final class WeatherHttpExecutor {

    private final HttpClient client;
    private final RetryPolicy retryPolicy;
    private final WeatherLogger logger;

    /**
     * Creates a new HTTP executor.
     *
     * @param client      underlying HTTP client used for request execution
     * @param retryPolicy retry strategy used when execution fails
     * @param logger      logger for debug/error output
     */
    public WeatherHttpExecutor(HttpClient client, RetryPolicy retryPolicy, WeatherLogger logger) {
        this.client = client;
        this.retryPolicy = retryPolicy;
        this.logger = logger;
    }

    /**
     * Executes an HTTP request defined in {@link RequestSettings},
     * applying retries via {@link RetryPolicy}.
     *
     * @param settings request configuration (URL + query parameters)
     * @return response body as UTF-8 text
     * @throws WeatherTimeoutException if request timeout occurs
     * @throws WeatherNetworkException if network connectivity errors occur
     * @throws WeatherApiException if OpenWeatherMap returns a non-2xx response
     * @throws WeatherSdkException for unexpected or internal errors
     */
    public String execute(RequestSettings settings) {
        return retryPolicy.executeWithRetry(() -> doExecute(settings));
    }

    /**
     * Executes the HTTP call a single time (without retries).
     * Internal method used by {@link #execute(RequestSettings)}.
     */
    private String doExecute(RequestSettings settings) {
        String url = buildUrl(settings);

        logger.info("HTTP request: " + url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response;

        try {
            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
            );

        } catch (HttpTimeoutException e) {
            logger.error(HTTP_TIMEOUT_MESSAGE, e);
            throw new WeatherTimeoutException(HTTP_TIMEOUT_MESSAGE, e);

        } catch (UnknownHostException | ConnectException e) {
            logger.error(HTTP_NETWORK_UNAVAILABLE_MESSAGE, e);
            throw new WeatherNetworkException(HTTP_NETWORK_UNAVAILABLE_MESSAGE + e.getMessage(), e);

        } catch (IOException e) {
            logger.error(HTTP_IO_ERROR_MESSAGE, e);
            throw new WeatherNetworkException(HTTP_IO_ERROR_MESSAGE, e);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error(HTTP_INTERRUPTED_MESSAGE, e);
            throw new WeatherSdkException(HTTP_INTERRUPTED_MESSAGE, e);
        }

        int status = response.statusCode();
        if (status >= 200 && status < 300) {
            return response.body();
        }

        logger.warn(String.format(HTTP_UNEXPECTED_STATUS_MESSAGE, status));
        throw new WeatherApiException(
                String.format(HTTP_UNEXPECTED_STATUS_MESSAGE, status) + " : " + response.body(),
                status
        );
    }

    /**
     * Builds the full URL with encoded query parameters.
     */
    private String buildUrl(RequestSettings requestSettings) {
        StringBuilder requestUrlBuilder = new StringBuilder();
        requestUrlBuilder.append(requestSettings.getUrlBuilder());
        requestUrlBuilder.append('?');

        String parameters = requestSettings.getRequestParameters().entrySet().stream()
                .map(entry -> entry.getKey() + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));

        return requestUrlBuilder.append(parameters).toString();
    }

    private static String encode(String value) {
        return java.net.URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
