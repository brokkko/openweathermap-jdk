package com.github.brokkko.openweathermap.jdk.models;

import com.github.brokkko.openweathermap.jdk.enums.WeatherCondition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeatherStateTest {

    @Test
    void testConstructorAndGetters() {
        WeatherState state = new WeatherState(800, "Clear", "Sky is clear");

        assertEquals(800, state.getId());
        assertEquals("Clear", state.getName());
        assertEquals("Sky is clear", state.getDescription());
        assertNull(state.getIconId());
        assertNotNull(state.getWeatherConditionEnum());
        assertEquals(WeatherCondition.getById(800), state.getWeatherConditionEnum());
    }

    @Test
    void testSetIconId() {
        WeatherState state = new WeatherState(800, "Clear", "Sky is clear");

        state.setIconId("01d");
        assertEquals("01d", state.getIconId());
    }

    @Test
    void testGetWeatherIconUrl_UsesIconIdIfSet() {
        WeatherState state = new WeatherState(800, "Clear", "Sky is clear");
        state.setIconId("01d");

        String expectedUrl = WeatherCondition.getIconUrl("01d");
        assertEquals(expectedUrl, state.getWeatherIconUrl());
    }

    @Test
    void testGetWeatherIconUrl_UsesEnumWhenIconIdNull() {
        WeatherState state = new WeatherState(800, "Clear", "Sky is clear");

        String expectedUrl = state.getWeatherConditionEnum().getDayIconUrl();
        assertEquals(expectedUrl, state.getWeatherIconUrl());
    }

    @Test
    void testEqualsAndHashCode() {
        WeatherState s1 = new WeatherState(500, "Rain", "Light rain");
        WeatherState s2 = new WeatherState(500, "Rain", "Light rain");

        s1.setIconId("10d");
        s2.setIconId("10d");

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testEqualsDifferentObjects() {
        WeatherState s1 = new WeatherState(500, "Rain", "Light rain");
        WeatherState s2 = new WeatherState(800, "Clear", "Sky is clear");

        assertNotEquals(s1, s2);
    }

    @Test
    void testToString() {
        WeatherState state = new WeatherState(800, "Clear", "Sky is clear");

        assertEquals("Weather state: Clear(Sky is clear).", state.toString());
    }
}
