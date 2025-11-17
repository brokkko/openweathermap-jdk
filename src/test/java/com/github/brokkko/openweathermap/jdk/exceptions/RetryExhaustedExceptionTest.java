package com.github.brokkko.openweathermap.jdk.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RetryExhaustedExceptionTest {

    @Test
    void testConstructor() {
        Throwable cause = new RuntimeException("all retries failed");
        RetryExhaustedException ex = new RetryExhaustedException(3, cause);
        assertEquals("Retry attempts exceeded (3 attempts)", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}
