package com.github.brokkko.openweathermap.jdk.factories;

import com.github.brokkko.openweathermap.jdk.enums.LogLevel;
import com.github.brokkko.openweathermap.jdk.enums.RetryPolicyType;
import com.github.brokkko.openweathermap.jdk.logging.impl.DefaultWeatherLogger;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.retries.RetryPolicy;
import com.github.brokkko.openweathermap.jdk.retries.impl.ExponentialBackoffRetryPolicy;
import com.github.brokkko.openweathermap.jdk.retries.impl.NoRetryPolicy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RetryPolicyFactoryTest {

    private final WeatherLogger logger =
            new DefaultWeatherLogger(RetryPolicyFactoryTest.class, LogLevel.INFO);

    @Test
    void testCreateNoRetryPolicy() {
        RetryPolicy policy = RetryPolicyFactory.create(
                RetryPolicyType.NONE,
                logger
        );

        assertNotNull(policy);
        assertInstanceOf(NoRetryPolicy.class, policy);
    }

    @Test
    void testCreateExponentialBackoffPolicy() {
        RetryPolicy policy = RetryPolicyFactory.create(
                RetryPolicyType.EXPONENTIAL_BACKOFF,
                logger
        );

        assertNotNull(policy);
        assertInstanceOf(ExponentialBackoffRetryPolicy.class, policy);
    }

    @Test
    void testNullArguments() {
        assertThrows(NullPointerException.class,
                () -> RetryPolicyFactory.create(null, logger)
        );
    }
}
