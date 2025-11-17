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
 * Terminator — финальный этап fluent API: решает, брать ответ из кэша или дернуть HTTP.
 * Он имеет ссылку на клиента (контекст), чтобы знать mode (ON_DEMAND / POLLING) и иметь доступ к cacheService.
 */
public class WeatherRequestTerminator {

    private final RequestSettings requestSettings;
    private final OpenWeatherMapClient client;
    private final WeatherLogger logger;

    public WeatherRequestTerminator(OpenWeatherMapClient client, WeatherLogger logger, RequestSettings requestSettings) {
        this.requestSettings = requestSettings;
        this.client = client;
        this.logger = logger;
    }

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

    public String asJSON() {
        return getRawResponse();
    }

    private String getRawResponse() {
        String cacheKey = requestSettings.cacheKey();

        try {
            // ON_DEMAND: сначала проверить кэш, если есть & свежо -> вернуть; иначе выполнить запрос и положить в кэш
            if (client.getSdkMode() == SdkMode.ON_DEMAND) {
               return handleOnDemand(cacheKey);
            }
            // POLLING: ожидание что poller держит данные свежими; если нет данных — fall back на запрос
            return handleOnPolling(cacheKey);
        } catch (Exception e) {
            if (e instanceof WeatherSdkException) {
                throw e;
            }
            throw new WeatherSdkException(String.format(TERMINATOR_UNEXPECTED_MESSAGE, cacheKey), e);
        }
    }

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
