package com.github.brokkko.openweathermap.jdk.clients;

import com.github.brokkko.openweathermap.jdk.enums.LogLevel;
import com.github.brokkko.openweathermap.jdk.enums.LoggerType;
import com.github.brokkko.openweathermap.jdk.enums.RetryPolicyType;
import com.github.brokkko.openweathermap.jdk.enums.SdkMode;
import com.github.brokkko.openweathermap.jdk.factories.LoggerFactory;
import com.github.brokkko.openweathermap.jdk.factories.RetryPolicyFactory;
import com.github.brokkko.openweathermap.jdk.http.WeatherHttpExecutor;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.request.RequestSettings;
import com.github.brokkko.openweathermap.jdk.request.requsters.WeatherLocationRequester;
import com.github.brokkko.openweathermap.jdk.retries.RetryPolicy;
import com.github.brokkko.openweathermap.jdk.retries.impl.NoRetryPolicy;
import com.github.brokkko.openweathermap.jdk.services.WeatherCacheService;
import com.github.brokkko.openweathermap.jdk.schedulers.WeatherPollingScheduler;
import com.github.brokkko.openweathermap.jdk.services.impl.WeatherCacheServiceImpl;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.github.brokkko.openweathermap.jdk.constants.DefaultValueConstants.*;
import static com.github.brokkko.openweathermap.jdk.constants.LogMessages.*;

/**
 * High-level client for interacting with the OpenWeatherMap API.
 * <p>
 * This class encapsulates all SDK functionality: executing HTTP requests,
 * managing cache, handling retry policies, configuring logging, and optionally
 * running background polling to keep weather data up-to-date.
 * <p>
 * The client can operate in two modes:
 * <ul>
 *     <li>{@link SdkMode#ON_DEMAND} — requests are executed only when invoked.</li>
 *     <li>{@link SdkMode#POLLING_MODE} — a background scheduler periodically polls
 *         weather data and updates the internal cache.</li>
 * </ul>
 *
 * <p>The client is thread-safe. When no longer needed, call {@link #destroy()}
 * to terminate executors, stop polling, and clear cached data.</p>
 */
public class OpenWeatherMapClient {
    private final String apiKey;
    private final String baseUrl;
    private final SdkMode sdkMode;
    private final WeatherHttpExecutor httpExecutor;
    private final WeatherCacheService cacheService;
    private final ScheduledExecutorService pollingExecutor;
    private final WeatherPollingScheduler pollingScheduler;
    private final WeatherLogger logger;

