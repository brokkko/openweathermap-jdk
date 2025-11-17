package com.github.brokkko.openweathermap.jdk.models;

import com.github.brokkko.openweathermap.jdk.exceptions.InvalidWeatherValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WindTest {

    @Test
    void withValue_shouldCreateWind_whenValidInput() {
        Wind wind = Wind.withValue(5.5, "m/s");

        assertEquals(5.5, wind.getSpeed());
        assertEquals("m/s", wind.getUnit());
        assertNull(wind.getDegrees());
        assertNull(wind.getGust());
    }

    @Test
    void withValue_shouldThrow_whenSpeedNegative() {
        assertThrows(InvalidWeatherValueException.class,
                () -> Wind.withValue(-1, "m/s"));
    }

    @Test
    void withValue_shouldThrow_whenUnitNull() {
        assertThrows(InvalidWeatherValueException.class,
                () -> Wind.withValue(5, null));
    }

    @Test
    void setSpeed_shouldUpdate_whenValid() {
        Wind wind = Wind.withValue(3, "m/s");
        wind.setSpeed(10);

        assertEquals(10, wind.getSpeed());
    }

    @Test
    void setSpeed_shouldThrow_whenNegative() {
        Wind wind = Wind.withValue(3, "m/s");

        assertThrows(InvalidWeatherValueException.class,
                () -> wind.setSpeed(-0.1));
    }

    @Test
    void setGust_shouldStoreValue_whenValid() {
        Wind wind = Wind.withValue(3, "m/s");
        wind.setGust(7);

        assertEquals(7, wind.getGust());
    }

    @Test
    void setGust_shouldThrow_whenNegative() {
        Wind wind = Wind.withValue(3, "m/s");

        assertThrows(InvalidWeatherValueException.class,
                () -> wind.setGust(-5));
    }

    @Test
    void setDegrees_shouldSet_whenInRange() {
        Wind wind = Wind.withValue(2, "m/s");
        wind.setDegrees(180);

        assertEquals(180, wind.getDegrees());
    }

    @Test
    void setDegrees_shouldThrow_whenBelowZero() {
        Wind wind = Wind.withValue(2, "m/s");

        assertThrows(InvalidWeatherValueException.class,
                () -> wind.setDegrees(-1));
    }

    @Test
    void setDegrees_shouldThrow_whenAbove360() {
        Wind wind = Wind.withValue(2, "m/s");

        assertThrows(InvalidWeatherValueException.class,
                () -> wind.setDegrees(361));
    }

    @Test
    void setUnit_shouldUpdate_whenValid() {
        Wind wind = Wind.withValue(2, "m/s");
        wind.setUnit("km/h");

        assertEquals("km/h", wind.getUnit());
    }

    @Test
    void setUnit_shouldThrow_whenNull() {
        Wind wind = Wind.withValue(2, "m/s");

        assertThrows(InvalidWeatherValueException.class,
                () -> wind.setUnit(null));
    }

    @Test
    void equalsAndHashCode_shouldWorkForEqualObjects() {
        Wind w1 = Wind.withValue(5, "m/s");
        Wind w2 = Wind.withValue(5, "m/s");

        w1.setDegrees(100);
        w2.setDegrees(100);

        w1.setGust(12);
        w2.setGust(12);

        assertEquals(w1, w2);
        assertEquals(w1.hashCode(), w2.hashCode());
    }

    @Test
    void equals_shouldReturnFalse_forDifferentObjects() {
        Wind w1 = Wind.withValue(5, "m/s");
        Wind w2 = Wind.withValue(7, "m/s");

        assertNotEquals(w1, w2);
    }

    @Test
    void toString_shouldContainAllValues() {
        Wind wind = Wind.withValue(3, "m/s");
        wind.setDegrees(150);
        wind.setGust(6);

        String s = wind.toString();

        assertTrue(s.contains("Wind speed: 3"));
        assertTrue(s.contains("m/s"));
        assertTrue(s.contains("degrees: 150"));
        assertTrue(s.contains("Gust: 6"));
    }

    @Test
    void toString_shouldNotContainGust_whenNull() {
        Wind wind = Wind.withValue(3, "m/s");
        wind.setDegrees(90);

        String s = wind.toString();
        assertFalse(s.contains("Gust"));
    }
}
