package com.github.brokkko.openweathermap.jdk.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherSdkExceptionTest {

    @Test
    void testMessageConstructor() {
        WeatherSdkException ex = new WeatherSdkException("error message");
        assertEquals("error message", ex.getMessage());
    }

    @Test
    void testMessageAndCauseConstructor() {
        Throwable cause = new RuntimeException("cause");
        WeatherSdkException ex = new WeatherSdkException("error message", cause);
        assertEquals("error message", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}