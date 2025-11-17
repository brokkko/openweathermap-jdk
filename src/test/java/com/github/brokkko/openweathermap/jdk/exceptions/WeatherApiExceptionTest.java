package com.github.brokkko.openweathermap.jdk.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherApiExceptionTest {

    @Test
    void testStatusCodeConstructor() {
        WeatherApiException ex = new WeatherApiException("API error", 404);
        assertEquals("API error", ex.getMessage());
        assertEquals(404, ex.getStatusCode());
    }

    @Test
    void testStatusCodeWithCause() {
        Throwable cause = new RuntimeException("cause");
        WeatherApiException ex = new WeatherApiException("API error", 500, cause);
        assertEquals("API error", ex.getMessage());
        assertEquals(500, ex.getStatusCode());
        assertEquals(cause, ex.getCause());
    }
}
