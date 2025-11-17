package com.github.brokkko.openweathermap.jdk.clients;

import com.github.brokkko.openweathermap.jdk.enums.*;
import com.github.brokkko.openweathermap.jdk.http.WeatherHttpExecutor;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.request.requsters.WeatherLocationRequester;
import com.github.brokkko.openweathermap.jdk.services.WeatherCacheService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OpenWeatherMapClientTest {

    @Test
    void constructor_onDemandMode_noPollingExecutor() {
        WeatherCacheService cache = mock(WeatherCacheService.class);
        WeatherHttpExecutor executor = mock(WeatherHttpExecutor.class);
        WeatherLogger logger = mock(WeatherLogger.class);

        OpenWeatherMapClient client = new OpenWeatherMapClient(
                "key",
                SdkMode.ON_DEMAND,
                "http://test",
                cache,
                executor,
                logger,
                5
        );

        assertEquals(SdkMode.ON_DEMAND, client.getSdkMode());
        assertEquals("key", client.getApiKey());
        assertEquals("http://test", client.getBaseUrl());
    }

    @Test
    void constructor_pollingMode_startsScheduler() throws InterruptedException {
        WeatherCacheService cache = mock(WeatherCacheService.class);
        WeatherHttpExecutor executor = mock(WeatherHttpExecutor.class);
        WeatherLogger logger = mock(WeatherLogger.class);

        OpenWeatherMapClient client = new OpenWeatherMapClient(
                "key",
                SdkMode.POLLING_MODE,
                "http://test",
                cache,
                executor,
                logger,
                1
        );

        Thread.sleep(50); // allow scheduler to run once
        verify(logger, atLeastOnce()).info(anyString());
    }

    @Test
    void query_returnsWeatherLocationRequester() {
        WeatherCacheService cache = mock(WeatherCacheService.class);
        WeatherHttpExecutor executor = mock(WeatherHttpExecutor.class);
        WeatherLogger logger = mock(WeatherLogger.class);

        OpenWeatherMapClient client = new OpenWeatherMapClient(
                "key",
                SdkMode.ON_DEMAND,
                "http://test",
                cache,
                executor,
                logger,
                5
        );

        WeatherLocationRequester requester = client.query();
        assertNotNull(requester);
    }

    @Test
    void destroy_clearsCacheAndShutsDown() {
        WeatherCacheService cache = mock(WeatherCacheService.class);
        WeatherHttpExecutor executor = mock(WeatherHttpExecutor.class);
        WeatherLogger logger = mock(WeatherLogger.class);

        OpenWeatherMapClient client = new OpenWeatherMapClient(
                "key",
                SdkMode.ON_DEMAND,
                "http://test",
                cache,
                executor,
                logger,
                5
        );

        client.destroy();

        verify(logger, atLeastOnce()).info(contains("Destroying"));
        verify(cache).clear();
    }
}
