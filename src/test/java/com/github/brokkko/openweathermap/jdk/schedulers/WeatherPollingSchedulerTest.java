package com.github.brokkko.openweathermap.jdk.schedulers;

import com.github.brokkko.openweathermap.jdk.http.WeatherHttpExecutor;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.request.RequestSettings;
import com.github.brokkko.openweathermap.jdk.services.WeatherCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.*;

class WeatherPollingSchedulerTest {

    private WeatherCacheService cache;
    private WeatherHttpExecutor http;
    private WeatherLogger logger;
    private WeatherPollingScheduler scheduler;

    @BeforeEach
    void setUp() {
        cache = mock(WeatherCacheService.class);
        http = mock(WeatherHttpExecutor.class);
        logger = mock(WeatherLogger.class);
        scheduler = new WeatherPollingScheduler(cache, http, logger);
    }

    @Test
    void testPollOnce_refreshesAllEntries() {
        RequestSettings rs1 = mock(RequestSettings.class);
        RequestSettings rs2 = mock(RequestSettings.class);

        when(rs1.copy()).thenReturn(rs1);
        when(rs2.copy()).thenReturn(rs2);

        when(cache.getAllRequestSettingsSnapshot())
                .thenReturn(Map.of("k1", rs1, "k2", rs2));

        when(http.execute(rs1)).thenReturn("{json1}");
        when(http.execute(rs2)).thenReturn("{json2}");

        scheduler.pollOnce();

        verify(http).execute(rs1);
        verify(http).execute(rs2);

        verify(cache).put("k1", "{json1}", rs1);
        verify(cache).put("k2", "{json2}", rs2);

        verify(logger).debug(contains("refreshing"));
        verify(logger).debug(contains("finished"));
    }

    @Test
    void testPollOnce_httpFailure_logsErrorButContinues() {
        RequestSettings rs = mock(RequestSettings.class);
        when(rs.copy()).thenReturn(rs);

        when(cache.getAllRequestSettingsSnapshot()).thenReturn(Map.of("k1", rs));

        when(http.execute(rs)).thenThrow(new RuntimeException("boom"));

        scheduler.pollOnce();

        verify(logger).error(contains("k1"), any());
        verify(cache, never()).put(any(), any(), any());
    }
}
