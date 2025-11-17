package com.github.brokkko.openweathermap.jdk.retries.impl;

import com.github.brokkko.openweathermap.jdk.exceptions.NoRetryException;
import com.github.brokkko.openweathermap.jdk.retries.RetryableOperation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NoRetryPolicyTest {

    @Test
    void testSuccessWithoutRetry() {
        NoRetryPolicy policy = new NoRetryPolicy();

        RetryableOperation<String> op = mock(RetryableOperation.class);
        when(op.run()).thenReturn("OK");

        String result = policy.executeWithRetry(op);

        assertEquals("OK", result);
    }

    @Test
    void testFailureWrappedIntoNoRetryException() {
        NoRetryPolicy policy = new NoRetryPolicy();

        RetryableOperation<String> op = mock(RetryableOperation.class);
        when(op.run()).thenThrow(new RuntimeException("boom"));

        NoRetryException ex = assertThrows(NoRetryException.class, () ->
                policy.executeWithRetry(op)
        );

        assertEquals("boom", ex.getCause().getMessage());
    }
}
