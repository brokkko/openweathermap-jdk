package com.github.brokkko.openweathermap.jdk.factories;

import com.github.brokkko.openweathermap.jdk.enums.RetryPolicyType;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.retries.RetryPolicy;
import com.github.brokkko.openweathermap.jdk.retries.impl.ExponentialBackoffRetryPolicy;
import com.github.brokkko.openweathermap.jdk.retries.impl.NoRetryPolicy;

import static com.github.brokkko.openweathermap.jdk.constants.DefaultValueConstants.DEFAULT_RETRY_POLICY_DELAY_MS;
import static com.github.brokkko.openweathermap.jdk.constants.DefaultValueConstants.DEFAULT_RETRY_POLICY_MAX_ATTEMPTS;

/**
 * Factory class for creating {@link RetryPolicy} implementations based on
 * the selected {@link RetryPolicyType}.
 * <p>
 * The factory encapsulates the logic of selecting and configuring retry policies,
 * such as no-retry or exponential backoff strategies.
 */
public class RetryPolicyFactory {

    /**
     * Creates a {@link RetryPolicy} instance for the specified retry policy type.
     *
     * @param type   the type of retry strategy
     * @param logger the logger used by retry policy implementations
     * @return a configured {@link RetryPolicy} instance
     * @throws NullPointerException if {@code type} or {@code logger} is null
     */
    public static RetryPolicy create(RetryPolicyType type, WeatherLogger logger) {
        return switch (type) {
            case NONE -> new NoRetryPolicy();
            case EXPONENTIAL_BACKOFF -> new ExponentialBackoffRetryPolicy(
                    DEFAULT_RETRY_POLICY_MAX_ATTEMPTS,
                    DEFAULT_RETRY_POLICY_DELAY_MS,
                    logger
            );
        };
    }
}

