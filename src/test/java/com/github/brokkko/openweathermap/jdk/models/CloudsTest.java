package com.github.brokkko.openweathermap.jdk.models;

import com.github.brokkko.openweathermap.jdk.exceptions.InvalidWeatherValueException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CloudsTest {

    @Test
    void testValidCreation() {
        Clouds c = Clouds.withValue((byte) 50);
        assertEquals(50, c.getValue());
    }

    @Test
    void testCreationWithLowInvalidValue() {
        assertThrows(
                InvalidWeatherValueException.class,
                () -> Clouds.withValue((byte) -1)
        );
    }

    @Test
    void testCreationWithHighInvalidValue() {
        assertThrows(
                InvalidWeatherValueException.class,
                () -> Clouds.withValue((byte) 101)
        );
    }

    @Test
    void testSetValidValue() {
        Clouds c = Clouds.withValue((byte) 20);
        c.setValue((byte) 80);
        assertEquals(80, c.getValue());
    }

    @Test
    void testSetInvalidLowValue() {
        Clouds c = Clouds.withValue((byte) 20);
        assertThrows(
                InvalidWeatherValueException.class,
                () -> c.setValue((byte) -5)
        );
    }

    @Test
    void testSetInvalidHighValue() {
        Clouds c = Clouds.withValue((byte) 20);
        assertThrows(
                InvalidWeatherValueException.class,
                () -> c.setValue((byte) 127)
        );
    }

    @Test
    void testUnit() {
        Clouds c = Clouds.withValue((byte) 30);
        assertEquals("%", c.getUnit());
    }

    @Test
    void testEqualsAndHashCode() {
        Clouds a = Clouds.withValue((byte) 70);
        Clouds b = Clouds.withValue((byte) 70);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testToString() {
        Clouds c = Clouds.withValue((byte) 45);
        assertEquals("Clouds: 45%", c.toString());
    }
}
