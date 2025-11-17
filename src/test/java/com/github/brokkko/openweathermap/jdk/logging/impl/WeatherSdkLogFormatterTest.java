package com.github.brokkko.openweathermap.jdk.logging.impl;

import org.junit.jupiter.api.Test;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.*;

class WeatherSdkLogFormatterTest {

    @Test
    void testFormatBasicMessage() {
        WeatherSdkLogFormatter formatter = new WeatherSdkLogFormatter();

        LogRecord record = new LogRecord(Level.INFO, "Hello world");
        record.setLoggerName("com.test.MyClass");

        String formatted = formatter.format(record);

        assertTrue(formatted.contains("Hello world"));
        assertTrue(formatted.contains("[INFO ]"));
        assertTrue(formatted.contains("MyClass"));
    }

    @Test
    void testFormatAddsColor() {
        WeatherSdkLogFormatter formatter = new WeatherSdkLogFormatter();

        LogRecord record = new LogRecord(Level.WARNING, "warn");
        String formatted = formatter.format(record);

        assertTrue(formatted.contains("\u001B[33m"));    // yellow
        assertTrue(formatted.contains("[WARN ]"));
    }

    @Test
    void testFormatStacktraceIncluded() {
        WeatherSdkLogFormatter formatter = new WeatherSdkLogFormatter();

        RuntimeException ex = new RuntimeException("boom");
        LogRecord record = new LogRecord(Level.SEVERE, "err");
        record.setThrown(ex);

        String formatted = formatter.format(record);

        assertTrue(formatted.contains("[ERROR]"));
    }

    @Test
    void testExtractSimpleClassName() {
        WeatherSdkLogFormatter formatter = new WeatherSdkLogFormatter();

        LogRecord record = new LogRecord(Level.INFO, "msg");
        record.setLoggerName("a.b.c.MyClass");

        String formatted = formatter.format(record);

        assertTrue(formatted.contains("(MyClass)"));
    }
}
