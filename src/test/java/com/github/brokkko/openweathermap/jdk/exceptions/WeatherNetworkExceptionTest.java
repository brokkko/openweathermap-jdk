package com.github.brokkko.openweathermap.jdk.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherNetworkExceptionTest {

    @Test
    void testConstructor() {
        Throwable cause = new RuntimeException("network issue");
        WeatherNetworkException ex = new WeatherNetworkException("Network failure", cause);
        assertEquals("Network failure", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}
