package com.github.brokkko.openweathermap.jdk.clients;

import com.github.brokkko.openweathermap.jdk.enums.LogLevel;
import com.github.brokkko.openweathermap.jdk.enums.LoggerType;
import com.github.brokkko.openweathermap.jdk.enums.RetryPolicyType;
import com.github.brokkko.openweathermap.jdk.enums.SdkMode;
import com.github.brokkko.openweathermap.jdk.exceptions.InvalidWeatherValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientConfigTest {

    @Test
    void build_shouldThrowException_whenApiKeyMissing() {
        ClientConfig.Builder builder = ClientConfig.builder();

        assertThrows(
                InvalidWeatherValueException.class,
                builder::build,
                "Missing API key must cause validation error"
        );
    }

    @Test
    void build_shouldApplyDefaultValues() {
        ClientConfig config = ClientConfig.builder()
                .apiKey("KEY")
                .build();

        assertEquals("KEY", config.getApiKey());
        assertEquals(SdkMode.ON_DEMAND, config.getMode());
        assertEquals(RetryPolicyType.NONE, config.getRetryPolicyType());
        assertEquals(LoggerType.DEFAULT, config.getLoggerType());
        assertEquals(LogLevel.INFO, config.getLogLevel());
        assertEquals(10, config.getHttpTimeoutSeconds());
    }

    @Test
    void build_shouldApplyCustomValues() {
        ClientConfig config = ClientConfig.builder()
                .apiKey("SECRET")
                .mode(SdkMode.POLLING_MODE)
                .retryPolicy(RetryPolicyType.EXPONENTIAL_BACKOFF)
                .logger(LoggerType.DEFAULT)
                .logLevel(LogLevel.DEBUG)
                .httpTimeoutSeconds(30)
                .build();

        assertEquals("SECRET", config.getApiKey());
        assertEquals(SdkMode.POLLING_MODE, config.getMode());
        assertEquals(RetryPolicyType.EXPONENTIAL_BACKOFF, config.getRetryPolicyType());
        assertEquals(LoggerType.DEFAULT, config.getLoggerType());
        assertEquals(LogLevel.DEBUG, config.getLogLevel());
        assertEquals(30, config.getHttpTimeoutSeconds());
    }

    @Test
    void builder_shouldBeReusableAndNotAffectPreviousInstances() {
        ClientConfig.Builder builder = ClientConfig.builder()
                .apiKey("A")
                .mode(SdkMode.ON_DEMAND);

        ClientConfig config1 = builder.build();

        builder
                .apiKey("B")
                .mode(SdkMode.POLLING_MODE);

        ClientConfig config2 = builder.build();

        assertNotEquals(config1.getApiKey(), config2.getApiKey());
        assertNotEquals(config1.getMode(), config2.getMode());
    }
}