    /**
     * Creates a new OpenWeatherMap SDK client instance.
     *
     * @param apiKey                 your OpenWeatherMap API key (must not be null)
     * @param sdkMode                SDK operation mode (ON_DEMAND or POLLING_MODE)
     * @param baseUrl                base API URL (must not be null)
     * @param cacheService           cache implementation used by the client
     * @param executor               HTTP executor responsible for performing requests
     * @param logger                 logger instance used for internal diagnostics
     * @param pollingIntervalMinutes interval (in minutes) between polling iterations
     *                               when {@link SdkMode#POLLING_MODE} is enabled
     *
     * @throws NullPointerException if any required argument is null
     */
    public OpenWeatherMapClient(String apiKey,
                                SdkMode sdkMode,
                                String baseUrl,
                                WeatherCacheService cacheService,
                                WeatherHttpExecutor executor,
                                WeatherLogger logger,
                                int pollingIntervalMinutes) {
        this.apiKey = Objects.requireNonNull(apiKey, "apiKey must not be null");
        this.sdkMode = Objects.requireNonNull(sdkMode, "sdkMode must not be null");
        this.baseUrl = Objects.requireNonNull(baseUrl, "baseUrl must not be null");
        this.cacheService = Objects.requireNonNull(cacheService, "cacheService must not be null");
        this.httpExecutor = Objects.requireNonNull(executor, "httpExecutor must not be null");
        this.logger = logger;

        if (this.sdkMode == SdkMode.POLLING_MODE) {
            this.pollingExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "owm-poller-" + Math.abs(apiKey.hashCode()));
                t.setDaemon(true);
                return t;
            });
            this.pollingScheduler = new WeatherPollingScheduler(this.cacheService, this.httpExecutor, this.logger);
            // schedule with initial delay 0 (first run immediately), then fixed interval
            this.pollingExecutor.scheduleAtFixedRate(() -> {
                try {
                    pollingScheduler.pollOnce();
                } catch (Throwable t) {
                    logger.error(POLLING_TOPLEVEL_ERROR_MESSAGE, t);
                }
            }, 0L, Math.max(1, pollingIntervalMinutes), TimeUnit.MINUTES);
            logger.info(String.format(POLLING_STARTED_MESSAGE, pollingIntervalMinutes));
        } else {
            this.pollingExecutor = null;
            this.pollingScheduler = null;
        }
    }

    /**
     * Gets client sdk mode.
     * @return the SDK operation mode ({@link SdkMode#ON_DEMAND} or {@link SdkMode#POLLING_MODE})
     */
    public SdkMode getSdkMode() {
        return this.sdkMode;
    }

    /**
     * Gets cache service.
     * @return internal weather data cache service
     */
    public WeatherCacheService getCacheService() {
        return this.cacheService;
    }

    /**
     * Gets HTTP executor.
     * @return HTTP executor used to send OpenWeatherMap API requests
     */
    public WeatherHttpExecutor getHttpExecutor() {
        return httpExecutor;
    }

    /**
     * Gets API key.
     * @return configured OpenWeatherMap API key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Gets base URL.
     * @return base URL used for API requests
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Creates a new {@link WeatherLocationRequester} used to build and execute
     * fluent weather requests.
     * <p>
     * A {@link RequestSettings} instance is created with the client's API key
     * and base URL. The requester receives a reference to this client so that
     * terminal operations can check mode, access the cache, or delegate execution.
     *
     * @return a starting point for fluent weather queries
     */
    public WeatherLocationRequester query() {
        RequestSettings settings = new RequestSettings(apiKey);
        settings.appendToURL(baseUrl);
        return new WeatherLocationRequester(this, logger, settings);
    }

    /**
     * Gracefully shuts down the client and releases resources.
     * <p>
     * This method:
     * <ul>
     *     <li>Stops polling executor (if the client is in POLLING_MODE)</li>
     *     <li>Waits for running tasks to complete</li>
     *     <li>Clears weather cache</li>
     *     <li>Logs shutdown lifecycle events</li>
     * </ul>
     *
     * After calling this method, the client should not be reused.
     */
    public void destroy() {
        logger.info(String.format(CLIENT_DESTROY_MESSAGE, apiKey));
        if (pollingExecutor != null) {
            try {
                pollingExecutor.shutdownNow();
                if (!pollingExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    logger.warn(POLLING_EXECUTOR_TIMEOUT_MESSAGE);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error(POLLING_INTERRUPTED_MESSAGE, e);
            }
        }
        try {
            cacheService.clear();
        } catch (Throwable t) {
            logger.error(POLLING_CACHE_ERROR_MESSAGE, t);
        }
        logger.info(String.format(CLIENT_DESTROY_MESSAGE, apiKey));
    }

    /**
     * Creates a new {@link Builder} for constructing client instances.
     *
     * @return fresh builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for configuring and constructing {@link OpenWeatherMapClient}.
     * <p>
     * Provides fluent methods for defining API key, timeouts, retry policy,
     * logging, polling intervals, and SDK mode.
     *
     * <p>Minimal valid configuration requires only an API key. All other fields
     * have reasonable defaults.
     */
    public static class Builder {
        private String apiKey;
        private SdkMode mode = SdkMode.ON_DEMAND;

        private HttpClient httpClient;
        private RetryPolicy retryPolicy;
        private RetryPolicyType retryPolicyType;
        private WeatherLogger logger;
        private LogLevel logLevel;

        private int httpTimeoutSeconds = DEFAULT_HTTP_TIMEOUT_SEC;

        private int pollingIntervalMinutes = DEFAULT_POLLING_INTERVAL_MIN;

        /**
         * Creates builder instance.
         */
        public Builder() {}

        /**
         * Sets an API key for OpenWeatherMap API.
         * The key must be a non-null, non-empty string.
         *
         * @param apiKey OpenWeatherMap API key
         * @return this builder
         */
        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        /**
         * Configures SDK operation mode.
         *
         * @param mode ON_DEMAND or POLLING_MODE
         * @return this builder
         */
        public Builder mode(SdkMode mode) {
            this.mode = mode;
            return this;
        }

        /**
         * Sets retry policy strategy to be used by HTTP executor.
         *
         * @param retryPolicyType retry strategy enum
         * @return this builder
         */
        public Builder retryPolicy(RetryPolicyType retryPolicyType) {
            this.retryPolicyType = retryPolicyType;
            return this;
        }

        /**
         * Sets SDK log verbosity level.
         *
         * @param level log level
         * @return this builder
         */
        public Builder logLevel(LogLevel level) {
            this.logLevel = level;
            return this;
        }

        /**
         * Sets a logger type for internal diagnostics output.
         *
         * @param loggerType logger type identifier
         * @return this builder
         */
        public Builder logger(LoggerType loggerType) {
            this.logger = LoggerFactory.create(loggerType, this.logLevel, this.getClass());
            return this;
        }

        /**
         * Sets the HTTP request timeout.
         *
         * @param seconds connection timeout in seconds
         * @return this builder
         */
        public Builder httpTimeoutSeconds(int seconds) {
            this.httpTimeoutSeconds = seconds;
            return this;
        }

        /**
         * Sets interval for background polling (minutes).
         * Applies only if mode = POLLING_MODE.
         *
         * @param minutes interval between polling tasks
         * @return this builder
         */
        public Builder pollingIntervalMinutes(int minutes) {
            this.pollingIntervalMinutes = minutes;
            return this;
        }

        /**
         * Builds and returns a fully configured {@link OpenWeatherMapClient}.
         * <p>
         * The builder:
         * <ul>
         *     <li>Creates an HTTP client if one is not provided</li>
         *     <li>Creates a retry policy instance</li>
         *     <li>Creates internal cache service</li>
         *     <li>Configures logger and executor</li>
         *     <li>Instantiates the client</li>
         * </ul>
         *
         * @return new OpenWeatherMapClient instance
         * @throws IllegalArgumentException if apiKey is missing or invalid
         */
        public OpenWeatherMapClient build() {
            if (httpClient == null) {
                httpClient = HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(httpTimeoutSeconds))
                        .version(HttpClient.Version.HTTP_1_1)
                        .build();
            }

            if (retryPolicy == null) {
                retryPolicy = new NoRetryPolicy();
            } else {
                retryPolicy = RetryPolicyFactory.create(retryPolicyType ,logger);
            }

            WeatherHttpExecutor executor = new WeatherHttpExecutor(
                    httpClient,
                    retryPolicy,
                    logger
            );

            WeatherCacheService cacheService = new WeatherCacheServiceImpl(DEFAULT_CACHE_MAX_ENTRIES, DEFAULT_CACHE_TTL_MS, logger);

            return new OpenWeatherMapClient(
                    apiKey,
                    mode,
                    DEFAULT_BASE_URL,
                    cacheService,
                    executor,
                    logger,
                    pollingIntervalMinutes
            );
        }
    }
}
