package com.github.brokkko.openweathermap.jdk.models;

import com.github.brokkko.openweathermap.jdk.exceptions.InvalidWeatherValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HumidityTest {

    @Test
    void testValidCreation() {
        Humidity h = Humidity.withValue(45);
        assertEquals(45, h.getValue());
    }

    @Test
    void testInvalidLowValue() {
        assertThrows(
                InvalidWeatherValueException.class,
                () -> Humidity.withValue(-1)
        );
    }

    @Test
    void testInvalidHighValue() {
        assertThrows(
                InvalidWeatherValueException.class,
                () -> Humidity.withValue(101)
        );
    }

    @Test
    void testSetValidValue() {
        Humidity h = Humidity.withValue(10);
        h.setValue(90);
        assertEquals(90, h.getValue());
    }

    @Test
    void testSetInvalidValue() {
        Humidity h = Humidity.withValue(10);
        assertThrows(
                InvalidWeatherValueException.class,
                () -> h.setValue(200)
        );
    }

    @Test
    void testEqualsAndHashCode() {
        Humidity a = Humidity.withValue(50);
        Humidity b = Humidity.withValue(50);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testToString() {
        Humidity h = Humidity.withValue(77);
        assertEquals("Humidity: 77%", h.toString());
    }
}
