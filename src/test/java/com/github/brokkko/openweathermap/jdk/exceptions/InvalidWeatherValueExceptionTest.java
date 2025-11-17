package com.github.brokkko.openweathermap.jdk.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidWeatherValueExceptionTest {

    @Test
    void testConstructor() {
        InvalidWeatherValueException ex = new InvalidWeatherValueException("Invalid temperature value");
        assertEquals("Invalid temperature value", ex.getMessage());
    }
}
