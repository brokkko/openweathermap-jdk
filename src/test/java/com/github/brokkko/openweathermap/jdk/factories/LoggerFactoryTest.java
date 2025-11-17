package com.github.brokkko.openweathermap.jdk.factories;

import com.github.brokkko.openweathermap.jdk.enums.LogLevel;
import com.github.brokkko.openweathermap.jdk.enums.LoggerType;
import com.github.brokkko.openweathermap.jdk.logging.impl.DefaultWeatherLogger;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoggerFactoryTest {

    @Test
    void testConstructor() {
        LoggerFactory factory = new LoggerFactory();
        assertNotNull(factory);
    }

    @Test
    void testCreateDefaultLogger() {
        WeatherLogger logger = LoggerFactory.create(
                LoggerType.DEFAULT,
                LogLevel.INFO,
                LoggerFactoryTest.class
        );

        assertNotNull(logger);
        assertInstanceOf(DefaultWeatherLogger.class, logger);
    }

    @Test
    void testCreateLoggerRequiresNonNullParams() {
        assertThrows(NullPointerException.class,
                () -> LoggerFactory.create(null, LogLevel.INFO, LoggerFactoryTest.class)
        );

        assertThrows(NullPointerException.class,
                () -> LoggerFactory.create(LoggerType.DEFAULT, null, LoggerFactoryTest.class)
        );

        assertThrows(NullPointerException.class,
                () -> LoggerFactory.create(LoggerType.DEFAULT, LogLevel.INFO, null)
        );
    }
}
