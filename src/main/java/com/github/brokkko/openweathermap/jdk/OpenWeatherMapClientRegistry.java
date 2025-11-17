package com.github.brokkko.openweathermap.jdk;

import com.github.brokkko.openweathermap.jdk.clients.ClientConfig;
import com.github.brokkko.openweathermap.jdk.clients.OpenWeatherMapClient;
import com.github.brokkko.openweathermap.jdk.enums.LogLevel;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.logging.impl.DefaultWeatherLogger;
import com.github.brokkko.openweathermap.jdk.retries.RetryPolicy;

import java.util.concurrent.ConcurrentHashMap;

import static com.github.brokkko.openweathermap.jdk.constants.LogMessages.CLIENT_CREATED_MESSAGE;
import static com.github.brokkko.openweathermap.jdk.constants.LogMessages.CLIENT_REMOVED_MESSAGE;

/**
 * A global registry responsible for managing {@link OpenWeatherMapClient} instances.
 * <p>
 * The registry ensures that only one client instance is created per API key.
 * Subsequent calls with the same {@link ClientConfig} reuse the previously
 * created client, providing efficient resource usage and predictable lifecycle
 * management.
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Create new {@link OpenWeatherMapClient} instances on demand</li>
 *     <li>Return cached clients when the same API key is requested</li>
 *     <li>Destroy and remove clients when no longer needed</li>
 *     <li>Log all create/remove events via {@link WeatherLogger}</li>
 * </ul>
 *
 * <h2>Thread Safety</h2>
 * The internal registry uses a {@link ConcurrentHashMap}, ensuring that
 * concurrent calls to {@link #getOrCreate(ClientConfig)} do not result in
 * duplicate client instances.
 */
public class OpenWeatherMapClientRegistry {
    private static final ConcurrentHashMap<String, OpenWeatherMapClient> clients = new ConcurrentHashMap<>();
    private static final WeatherLogger logger =
            new DefaultWeatherLogger(OpenWeatherMapClientRegistry.class, LogLevel.INFO);

    /**
     * Creates a {@link OpenWeatherMapClientRegistry} instance.
     */
    public OpenWeatherMapClientRegistry() {}
    /**
     * Retrieves an existing {@link OpenWeatherMapClient} for the given
     * configuration, or creates a new one if it does not exist yet.
     *
     * @param config the configuration used to construct the client
     * @return an existing or newly created {@link OpenWeatherMapClient}
     *
     * Clients are uniquely identified and cached by API key.
     * All other fields in {@link ClientConfig} are only used when
     * creating a new instance and are ignored for cached ones.
     */
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

    /**
     * Removes the client associated with the specified API key and destroys it.
     * <p>
     * If no client exists for the given key, the method performs no action other
     * than writing a log entry.
     *
     * @param apiKey the API key whose associated client should be removed
     */
    public static void remove(String apiKey) {
        logger.info(String.format(CLIENT_REMOVED_MESSAGE, apiKey));
        OpenWeatherMapClient removed = clients.remove(apiKey);
        if (removed != null) removed.destroy();
    }
}
