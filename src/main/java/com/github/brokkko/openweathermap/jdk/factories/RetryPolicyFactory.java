package com.github.brokkko.openweathermap.jdk.factories;

import com.github.brokkko.openweathermap.jdk.enums.RetryPolicyType;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.retries.RetryPolicy;
import com.github.brokkko.openweathermap.jdk.retries.impl.ExponentialBackoffRetryPolicy;
import com.github.brokkko.openweathermap.jdk.retries.impl.NoRetryPolicy;

import static com.github.brokkko.openweathermap.jdk.constants.DefaultValueConstants.DEFAULT_RETRY_POLICY_DELAY_MS;
import static com.github.brokkko.openweathermap.jdk.constants.DefaultValueConstants.DEFAULT_RETRY_POLICY_MAX_ATTEMPTS;

public class RetryPolicyFactory {
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
