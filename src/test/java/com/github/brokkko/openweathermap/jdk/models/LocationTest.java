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

    @Test
    void testSetId() {
        Location loc = Location.withValues(100, "Paris");
        loc.setId(200);
        assertEquals(200, loc.getId());
    }

    @Test
    void testToStringWithCoordinate() {
        Location loc = Location.withValues(50, "Berlin");
        Coordinate coord = Coordinate.of(52.52, 13.40);
        loc.setCoordinate(coord);
        loc.setCountryCode("DE");

        String s = loc.toString();

        assertTrue(s.contains("Berlin"));
        assertTrue(s.contains("50"));
        assertTrue(s.contains("DE"));
    }

    @Test
    void testToStringWithoutCountryCode() {
        Location loc = Location.withValues(10, "Tokyo");

        String s = loc.toString();

        assertTrue(s.contains("Tokyo"));
        assertTrue(s.contains("10"));
        assertFalse(s.contains("("));
    }

    @Test
    void testEqualsWithDifferentIds() {
        Location a = Location.withValues(1, "Rome");
        Location b = Location.withValues(2, "Rome");

        assertNotEquals(a, b);
    }

    @Test
    void testEqualsWithDifferentNames() {
        Location a = Location.withValues(1, "Rome");
        Location b = Location.withValues(1, "Milan");

        assertNotEquals(a, b);
    }

    @Test
    void testEqualsWithSameObject() {
        Location loc = Location.withValues(1, "Rome");
        assertEquals(loc, loc);
    }

    @Test
    void testEqualsWithNull() {
        Location loc = Location.withValues(1, "Rome");
        assertNotEquals(loc, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        Location loc = Location.withValues(1, "Rome");
        assertNotEquals(loc, "Rome");
    }

    @Test
    void testEqualsWithAllFields() {
        Location a = Location.withValues(1, "Rome");
        Location b = Location.withValues(1, "Rome");

        LocalDateTime sunrise = LocalDateTime.of(2023, 11, 15, 6, 30);
        LocalDateTime sunset = LocalDateTime.of(2023, 11, 15, 17, 0);
        ZoneOffset offset = ZoneOffset.ofHours(1);
        Coordinate coord = Coordinate.of(41.9, 12.5);

        a.setCountryCode("IT");
        a.setSunriseTime(sunrise);
        a.setSunsetTime(sunset);
        a.setZoneOffset(offset);
        a.setCoordinate(coord);

        b.setCountryCode("IT");
        b.setSunriseTime(sunrise);
        b.setSunsetTime(sunset);
        b.setZoneOffset(offset);
        b.setCoordinate(coord);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
