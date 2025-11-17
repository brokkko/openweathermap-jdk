package com.github.brokkko.openweathermap.jdk.request.terminaters;

import com.github.brokkko.openweathermap.jdk.clients.OpenWeatherMapClient;
import com.github.brokkko.openweathermap.jdk.exceptions.*;
import com.github.brokkko.openweathermap.jdk.enums.SdkMode;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.mappers.WeatherResponseMapper;
import com.github.brokkko.openweathermap.jdk.models.Weather;
import com.github.brokkko.openweathermap.jdk.request.RequestSettings;

import java.util.Optional;

import static com.github.brokkko.openweathermap.jdk.constants.LogMessages.*;

/**
 * Final stage of the fluent API. Responsible for:
 * - selecting cache or live HTTP request depending on SDK mode,
 * - retrieving raw JSON or mapping it into {@link Weather},
 * - handling caching rules and failures.
 */
public class WeatherRequestTerminator {

    private final RequestSettings requestSettings;
    private final OpenWeatherMapClient client;
    private final WeatherLogger logger;

    /**
     * Creates a terminating stage with required context.
     *
     * @param client          API client.
     * @param logger          logger.
     * @param requestSettings configured request settings.
     */
    public WeatherRequestTerminator(OpenWeatherMapClient client, WeatherLogger logger, RequestSettings requestSettings) {
        this.requestSettings = requestSettings;
        this.client = client;
        this.logger = logger;
    }

    /**
     * Executes the request and maps the response into a {@link Weather} object.
     *
     * @return parsed Weather result.
     */
    public Weather asJava() {
        try {
            String json = getRawResponse();
            return new WeatherResponseMapper(requestSettings.getUnitSystem(), logger).mapJsonToWeather(json);
        } catch (WeatherSdkException e) {
            // перекидываем специфичные исключения дальше
            throw e;
        } catch (Exception e) {
            logger.error(UNABLE_PARSE_JSON_TO_WEATHER_OBJECT_MESSAGE, e);
            throw new WeatherSerializationException(UNABLE_PARSE_JSON_TO_WEATHER_OBJECT_MESSAGE, e);
        }
    }

    /**
     * Executes the request and returns the response as a raw JSON string.
     *
     * @return JSON response.
     */
    public String asJSON() {
        return getRawResponse();
    }

    /**
     * Resolves the raw JSON response according to the client's SDK mode.
     * <p>
     * In ON_DEMAND mode the terminator first checks the cache and falls back to an HTTP request
     * if no fresh cached value is available. In POLLING mode the terminator assumes that polling
     * keeps cache entries up-to-date and will retrieve data from the cache when possible.
     * </p>
     *
     * @return raw JSON response from cache or HTTP executor
     * @throws WeatherSdkException if an unexpected error occurs or a low-level exception must be wrapped
     */
    private String getRawResponse() {
        String cacheKey = requestSettings.cacheKey();

        try {
            if (client.getSdkMode() == SdkMode.ON_DEMAND) {
               return handleOnDemand(cacheKey);
            }
            return handleOnPolling(cacheKey);
        } catch (Exception e) {
            if (e instanceof WeatherSdkException) {
                throw e;
            }
            throw new WeatherSdkException(String.format(TERMINATOR_UNEXPECTED_MESSAGE, cacheKey), e);
        }
    }

    /**
     * Handles response retrieval in ON_DEMAND mode:
     * <ul>
     *     <li>returns a cached value when present</li>
     *     <li>logs cache hit/miss events</li>
     *     <li>executes an HTTP request when no cached value is available</li>
     *     <li>stores the response in the cache using a copy of the current request settings</li>
     * </ul>
     *
     * @param cacheKey key used to look up cached responses
     * @return raw JSON response, either from cache or via HTTP
     */
    private String handleOnDemand(String cacheKey) {
        Optional<String> cached = client.getCacheService().get(cacheKey);
        if (cached.isPresent()) {
            logger.debug(String.format(CACHE_HIT_ON_DEMAND_MESSAGE, cacheKey));
            return cached.get();
        }
        logger.debug(String.format(CACHE_MISS_ON_DEMAND_MESSAGE, cacheKey));
        String resp = client.getHttpExecutor().execute(requestSettings);
        client.getCacheService().put(cacheKey, resp, requestSettings.copy());
        return resp;
    }

    /**
     * Handles response retrieval in POLLING mode:
     * <ul>
     *     <li>attempts to return a cached value, assuming polling keeps entries fresh</li>
     *     <li>logs cache hit/miss events</li>
     *     <li>executes an HTTP request only as a fallback when the cache is empty</li>
     *     <li>stores the newly retrieved response in the cache</li>
     * </ul>
     *
     * @param cacheKey key used to look up cached responses
     * @return raw JSON response, either from cache or via HTTP
     */
    private String handleOnPolling(String cacheKey) {
        Optional<String> cached = client.getCacheService().get(cacheKey);
        if (cached.isPresent()) {
            logger.debug(String.format(CACHE_HIT_POLLING_MESSAGE,  cacheKey));
            return cached.get();
        } else {
            logger.debug(String.format(CACHE_MISS_POLLING_MESSAGE, cacheKey));
            String resp = client.getHttpExecutor().execute(requestSettings);
            client.getCacheService().put(cacheKey, resp, requestSettings.copy());
            return resp;
        }
    }
}
