package com.github.brokkko.openweathermap.jdk.models;

import static org.junit.jupiter.api.Assertions.*;

import com.github.brokkko.openweathermap.jdk.exceptions.InvalidWeatherValueException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

class LocationTest {

    @Test
    void testValidCreation() {
        Location loc = Location.withValues(123, "Berlin");
        assertEquals(123, loc.getId());
        assertEquals("Berlin", loc.getName());
    }

    @Test
    void testInvalidName_null() {
        assertThrows(
                InvalidWeatherValueException.class,
                () -> Location.withValues(1, null)
        );
    }

    @Test
    void testInvalidName_blank() {
        assertThrows(
                InvalidWeatherValueException.class,
                () -> Location.withValues(1, "   ")
        );
    }

    @Test
    void testSetterValidName() {
        Location loc = Location.withValues(1, "Paris");
        loc.setName("London");
        assertEquals("London", loc.getName());
    }

    @Test
    void testSetterInvalidName() {
        Location loc = Location.withValues(1, "Paris");
        assertThrows(
                InvalidWeatherValueException.class,
                () -> loc.setName("")
        );
    }

    @Test
    void testCoordinateAssignment() {
        Location loc = Location.withValues(5, "Tokyo");
        Coordinate coord = Coordinate.of(35.0, 139.0);
        loc.setCoordinate(coord);

        assertEquals(coord, loc.getCoordinate());
    }

    @Test
    void testEqualsAndHashCode() {
        Location a = Location.withValues(1, "Rome");
        Location b = Location.withValues(1, "Rome");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testToStringContainsFields() {
        Location loc = Location.withValues(10, "Madrid");
        loc.setCountryCode("ES");

        String s = loc.toString();

        assertTrue(s.contains("Madrid"));
        assertTrue(s.contains("10"));
        assertTrue(s.contains("ES"));
    }

    @Test
    void testSetSunriseSunset() {
        Location loc = Location.withValues(1, "Oslo");

        LocalDateTime sunrise = LocalDateTime.now();
        LocalDateTime sunset = LocalDateTime.now().plusHours(5);
        ZoneOffset offset = ZoneOffset.UTC;

        loc.setSunriseTime(sunrise);
        loc.setSunsetTime(sunset);
        loc.setZoneOffset(offset);

        assertEquals(sunrise, loc.getSunriseTime());
        assertEquals(sunset, loc.getSunsetTime());
        assertEquals(offset, loc.getZoneOffset());
    }
}
