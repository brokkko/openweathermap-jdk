package com.github.brokkko.openweathermap.jdk.models;

import com.github.brokkko.openweathermap.jdk.exceptions.InvalidWeatherValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SnowTest {

    @Test
    void testCreateWithOneHourLevelValue() {
        Snow snow = Snow.withOneHourLevelValue(5.0);

        assertEquals(5.0, snow.getOneHourLevel());
        assertNull(snow.getThreeHourLevel());
        assertEquals("mm", snow.getUnit());
    }

    @Test
    void testCreateWithThreeHourLevelValue() {
        Snow snow = Snow.withThreeHourLevelValue(7.5);

        assertEquals(7.5, snow.getThreeHourLevel());
        assertNull(snow.getOneHourLevel());
        assertEquals("mm", snow.getUnit());
    }

    @Test
    void testCreateWithValues() {
        Snow snow = Snow.withValues(3.0, 8.0);

        assertEquals(3.0, snow.getOneHourLevel());
        assertEquals(8.0, snow.getThreeHourLevel());
    }

    @Test
    void testNegativeOneHourValue() {
        assertThrows(
                InvalidWeatherValueException.class,
                () -> Snow.withOneHourLevelValue(-1.0)
        );
    }

    @Test
    void testNegativeThreeHourValue() {
        assertThrows(
                InvalidWeatherValueException.class,
                () -> Snow.withThreeHourLevelValue(-5.0)
        );
    }

    @Test
    void testNegativeValueInWithValues() {
        assertThrows(
                InvalidWeatherValueException.class,
                () -> Snow.withValues(-1.0, 3.0)
        );

        assertThrows(
                InvalidWeatherValueException.class,
                () -> Snow.withValues(1.0, -3.0)
        );
    }

    @Test
    void testEqualsAndHashCode() {
        Snow s1 = Snow.withValues(1.0, 2.0);
        Snow s2 = Snow.withValues(1.0, 2.0);

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testToStringForOnlyOneHour() {
        Snow snow = Snow.withOneHourLevelValue(4.2);

        assertEquals("1-hour snow level: 4.2mm", snow.toString());
    }

    @Test
    void testToStringForOnlyThreeHours() {
        Snow snow = Snow.withThreeHourLevelValue(6.7);

        assertEquals("3-hours snow level: 6.7mm", snow.toString());
    }

    @Test
    void testToStringForBothValues() {
        Snow snow = Snow.withValues(1.0, 2.0);

        assertEquals("1-hour snow level: 1.0mm, 3-hours snow level: 2.0mm", snow.toString());
    }
}
