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

public final class WeatherHttpExecutor {

    private final HttpClient client;
    private final RetryPolicy retryPolicy;
    private final WeatherLogger logger;

    public WeatherHttpExecutor(HttpClient client, RetryPolicy retryPolicy, WeatherLogger logger) {
        this.client = client;
        this.retryPolicy = retryPolicy;
        this.logger = logger;
    }

    public String execute(RequestSettings settings) {
        return retryPolicy.executeWithRetry(() -> doExecute(settings));
    }

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


