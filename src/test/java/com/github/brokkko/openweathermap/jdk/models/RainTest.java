package com.github.brokkko.openweathermap.jdk.models;

import com.github.brokkko.openweathermap.jdk.exceptions.InvalidWeatherValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RainTest {

    @Test
    @DisplayName("withOneHourLevelValue: valid value should create object")
    void testWithOneHourLevelValueValid() {
        Rain rain = Rain.withOneHourLevelValue(2.5);
        assertEquals(2.5, rain.getOneHourLevel());
    }

    @Test
    @DisplayName("withOneHourLevelValue: negative value should throw exception")
    void testWithOneHourLevelValueInvalid() {
        assertThrows(InvalidWeatherValueException.class,
                () -> Rain.withOneHourLevelValue(-1.0));
    }

    @Test
    @DisplayName("withThreeHourLevelValue: valid value should create object")
    void testWithThreeHourLevelValueValid() {
        Rain rain = Rain.withThreeHourLevelValue(4.0);
        assertEquals(4.0, rain.getThreeHourLevel());
    }

    @Test
    @DisplayName("withThreeHourLevelValue: negative value should throw exception")
    void testWithThreeHourLevelValueInvalid() {
        assertThrows(InvalidWeatherValueException.class,
                () -> Rain.withThreeHourLevelValue(-0.5));
    }

    @Test
    @DisplayName("withValues: valid values should create object")
    void testWithValuesValid() {
        Rain rain = Rain.withValues(1.3, 2.7);
        assertEquals(1.3, rain.getOneHourLevel());
        assertEquals(2.7, rain.getThreeHourLevel());
    }

    @Test
    @DisplayName("withValues: negative values should throw exception")
    void testWithValuesInvalid() {
        assertThrows(InvalidWeatherValueException.class,
                () -> Rain.withValues(1.0, -2.0));
        assertThrows(InvalidWeatherValueException.class,
                () -> Rain.withValues(-1.0, 2.0));
    }

    @Test
    @DisplayName("setOneHourLevel: valid value should update field")
    void testSetOneHourLevelValid() {
        Rain rain = Rain.withValues(0.0, 0.0);
        rain.setOneHourLevel(3.5);

        assertEquals(3.5, rain.getOneHourLevel());
    }

    @Test
    @DisplayName("setOneHourLevel: negative value should throw exception")
    void testSetOneHourLevelInvalid() {
        Rain rain = Rain.withValues(0.0, 0.0);
        assertThrows(InvalidWeatherValueException.class,
                () -> rain.setOneHourLevel(-1.0));
    }

    @Test
    @DisplayName("setThreeHourLevel: valid value should update field")
    void testSetThreeHourLevelValid() {
        Rain rain = Rain.withValues(0.0, 0.0);
        rain.setThreeHourLevel(1.8);

        assertEquals(1.8, rain.getThreeHourLevel());
    }

    @Test
    @DisplayName("setThreeHourLevel: negative value should throw exception")
    void testSetThreeHourLevelInvalid() {
        Rain rain = Rain.withValues(0.0, 0.0);
        assertThrows(InvalidWeatherValueException.class,
                () -> rain.setThreeHourLevel(-3.0));
    }

    @Test
    @DisplayName("equals & hashCode: equal objects should be equal")
    void testEqualsAndHashCodeEqual() {
        Rain r1 = Rain.withValues(1.0, 2.0);
        Rain r2 = Rain.withValues(1.0, 2.0);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    @DisplayName("equals: different objects should NOT be equal")
    void testEqualsDifferent() {
        Rain r1 = Rain.withValues(1.0, 2.0);
        Rain r2 = Rain.withValues(2.0, 3.0);

        assertNotEquals(r1, r2);
    }

    @Test
    @DisplayName("toString: should contain both values and units")
    void testToString() {
        Rain rain = Rain.withValues(1.5, 3.2);
        String s = rain.toString();

        assertTrue(s.contains("1.5"));
        assertTrue(s.contains("3.2"));
        assertTrue(s.contains("mm"));
    }
}
