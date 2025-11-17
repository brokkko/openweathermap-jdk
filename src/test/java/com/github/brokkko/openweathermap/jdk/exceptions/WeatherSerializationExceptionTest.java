package com.github.brokkko.openweathermap.jdk.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherSerializationExceptionTest {

    @Test
    void testConstructor() {
        Throwable cause = new RuntimeException("JSON parse error");
        WeatherSerializationException ex = new WeatherSerializationException("Failed to parse JSON", cause);
        assertEquals("Failed to parse JSON", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}
