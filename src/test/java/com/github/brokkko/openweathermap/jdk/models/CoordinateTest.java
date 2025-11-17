package com.github.brokkko.openweathermap.jdk.models;

import com.github.brokkko.openweathermap.jdk.exceptions.InvalidWeatherValueException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {

    @Test
    void testValidCreation() {
        Coordinate c = Coordinate.of(45.0, 90.0);

        assertEquals(45.0, c.getLatitude());
        assertEquals(90.0, c.getLongitude());
    }

    @Test
    void testInvalidLatitudeLow() {
        assertThrows(
                InvalidWeatherValueException.class,
                () -> Coordinate.of(-91.0, 10.0)
        );
    }

    @Test
    void testInvalidLatitudeHigh() {
        assertThrows(
                InvalidWeatherValueException.class,
                () -> Coordinate.of(91.0, 10.0)
        );
    }

    @Test
    void testInvalidLongitudeLow() {
        assertThrows(
                InvalidWeatherValueException.class,
                () -> Coordinate.of(10.0, -181.0)
        );
    }

    @Test
    void testInvalidLongitudeHigh() {
        assertThrows(
                InvalidWeatherValueException.class,
                () -> Coordinate.of(10.0, 181.0)
        );
    }

    @Test
    void testSetValidLatitude() {
        Coordinate c = Coordinate.of(0, 0);
        c.setLatitude(55.5);
        assertEquals(55.5, c.getLatitude());
    }

    @Test
    void testSetInvalidLatitude() {
        Coordinate c = Coordinate.of(0, 0);
        assertThrows(
                InvalidWeatherValueException.class,
                () -> c.setLatitude(100)
        );
    }

    @Test
    void testSetValidLongitude() {
        Coordinate c = Coordinate.of(0,0);
        c.setLongitude(-120.25);
        assertEquals(-120.25, c.getLongitude());
    }

    @Test
    void testSetInvalidLongitude() {
        Coordinate c = Coordinate.of(0, 0);
        assertThrows(
                InvalidWeatherValueException.class,
                () -> c.setLongitude(200)
        );
    }

    @Test
    void testEqualsAndHashCode() {
        Coordinate a = Coordinate.of(10, 20);
        Coordinate b = Coordinate.of(10, 20);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testToStringFormat() {
        Coordinate c = Coordinate.of(10.5, -20.25);

        String s = c.toString();
        assertTrue(s.contains("10° 30′ 0″"));
        assertTrue(s.contains("-20° -15′ 0″"));
    }
}
