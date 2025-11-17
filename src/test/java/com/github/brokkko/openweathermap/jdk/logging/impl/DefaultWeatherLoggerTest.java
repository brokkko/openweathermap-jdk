package com.github.brokkko.openweathermap.jdk.logging.impl;

import com.github.brokkko.openweathermap.jdk.enums.LogLevel;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultWeatherLoggerTest {

    @Test
    void testDebugLogsFineLevel() {
        Handler handlerMock = mock(Handler.class);

        DefaultWeatherLogger logger = new DefaultWeatherLogger(
                DefaultWeatherLoggerTest.class,
                LogLevel.DEBUG
        );

        java.util.logging.Logger jdkLogger =
                java.util.logging.Logger.getLogger(DefaultWeatherLoggerTest.class.getName());

        for (Handler h : jdkLogger.getHandlers()) {
            jdkLogger.removeHandler(h);
        }
        jdkLogger.addHandler(handlerMock);

        when(handlerMock.getLevel()).thenReturn(Level.ALL);

        String msg = "debug message";
        logger.debug(msg);

        ArgumentCaptor<LogRecord> captor = ArgumentCaptor.forClass(LogRecord.class);
        verify(handlerMock, timeout(500)).publish(captor.capture());
        LogRecord record = captor.getValue();

        assertEquals(Level.FINE, record.getLevel());
        assertTrue(record.getMessage().contains(msg));
    }

    @Test
    void testInfoLogsInfoLevel() {
        Handler handlerMock = mock(Handler.class);

        DefaultWeatherLogger logger = new DefaultWeatherLogger(
                DefaultWeatherLoggerTest.class,
                LogLevel.INFO
        );

        java.util.logging.Logger jdkLogger =
                java.util.logging.Logger.getLogger(DefaultWeatherLoggerTest.class.getName());

        for (Handler h : jdkLogger.getHandlers()) {
            jdkLogger.removeHandler(h);
        }
        jdkLogger.addHandler(handlerMock);

        when(handlerMock.getLevel()).thenReturn(Level.ALL);

        String msg = "info message";
        logger.info(msg);

        ArgumentCaptor<LogRecord> captor = ArgumentCaptor.forClass(LogRecord.class);
        verify(handlerMock, timeout(500)).publish(captor.capture());
        LogRecord record = captor.getValue();

        assertEquals(Level.INFO, record.getLevel());
        assertTrue(record.getMessage().contains(msg));
    }

    @Test
    void testWarnLogsWarningLevel() {
        Handler handlerMock = mock(Handler.class);

        DefaultWeatherLogger logger = new DefaultWeatherLogger(
                DefaultWeatherLoggerTest.class,
                LogLevel.WARN
        );

        java.util.logging.Logger jdkLogger =
                java.util.logging.Logger.getLogger(DefaultWeatherLoggerTest.class.getName());

        for (Handler h : jdkLogger.getHandlers()) {
            jdkLogger.removeHandler(h);
        }
        jdkLogger.addHandler(handlerMock);

        when(handlerMock.getLevel()).thenReturn(Level.ALL);

        String msg = "warn message";
        logger.warn(msg);

        ArgumentCaptor<LogRecord> captor = ArgumentCaptor.forClass(LogRecord.class);
        verify(handlerMock, timeout(500)).publish(captor.capture());
        LogRecord record = captor.getValue();

        assertEquals(Level.WARNING, record.getLevel());
        assertTrue(record.getMessage().contains(msg));
    }

    @Test
    void testErrorLogsSevereLevel() {
        Handler handlerMock = mock(Handler.class);

        DefaultWeatherLogger logger = new DefaultWeatherLogger(
                DefaultWeatherLoggerTest.class,
                LogLevel.ERROR
        );

        java.util.logging.Logger jdkLogger =
                java.util.logging.Logger.getLogger(DefaultWeatherLoggerTest.class.getName());

        for (Handler h : jdkLogger.getHandlers()) {
            jdkLogger.removeHandler(h);
        }
        jdkLogger.addHandler(handlerMock);

        when(handlerMock.getLevel()).thenReturn(Level.ALL);

        String msg = "error message";
        Throwable ex = new RuntimeException("boom");

        logger.error(msg, ex);

        ArgumentCaptor<LogRecord> captor = ArgumentCaptor.forClass(LogRecord.class);
        verify(handlerMock, timeout(500)).publish(captor.capture());
        LogRecord record = captor.getValue();

        assertEquals(Level.SEVERE, record.getLevel());
        assertTrue(record.getMessage().contains(msg));
        assertEquals(ex, record.getThrown());
    }

}
