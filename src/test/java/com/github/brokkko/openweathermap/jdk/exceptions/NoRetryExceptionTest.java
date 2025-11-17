package com.github.brokkko.openweathermap.jdk.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NoRetryExceptionTest {

    @Test
    void testConstructor() {
        Throwable cause = new RuntimeException("operation failed");
        NoRetryException ex = new NoRetryException(cause);
        assertEquals("Operation failed and retry is disabled", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}
