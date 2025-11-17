package com.github.brokkko.openweathermap.jdk.request.customizers;

import com.github.brokkko.openweathermap.jdk.clients.OpenWeatherMapClient;
import com.github.brokkko.openweathermap.jdk.enums.Language;
import com.github.brokkko.openweathermap.jdk.enums.UnitSystem;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.request.RequestSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class WeatherResultCustomizerTest {

    @Test
    void testLanguageSetsRequestParameter() {
        RequestSettings rs = new RequestSettings("k");
        WeatherResultCustomizer c = new WeatherResultCustomizer(mock(OpenWeatherMapClient.class), mock(WeatherLogger.class), rs);

        c.language(Language.GERMAN);

        assertEquals("de", rs.getRequestParameters().get("lang"));
    }

    @Test
    void testUnitSystemSetsRequestParameter() {
        RequestSettings rs = new RequestSettings("k");
        WeatherResultCustomizer c = new WeatherResultCustomizer(mock(OpenWeatherMapClient.class), mock(WeatherLogger.class), rs);

        c.unitSystem(UnitSystem.METRIC);

        assertEquals("metric", rs.getRequestParameters().get("units"));
    }

    @Test
    void testRetrieveReturnsTerminator() {
        WeatherResultCustomizer c = new WeatherResultCustomizer(
                mock(OpenWeatherMapClient.class),
                mock(WeatherLogger.class),
                new RequestSettings("key")
        );

        assertNotNull(c.retrieve());
    }
}
