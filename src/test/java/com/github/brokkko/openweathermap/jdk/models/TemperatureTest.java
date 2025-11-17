package com.github.brokkko.openweathermap.jdk.models;

import com.github.brokkko.openweathermap.jdk.exceptions.InvalidWeatherValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureTest {

    @Test
    void testCreateWithValue() {
        Temperature t = Temperature.withValue(15.5, "°C");

        assertEquals(15.5, t.getValue());
        assertEquals("°C", t.getUnit());
        assertNull(t.getMaxTemperature());
        assertNull(t.getMinTemperature());
        assertNull(t.getFeelsLike());
    }

    @Test
    void testUnitCannotBeNull() {
        assertThrows(
                InvalidWeatherValueException.class,
                () -> Temperature.withValue(10.0, null)
        );
    }

    @Test
    void testSetUnitCannotBeNull() {
        Temperature t = Temperature.withValue(10.0, "°C");
        assertThrows(
                InvalidWeatherValueException.class,
                () -> t.setUnit(null)
        );
    }

    @Test
    void testSetMaxTemperature() {
        Temperature t = Temperature.withValue(10.0, "°C");
        t.setMaxTemperature(20.0);

        assertEquals(20.0, t.getMaxTemperature());
    }

    @Test
    void testSetMinTemperature() {
        Temperature t = Temperature.withValue(10.0, "°C");
        t.setMinTemperature(5.0);

        assertEquals(5.0, t.getMinTemperature());
    }

    @Test
    void testSetFeelsLike() {
        Temperature t = Temperature.withValue(10.0, "°C");
        t.setFeelsLike(9.0);

        assertEquals(9.0, t.getFeelsLike());
    }

    @Test
    void testEqualsAndHashCode() {
        Temperature t1 = Temperature.withValue(10.0, "°C");
        t1.setFeelsLike(9.0);

        Temperature t2 = Temperature.withValue(10.0, "°C");
        t2.setFeelsLike(9.0);

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void testToStringBase() {
        Temperature t = Temperature.withValue(15.0, "°C");
        assertEquals("Temperature: 15.0 °C", t.toString());
    }

    @Test
    void testToStringFull() {
        Temperature t = Temperature.withValue(10.0, "°C");
        t.setMaxTemperature(20.0);
        t.setMinTemperature(5.0);
        t.setFeelsLike(8.0);

        assertEquals(
                "Temperature: 10.0 °C, Maximum value: 20.0 °C, Minimum value: 5.0 °C, Feels like: 8.0 °C",
                t.toString()
        );
    }
}
