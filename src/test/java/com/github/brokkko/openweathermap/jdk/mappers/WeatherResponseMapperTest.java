package com.github.brokkko.openweathermap.jdk.mappers;

import com.github.brokkko.openweathermap.jdk.clients.OpenWeatherMapClient;
import com.github.brokkko.openweathermap.jdk.enums.SdkMode;
import com.github.brokkko.openweathermap.jdk.enums.UnitSystem;
import com.github.brokkko.openweathermap.jdk.exceptions.WeatherSerializationException;
import com.github.brokkko.openweathermap.jdk.http.WeatherHttpExecutor;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.models.Weather;
import com.github.brokkko.openweathermap.jdk.request.RequestSettings;
import com.github.brokkko.openweathermap.jdk.request.terminaters.WeatherRequestTerminator;
import com.github.brokkko.openweathermap.jdk.services.WeatherCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherResponseMapperTest {

    private OpenWeatherMapClient client;
    private WeatherLogger logger;
    private WeatherCacheService cache;
    private WeatherHttpExecutor httpExecutor;
    private RequestSettings rs;

    @BeforeEach
    void setup() {
        client = mock(OpenWeatherMapClient.class);
        logger = mock(WeatherLogger.class);
        cache = mock(WeatherCacheService.class);
        httpExecutor = mock(WeatherHttpExecutor.class);

        when(client.getCacheService()).thenReturn(cache);
        when(client.getHttpExecutor()).thenReturn(httpExecutor);

        rs = mock(RequestSettings.class);
        when(rs.cacheKey()).thenReturn("moscow");
        when(rs.getUnitSystem()).thenReturn(UnitSystem.METRIC);
        when(rs.copy()).thenReturn(rs);
    }

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

    @Test
    void testHandleOnPolling_cacheMiss() {
        when(client.getSdkMode()).thenReturn(SdkMode.POLLING_MODE);
        when(cache.get("Moscow")).thenReturn(Optional.empty());
        when(httpExecutor.execute(rs)).thenReturn("{json-response}");

        WeatherRequestTerminator terminator = new WeatherRequestTerminator(client, logger, rs);

        String json = terminator.asJSON();

        assertEquals("{json-response}", json);
        verify(httpExecutor).execute(rs);
    }

    @Test
    void testHandleOnPolling_cacheHit() {
        when(client.getSdkMode()).thenReturn(SdkMode.POLLING_MODE);
        when(cache.get("moscow")).thenReturn(Optional.of("cached-json"));

        WeatherRequestTerminator terminator = new WeatherRequestTerminator(client, logger, rs);

        String json = terminator.asJSON();

        assertEquals("cached-json", json);
        verify(httpExecutor, never()).execute(any());
    }

    @Test
    void testAsJava_success() {
        when(client.getSdkMode()).thenReturn(SdkMode.ON_DEMAND);
        when(cache.get("moscow")).thenReturn(Optional.of("{json-ok}"));

        Weather mockWeather = mock(Weather.class);

        try (MockedConstruction<WeatherResponseMapper> mapperMock =
                     Mockito.mockConstruction(
                             WeatherResponseMapper.class,
                             (mock, context) -> when(mock.mapJsonToWeather("{json-ok}"))
                                     .thenReturn(mockWeather)
                     )) {

            WeatherRequestTerminator terminator = new WeatherRequestTerminator(client, logger, rs);

            Weather w = terminator.asJava();

            assertSame(mockWeather, w);
        }
    }

    @Test
    void testAsJava_mapperThrows() {
        when(client.getSdkMode()).thenReturn(SdkMode.ON_DEMAND);
        when(cache.get("moscow")).thenReturn(Optional.of("{bad-json}"));

        try (MockedConstruction<WeatherResponseMapper> mapperMock =
                     Mockito.mockConstruction(
                             WeatherResponseMapper.class,
                             (mock, context) -> when(mock.mapJsonToWeather("{bad-json}"))
                                     .thenThrow(new RuntimeException("parse failed"))
                     )) {

            WeatherRequestTerminator terminator = new WeatherRequestTerminator(client, logger, rs);

            assertThrows(WeatherSerializationException.class, terminator::asJava);
        }
    }

    @Test
    void testMapperWithNullUnitSystem() {
        WeatherLogger logger = mock(WeatherLogger.class);
        WeatherResponseMapper mapper = new WeatherResponseMapper(null, logger);

        String json = """
                {
                  "weather": [{"id": 800, "main": "Clear", "description": "clear sky", "icon": "01d"}],
                  "main": {"temp": 273.15, "pressure": 1013, "humidity": 50},
                  "wind": {"speed": 5.0},
                  "id": 1,
                  "name": "Test",
                  "clouds": {"all": 0},
                  "dt": 1700000000
                }
                """;

        Weather w = mapper.mapJsonToWeather(json);
        assertNotNull(w);
    }

    @Test
    void testMapperWithImperialUnits() {
        WeatherLogger logger = mock(WeatherLogger.class);
        WeatherResponseMapper mapper = new WeatherResponseMapper(UnitSystem.IMPERIAL, logger);

        String json = """
                {
                  "weather": [{"id": 800, "main": "Clear", "description": "clear sky", "icon": "01d"}],
                  "main": {"temp": 75.0, "pressure": 1013, "humidity": 50},
                  "wind": {"speed": 10.0},
                  "id": 1,
                  "name": "Test",
                  "clouds": {"all": 0},
                  "dt": 1700000000
                }
                """;

        Weather w = mapper.mapJsonToWeather(json);
        assertNotNull(w);
        assertEquals("°F", w.getTemperature().getUnit());
        assertEquals("miles/hour", w.getWind().getUnit());
    }

    @Test
    void testMapperWithMissingOptionalFields() {
        WeatherLogger logger = mock(WeatherLogger.class);
        WeatherResponseMapper mapper = new WeatherResponseMapper(UnitSystem.METRIC, logger);

        String json = """
                {
                  "weather": [{"id": 800, "main": "Clear", "description": "clear sky", "icon": "01d"}],
                  "main": {"temp": 20.0, "pressure": 1013, "humidity": 50},
                  "wind": {"speed": 5.0},
                  "id": 1,
                  "name": "Test",
                  "clouds": {"all": 0},
                  "dt": 1700000000
                }
                """;

        Weather w = mapper.mapJsonToWeather(json);
        assertNotNull(w);
        assertNull(w.getRain());
        assertNull(w.getSnow());
        assertNull(w.getTemperature().getFeelsLike());
    }

    @Test
    void testMapperWithRainThreeHourOnly() {
        WeatherLogger logger = mock(WeatherLogger.class);
        WeatherResponseMapper mapper = new WeatherResponseMapper(UnitSystem.METRIC, logger);

        String json = """
                {
                  "weather": [{"id": 500, "main": "Rain", "description": "light rain", "icon": "10d"}],
                  "main": {"temp": 18.0, "pressure": 1010, "humidity": 80},
                  "wind": {"speed": 3.0},
                  "rain": {"3h": 2.5},
                  "id": 1,
                  "name": "Test",
                  "clouds": {"all": 75},
                  "dt": 1700000000
                }
                """;

        Weather w = mapper.mapJsonToWeather(json);
        assertNotNull(w.getRain());
        assertNull(w.getRain().getOneHourLevel());
        assertEquals(2.5, w.getRain().getThreeHourLevel());
    }

    @Test
    void testMapperWithSnowOneHourOnly() {
        WeatherLogger logger = mock(WeatherLogger.class);
        WeatherResponseMapper mapper = new WeatherResponseMapper(UnitSystem.METRIC, logger);

        String json = """
                {
                  "weather": [{"id": 600, "main": "Snow", "description": "light snow", "icon": "13d"}],
                  "main": {"temp": -2.0, "pressure": 1015, "humidity": 90},
                  "wind": {"speed": 2.0},
                  "snow": {"1h": 1.0},
                  "id": 1,
                  "name": "Test",
                  "clouds": {"all": 100},
                  "dt": 1700000000
                }
                """;

        Weather w = mapper.mapJsonToWeather(json);
        assertNotNull(w.getSnow());
        assertEquals(1.0, w.getSnow().getOneHourLevel());
        assertNull(w.getSnow().getThreeHourLevel());
    }

    @Test
    void testMapperWithInvalidJson() {
        WeatherLogger logger = mock(WeatherLogger.class);
        WeatherResponseMapper mapper = new WeatherResponseMapper(UnitSystem.METRIC, logger);

        String invalidJson = "{ invalid json }";

        assertThrows(WeatherSerializationException.class, () -> mapper.mapJsonToWeather(invalidJson));
        verify(logger).error(anyString(), any(Exception.class));
    }

    @Test
    void testMapperWithNullClouds() {
        WeatherLogger logger = mock(WeatherLogger.class);
        WeatherResponseMapper mapper = new WeatherResponseMapper(UnitSystem.METRIC, logger);

        String json = """
                {
                  "weather": [{"id": 800, "main": "Clear", "description": "clear sky", "icon": "01d"}],
                  "main": {"temp": 20.0, "pressure": 1013, "humidity": 50},
                  "wind": {"speed": 5.0},
                  "id": 1,
                  "name": "Test",
                  "clouds": {},
                  "dt": 1700000000
                }
                """;

        Weather w = mapper.mapJsonToWeather(json);
        assertNull(w.getClouds());
    }

    @Test
    void testMapperWithLocationDetails() {
        WeatherLogger logger = mock(WeatherLogger.class);
        WeatherResponseMapper mapper = new WeatherResponseMapper(UnitSystem.METRIC, logger);

        String json = """
                {
                  "weather": [{"id": 800, "main": "Clear", "description": "clear sky", "icon": "01d"}],
                  "main": {"temp": 20.0, "pressure": 1013, "humidity": 50},
                  "wind": {"speed": 5.0},
                  "id": 12345,
                  "name": "Moscow",
                  "timezone": 10800,
                  "sys": {"country": "RU", "sunrise": 1700000000, "sunset": 1700040000},
                  "coord": {"lat": 55.75, "lon": 37.62},
                  "clouds": {"all": 0},
                  "dt": 1700020000
                }
                """;

        Weather w = mapper.mapJsonToWeather(json);
        assertNotNull(w.getLocation());
        assertEquals(12345, w.getLocation().getId());
        assertEquals("Moscow", w.getLocation().getName());
        assertEquals("RU", w.getLocation().getCountryCode());
        assertNotNull(w.getLocation().getSunriseTime());
        assertNotNull(w.getLocation().getSunsetTime());
        assertNotNull(w.getLocation().getZoneOffset());
        assertNotNull(w.getLocation().getCoordinate());
        assertEquals(55.75, w.getLocation().getCoordinate().getLatitude());
        assertEquals(37.62, w.getLocation().getCoordinate().getLongitude());
    }

}
