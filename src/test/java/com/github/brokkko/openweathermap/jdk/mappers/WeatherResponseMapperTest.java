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

}
