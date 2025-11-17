package com.github.brokkko.openweathermap.jdk.request.terminaters;

import com.github.brokkko.openweathermap.jdk.clients.OpenWeatherMapClient;
import com.github.brokkko.openweathermap.jdk.enums.SdkMode;
import com.github.brokkko.openweathermap.jdk.http.WeatherHttpExecutor;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.request.RequestSettings;
import com.github.brokkko.openweathermap.jdk.services.WeatherCacheService;
import com.github.brokkko.openweathermap.jdk.services.impl.WeatherCacheServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WeatherRequestTerminatorTest {

    @Test
    void testAsJsonReturnsCachedValueOnDemand() {
        // mocks
        OpenWeatherMapClient client = mock(OpenWeatherMapClient.class);
        WeatherLogger logger = mock(WeatherLogger.class);
        WeatherCacheService cache = mock(WeatherCacheServiceImpl.class);
        WeatherHttpExecutor http = mock(WeatherHttpExecutor.class);

        when(client.getSdkMode()).thenReturn(SdkMode.ON_DEMAND);
        when(client.getCacheService()).thenReturn(cache);
        when(client.getHttpExecutor()).thenReturn(http);

        RequestSettings rs = new RequestSettings("key");
        rs.putRequestParameter("q", "London");

        when(cache.get(any())).thenReturn(Optional.of("cached-json"));

        WeatherRequestTerminator t = new WeatherRequestTerminator(client, logger, rs);

        assertEquals("cached-json", t.asJSON());
    }

    @Test
    void testAsJsonExecutesHttpWhenCacheMiss() {
        OpenWeatherMapClient client = mock(OpenWeatherMapClient.class);
        WeatherLogger logger = mock(WeatherLogger.class);
        WeatherCacheService cache = mock(WeatherCacheServiceImpl.class);
        WeatherHttpExecutor http = mock(WeatherHttpExecutor .class);

        when(client.getSdkMode()).thenReturn(SdkMode.ON_DEMAND);
        when(client.getCacheService()).thenReturn(cache);
        when(client.getHttpExecutor()).thenReturn(http);

        RequestSettings rs = new RequestSettings("key");

        when(cache.get(any())).thenReturn(Optional.empty());
        when(http.execute(any())).thenReturn("live-json");

        WeatherRequestTerminator t = new WeatherRequestTerminator(client, logger, rs);

        assertEquals("live-json", t.asJSON());
    }
}
