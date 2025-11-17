package com.github.brokkko.openweathermap.jdk;

import com.github.brokkko.openweathermap.jdk.clients.ClientConfig;
import com.github.brokkko.openweathermap.jdk.clients.OpenWeatherMapClient;
import com.github.brokkko.openweathermap.jdk.enums.LogLevel;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.logging.impl.DefaultWeatherLogger;

import java.util.concurrent.ConcurrentHashMap;

import static com.github.brokkko.openweathermap.jdk.constants.LogMessages.CLIENT_CREATED_MESSAGE;
import static com.github.brokkko.openweathermap.jdk.constants.LogMessages.CLIENT_REMOVED_MESSAGE;

public class OpenWeatherMapClientRegistry {
    private static final ConcurrentHashMap<String, OpenWeatherMapClient> clients = new ConcurrentHashMap<>();
    private static final WeatherLogger logger =
            new DefaultWeatherLogger(OpenWeatherMapClientRegistry.class, LogLevel.INFO);

    public static OpenWeatherMapClient getOrCreate(ClientConfig config) {
        return clients.computeIfAbsent(config.getApiKey(), key -> {
            logger.info(String.format(CLIENT_CREATED_MESSAGE, key));
            return OpenWeatherMapClient.builder()
                    .apiKey(config.getApiKey())
                    .mode(config.getMode())
                    .retryPolicy(config.getRetryPolicyType())
                    .logLevel(config.getLogLevel())
                    .logger(config.getLoggerType())
                    .httpTimeoutSeconds(config.getHttpTimeoutSeconds())
                    .build();
        });
    }

    public static void remove(String apiKey) {
        logger.info(String.format(CLIENT_REMOVED_MESSAGE, apiKey));
        OpenWeatherMapClient removed = clients.remove(apiKey);
        if (removed != null) removed.destroy();
    }
}
