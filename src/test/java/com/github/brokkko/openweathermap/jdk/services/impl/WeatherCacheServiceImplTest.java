package com.github.brokkko.openweathermap.jdk.services.impl;

import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.request.RequestSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class WeatherCacheServiceImplTest {

    private WeatherLogger logger;
    private WeatherCacheServiceImpl cache;

    @BeforeEach
    void setUp() {
        logger = mock(WeatherLogger.class);
        cache = new WeatherCacheServiceImpl(2, 1000, logger);
    }

    @Test
    void testPutAndGet() {
        RequestSettings rs = new RequestSettings("k");
        cache.put("a", "{\"t\":1}", rs);

        assertEquals(Optional.of("{\"t\":1}"), cache.get("a"));
    }

    @Test
    void testExpiredEntryIsRemoved() throws Exception {
        cache = new WeatherCacheServiceImpl(10, 1, logger);

        RequestSettings rs = new RequestSettings("k");
        cache.put("a", "json", rs);

        Thread.sleep(5);

        assertEquals(Optional.empty(), cache.get("a"));
    }

    @Test
    void testEvictionOldestEntry() {
        RequestSettings r1 = new RequestSettings("k");
        RequestSettings r2 = new RequestSettings("k");
        RequestSettings r3 = new RequestSettings("k");

        cache.put("a", "1", r1);
        cache.put("b", "2", r2);

        cache.put("c", "3", r3); // capacity exceeded → evict oldest ("a")

        assertEquals(Optional.empty(), cache.get("a"));
        assertEquals(Optional.of("2"), cache.get("b"));
        assertEquals(Optional.of("3"), cache.get("c"));
    }

    @Test
    void testRemove() {
        RequestSettings rs = new RequestSettings("k");
        cache.put("x", "json", rs);

        cache.remove("x");

        assertEquals(Optional.empty(), cache.get("x"));
    }

    @Test
    void testSnapshotSettings() {
        RequestSettings rs = new RequestSettings("key");
        cache.put("k", "json", rs);

        Map<String, RequestSettings> snapshot = cache.getAllRequestSettingsSnapshot();

        assertEquals(1, snapshot.size());
        assertSame(rs, snapshot.get("k")); // shallow copy is correct
    }
}
