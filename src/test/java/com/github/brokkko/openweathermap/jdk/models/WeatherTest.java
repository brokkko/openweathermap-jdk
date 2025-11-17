package com.github.brokkko.openweathermap.jdk.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class WeatherTest {

    @Test
    void settersAndGetters_shouldStoreValues() {
        Weather weather = new Weather();

        LocalDateTime now = LocalDateTime.now();
        weather.setCalculationTime(now);

        WeatherState state = new WeatherState(800, "Clear", "Clear sky");
        weather.setWeatherState(state);

        Temperature temp = Temperature.withValue(20.5, "C");
        weather.setTemperature(temp);

        AtmosphericPressure pressure = AtmosphericPressure.withValue(1013);
        weather.setAtmosphericPressure(pressure);

        Humidity humidity = Humidity.withValue(55);
        weather.setHumidity(humidity);

        Wind wind = Wind.withValue(5, "m/s");
        weather.setWind(wind);

        Rain rain = Rain.withOneHourLevelValue(2.0);
        weather.setRain(rain);

        Snow snow = Snow.withOneHourLevelValue(1.0);
        weather.setSnow(snow);

        Clouds clouds = Clouds.withValue((byte) 40);
        weather.setClouds(clouds);

        Location loc = Location.withValues(0, "Berlin");
        weather.setLocation(loc);

        assertEquals(now, weather.getCalculationTime());
        assertEquals(state, weather.getWeatherState());
        assertEquals(temp, weather.getTemperature());
        assertEquals(pressure, weather.getAtmosphericPressure());
        assertEquals(humidity, weather.getHumidity());
        assertEquals(wind, weather.getWind());
        assertEquals(rain, weather.getRain());
        assertEquals(snow, weather.getSnow());
        assertEquals(clouds, weather.getClouds());
        assertEquals(loc, weather.getLocation());
    }

    @Test
    void equalsAndHashCode_shouldMatch_forIdenticalObjects() {
        Weather w1 = new Weather();
        Weather w2 = new Weather();

        LocalDateTime time = LocalDateTime.now();
        w1.setCalculationTime(time);
        w2.setCalculationTime(time);

        w1.setWeatherState(new WeatherState(800, "Clear", "Clear sky"));
        w2.setWeatherState(new WeatherState(800, "Clear", "Clear sky"));

        w1.setTemperature(Temperature.withValue(20, "C"));
        w2.setTemperature(Temperature.withValue(20, "C"));

        w1.setAtmosphericPressure(AtmosphericPressure.withValue(1013));
        w2.setAtmosphericPressure(AtmosphericPressure.withValue(1013));

        w1.setHumidity(Humidity.withValue(50));
        w2.setHumidity(Humidity.withValue(50));

        w1.setWind(Wind.withValue(3, "m/s"));
        w2.setWind(Wind.withValue(3, "m/s"));

        w1.setRain(Rain.withOneHourLevelValue(1));
        w2.setRain(Rain.withOneHourLevelValue(1));

        w1.setSnow(Snow.withOneHourLevelValue(0.5));
        w2.setSnow(Snow.withOneHourLevelValue(0.5));

        w1.setClouds(Clouds.withValue((byte) 20));
        w2.setClouds(Clouds.withValue((byte) 20));

        Location loc = Location.withValues(2, "Paris");
        w1.setLocation(loc);
        w2.setLocation(loc);

        assertEquals(w1, w2);
        assertEquals(w1.hashCode(), w2.hashCode());
    }

    @Test
    void equals_shouldReturnFalse_forDifferentObjects() {
        Weather w1 = new Weather();
        Weather w2 = new Weather();

        w1.setTemperature(Temperature.withValue(20, "C"));
        w2.setTemperature(Temperature.withValue(15, "C"));

        assertNotEquals(w1, w2);
    }

    @Test
    void toString_shouldIncludeAvailableFields() {
        Weather w = new Weather();

        w.setLocation(Location.withValues(3, "Madrid"));
        w.setWeatherState(new WeatherState(800, "Clear", "Sunny"));
        w.setTemperature(Temperature.withValue(25, "C"));
        w.setAtmosphericPressure(AtmosphericPressure.withValue(1015));
        w.setClouds(Clouds.withValue((byte) 10));
        w.setRain(Rain.withOneHourLevelValue(0.5));

        String result = w.toString();

        assertTrue(result.contains("Madrid"));
        assertTrue(result.contains("Sunny"));
        assertTrue(result.contains("25"));
        assertTrue(result.contains("1015"));
        assertTrue(result.contains("10"));
        assertTrue(result.contains("Rain: 0.5 mm"));
    }
}
