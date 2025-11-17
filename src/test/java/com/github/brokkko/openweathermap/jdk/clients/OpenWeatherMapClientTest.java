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

    @Test
    void builder_shouldCreateClientWithMinimalConfig() {
        OpenWeatherMapClient client = OpenWeatherMapClient.builder()
                .apiKey("test-key")
                .logLevel(LogLevel.INFO)
                .logger(LoggerType.DEFAULT)
                .build();

        assertNotNull(client);
        assertEquals("test-key", client.getApiKey());
        assertEquals(SdkMode.ON_DEMAND, client.getSdkMode());
    }

    @Test
    void builder_shouldCreateClientWithPollingMode() {
        OpenWeatherMapClient client = OpenWeatherMapClient.builder()
                .apiKey("test-key")
                .mode(SdkMode.POLLING_MODE)
                .pollingIntervalMinutes(5)
                .logLevel(LogLevel.INFO)
                .logger(LoggerType.DEFAULT)
                .build();

        assertNotNull(client);
        assertEquals(SdkMode.POLLING_MODE, client.getSdkMode());
    }

    @Test
    void builder_shouldSetRetryPolicy() {
        OpenWeatherMapClient client = OpenWeatherMapClient.builder()
                .apiKey("test-key")
                .retryPolicy(RetryPolicyType.EXPONENTIAL_BACKOFF)
                .logLevel(LogLevel.INFO)
                .logger(LoggerType.DEFAULT)
                .build();

        assertNotNull(client);
        assertNotNull(client.getHttpExecutor());
    }

    @Test
    void builder_shouldSetHttpTimeout() {
        OpenWeatherMapClient client = OpenWeatherMapClient.builder()
                .apiKey("test-key")
                .httpTimeoutSeconds(30)
                .logLevel(LogLevel.INFO)
                .logger(LoggerType.DEFAULT)
                .build();

        assertNotNull(client);
    }

    @Test
    void builder_shouldSetLogLevel() {
        OpenWeatherMapClient client = OpenWeatherMapClient.builder()
                .apiKey("test-key")
                .logLevel(LogLevel.DEBUG)
                .logger(LoggerType.DEFAULT)
                .build();

        assertNotNull(client);
    }

    @Test
    void getCacheService_shouldReturnCacheService() {
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

        assertSame(cache, client.getCacheService());
    }

    @Test
    void getHttpExecutor_shouldReturnHttpExecutor() {
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

        assertSame(executor, client.getHttpExecutor());
    }

    @Test
    void getBaseUrl_shouldReturnBaseUrl() {
        WeatherCacheService cache = mock(WeatherCacheService.class);
        WeatherHttpExecutor executor = mock(WeatherHttpExecutor.class);
        WeatherLogger logger = mock(WeatherLogger.class);

        OpenWeatherMapClient client = new OpenWeatherMapClient(
                "key",
                SdkMode.ON_DEMAND,
                "http://custom-url",
                cache,
                executor,
                logger,
                5
        );

        assertEquals("http://custom-url", client.getBaseUrl());
    }
}
