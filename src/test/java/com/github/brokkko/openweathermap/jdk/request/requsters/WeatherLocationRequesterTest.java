package com.github.brokkko.openweathermap.jdk.request.requsters;

import com.github.brokkko.openweathermap.jdk.clients.OpenWeatherMapClient;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.models.Coordinate;
import com.github.brokkko.openweathermap.jdk.request.RequestSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class WeatherLocationRequesterTest {

    @Test
    void testByCityNameSetsParameter() {
        RequestSettings rs = new RequestSettings("key");
        WeatherLocationRequester wlr = new WeatherLocationRequester(
                mock(OpenWeatherMapClient.class),
                mock(WeatherLogger.class),
                rs
        );

        wlr.byCityName("Paris");

        assertEquals("Paris", rs.getRequestParameters().get("q"));
    }

    @Test
    void testByCoordinatesSetsLatLon() {
        RequestSettings rs = new RequestSettings("key");
        WeatherLocationRequester wlr = new WeatherLocationRequester(
                mock(OpenWeatherMapClient.class),
                mock(WeatherLogger.class),
                rs
        );

        wlr.byCoordinates(Coordinate.of(10.5, -1.2));

        assertEquals("10.5", rs.getRequestParameters().get("lat"));
        assertEquals("-1.2", rs.getRequestParameters().get("lon"));
    }

    @Test
    void testConstructorAppendsWeather() {
        RequestSettings rs = new RequestSettings("key");
        new WeatherLocationRequester(
                mock(OpenWeatherMapClient.class),
                mock(WeatherLogger.class),
                rs
        );

        assertTrue(rs.getUrlBuilder().toString().contains("/weather"));
    }
}
