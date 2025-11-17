package com.github.brokkko.openweathermap.jdk.retries.impl;

import com.github.brokkko.openweathermap.jdk.exceptions.RetryExhaustedException;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.retries.RetryableOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExponentialBackoffRetryPolicyTest {

    private ExponentialBackoffRetryPolicy policy;

    @BeforeEach
    void init() {
        WeatherLogger logger = mock(WeatherLogger.class);

        policy = spy(new ExponentialBackoffRetryPolicy(3, 100, logger));
    }

    @Test
    void testSuccessfulOnFirstAttempt() {
        RetryableOperation<String> op = mock(RetryableOperation.class);
        when(op.run()).thenReturn("OK");

        String result = policy.executeWithRetry(op);

        assertEquals("OK", result);
    }

    @Test
    void testRetriesAndThenSuccess() {
        RetryableOperation<String> op = mock(RetryableOperation.class);

        when(op.run())
                .thenThrow(new RuntimeException("fail1"))
                .thenThrow(new RuntimeException("fail2"))
                .thenReturn("OK");

        String result = policy.executeWithRetry(op);

        assertEquals("OK", result);
    }

    @Test
    void testExhaustedRetriesThrows() {
        RetryableOperation<String> op = mock(RetryableOperation.class);

        when(op.run()).thenThrow(new RuntimeException("boom"));

        assertThrows(RetryExhaustedException.class, () -> policy.executeWithRetry(op));
    }
}
