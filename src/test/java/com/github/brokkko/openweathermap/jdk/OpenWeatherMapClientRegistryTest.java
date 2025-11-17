package com.github.brokkko.openweathermap.jdk;

import com.github.brokkko.openweathermap.jdk.clients.ClientConfig;
import com.github.brokkko.openweathermap.jdk.clients.OpenWeatherMapClient;
import com.github.brokkko.openweathermap.jdk.enums.LogLevel;
import com.github.brokkko.openweathermap.jdk.enums.LoggerType;
import com.github.brokkko.openweathermap.jdk.enums.SdkMode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OpenWeatherMapClientRegistryTest {

    private static final String API_KEY = "KEY123";
    private ClientConfig config;

    @BeforeEach
    void setup() {
        config = mock(ClientConfig.class);
        when(config.getApiKey()).thenReturn("KEY123");
        when(config.getMode()).thenReturn(SdkMode.ON_DEMAND);
        when(config.getRetryPolicyType()).thenReturn(null);
        when(config.getLogLevel()).thenReturn(LogLevel.INFO);
        when(config.getLoggerType()).thenReturn(LoggerType.DEFAULT);
        when(config.getHttpTimeoutSeconds()).thenReturn(5);
    }

        @AfterEach
    void cleanup() {
        OpenWeatherMapClientRegistry.remove(API_KEY);
    }

    @Test
    void testGetOrCreate_createsAndCachesClient() {
        OpenWeatherMapClient client1 = OpenWeatherMapClientRegistry.getOrCreate(config);
        assertNotNull(client1, "Client should not be null");

        OpenWeatherMapClient client2 = OpenWeatherMapClientRegistry.getOrCreate(config);
        assertSame(client1, client2, "Should return the same cached instance");
    }

    @Test
    void testRemove_deletesClient() {
        OpenWeatherMapClient client = OpenWeatherMapClientRegistry.getOrCreate(config);
        assertNotNull(client);

        OpenWeatherMapClientRegistry.remove(API_KEY);

        OpenWeatherMapClient newClient = OpenWeatherMapClientRegistry.getOrCreate(config);
        assertNotSame(client, newClient, "After removal, a new client instance should be created");
    }
}
