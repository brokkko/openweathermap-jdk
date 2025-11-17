package com.github.brokkko.openweathermap.jdk.clients;

import com.github.brokkko.openweathermap.jdk.enums.LogLevel;
import com.github.brokkko.openweathermap.jdk.enums.LoggerType;
import com.github.brokkko.openweathermap.jdk.enums.RetryPolicyType;
import com.github.brokkko.openweathermap.jdk.enums.SdkMode;
import com.github.brokkko.openweathermap.jdk.exceptions.InvalidWeatherValueException;

/**
 * Immutable configuration object used to initialize and customize
 * the OpenWeatherMap SDK client. The configuration is constructed
 * via the {@link Builder} and includes:
 *
 * <ul>
 *     <li>API key required for all requests</li>
 *     <li>SDK execution mode ({@link SdkMode})</li>
 *     <li>Retry policy strategy ({@link RetryPolicyType})</li>
 *     <li>Logging system type ({@link LoggerType})</li>
 *     <li>Minimum log level to output ({@link LogLevel})</li>
 *     <li>HTTP timeout value in seconds</li>
 * </ul>
 *
 * The class performs validation of required fields during build time.
 */
public class ClientConfig {

    private final String apiKey;
    private final SdkMode mode;
    private final RetryPolicyType retryPolicyType;
    private final LoggerType loggerType;
    private final LogLevel logLevel;
    private final int httpTimeoutSeconds;

    /**
     * Returns a new {@link Builder} instance to create
     * a {@link ClientConfig} object.
     *
     * @return builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for constructing {@link ClientConfig}.
     * All fields have default values except the API key,
     * which must be explicitly specified.
     */
    public static class Builder {
        private String apiKey;
        private SdkMode mode = SdkMode.ON_DEMAND;

        private RetryPolicyType retryPolicyType = RetryPolicyType.NONE;
        private LoggerType loggerType = LoggerType.DEFAULT;
        private LogLevel logLevel = LogLevel.INFO;

        private int httpTimeoutSeconds = 10;

        /**
         * Creates builder instance.
         */
        public Builder() {}


        /**
         * Sets the API key. This value is required.
         *
         * @param apiKey OpenWeatherMap API key
         * @return this builder
         */
        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        /**
         * Sets the SDK execution mode.
         *
         * @param mode desired SDK mode
         * @return this builder
         */
        public Builder mode(SdkMode mode) {
            this.mode = mode;
            return this;
        }

        /**
         * Sets the retry policy strategy.
         *
         * @param type retry policy type
         * @return this builder
         */
        public Builder retryPolicy(RetryPolicyType type) {
            this.retryPolicyType = type;
            return this;
        }

        /**
         * Sets the logger type.
         *
         * @param type logger implementation
         * @return this builder
         */
        public Builder logger(LoggerType type) {
            this.loggerType = type;
            return this;
        }

        /**
         * Sets the minimum log level.
         *
         * @param level log level
         * @return this builder
         */
        public Builder logLevel(LogLevel level) {
            this.logLevel = level;
            return this;
        }

        /**
         * Sets the HTTP client timeout value.
         *
         * @param timeout timeout in seconds
         * @return this builder
         */
        public Builder httpTimeoutSeconds(int timeout) {
            this.httpTimeoutSeconds = timeout;
            return this;
        }

        /**
         * Builds a validated {@link ClientConfig} instance.
         *
         * @return client configuration
         * @throws InvalidWeatherValueException if an API key is missing
         */
        public ClientConfig build() {
            validateApiKey(apiKey);
            return new ClientConfig(apiKey, mode, retryPolicyType, logLevel, loggerType, httpTimeoutSeconds);
        }

        /**
         * Validate Api-key so it can't be null or blank
         * @param key Api-key
         * @throws InvalidWeatherValueException if an API key is missing or blank
         */
        private void validateApiKey(String key) {
            if (key == null) {
                throw new InvalidWeatherValueException("API key is required");
            }
            if (key.isBlank()) {
                throw new InvalidWeatherValueException("API key must not be empty or blank");
            }
        }
    }

    private ClientConfig(String apiKey, SdkMode mode,
                         RetryPolicyType retryPolicyType,
                         LogLevel logLevel,
                         LoggerType loggerType,
                         int httpTimeoutSeconds) {
        this.apiKey = apiKey;
        this.mode = mode;
        this.retryPolicyType = retryPolicyType;
        this.logLevel = logLevel;
        this.loggerType = loggerType;
        this.httpTimeoutSeconds = httpTimeoutSeconds;
    }

    /**
     * Returns the API key.
     *
     * @return API key string
     */
    public String getApiKey() { return apiKey; }

    /**
     * Returns the configured SDK mode.
     *
     * @return SDK mode
     */
    public SdkMode getMode() { return mode; }

    /**
     * Returns the retry policy type.
     *
     * @return retry policy
     */
    public RetryPolicyType getRetryPolicyType() { return retryPolicyType; }

    /**
     * Returns the minimum log level used by the SDK.
     *
     * @return log level
     */
    public LogLevel getLogLevel() { return logLevel; }

    /**
     * Returns logger type.
     *
     * @return logger type
     */
    public LoggerType getLoggerType() { return loggerType; }

    /**
     * Returns the HTTP timeout value in seconds.
     *
     * @return timeout value in seconds
     */
    public int getHttpTimeoutSeconds() { return httpTimeoutSeconds; }
}
