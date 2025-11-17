package com.github.brokkko.openweathermap.jdk.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RetryExceptionTest {

    @Test
    void testConstructor() {
        Throwable cause = new RuntimeException("retry failure");
        RetryException ex = new RetryException("Retry failed", cause);
        assertEquals("Retry failed", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}
