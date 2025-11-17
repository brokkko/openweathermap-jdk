package com.github.brokkko.openweathermap.jdk.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherTimeoutExceptionTest {

    @Test
    void testConstructor() {
        Throwable cause = new RuntimeException("timeout");
        WeatherTimeoutException ex = new WeatherTimeoutException("Request timed out", cause);
        assertEquals("Request timed out", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}
