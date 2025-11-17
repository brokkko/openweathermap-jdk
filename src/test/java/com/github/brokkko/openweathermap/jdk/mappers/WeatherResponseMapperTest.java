package com.github.brokkko.openweathermap.jdk.mappers;

import com.github.brokkko.openweathermap.jdk.enums.UnitSystem;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.models.Weather;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class WeatherResponseMapperTest {

    @Test
    void testFullJsonMapping() {
        WeatherLogger logger = mock(WeatherLogger.class);
        WeatherResponseMapper mapper = new WeatherResponseMapper(UnitSystem.METRIC, logger);

        String json = """
                {
                  "weather": [{
                    "id": 800,
                    "main": "Clear",
                    "description": "clear sky",
                    "icon": "01d"
                  }],
                  "main": {
                    "temp": 23.5,
                    "feels_like": 21.0,
                    "temp_min": 20.0,
                    "temp_max": 25.0,
                    "pressure": 1012,
                    "sea_level": 1015,
                    "grnd_level": 1009,
                    "humidity": 40
                  },
                  "wind": {
                    "speed": 3.5,
                    "deg": 150,
                    "gust": 7.0
                  },
                  "rain": { "1h": 0.5 },
                  "snow": { "3h": 1.2 },
                  "clouds": { "all": 10 },
                  "id": 12345,
                  "name": "London",
                  "timezone": 3600,
                  "sys": {
                    "country": "GB",
                    "sunrise": 1700000000,
                    "sunset": 1700040000
                  },
                  "coord": { "lat": 51.51, "lon": -0.13 },
                  "dt": 1700020000
                }
                """;

        Weather w = mapper.mapJsonToWeather(json);

        assertEquals(12345, w.getLocation().getId());
        assertEquals("London", w.getLocation().getName());
        assertEquals("GB", w.getLocation().getCountryCode());

        assertEquals(23.5, w.getTemperature().getValue());
        assertEquals(1012, w.getAtmosphericPressure().getValue());
        assertEquals(40, w.getHumidity().getValue());

        assertEquals(3.5, w.getWind().getSpeed());
        assertEquals(0.5, w.getRain().getOneHourLevel());
        assertEquals(1.2, w.getSnow().getThreeHourLevel());

        assertEquals(10, w.getClouds().getValue());
    }

}
