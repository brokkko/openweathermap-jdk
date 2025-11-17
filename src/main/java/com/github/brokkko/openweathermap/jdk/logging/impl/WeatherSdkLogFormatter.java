package com.github.brokkko.openweathermap.jdk.logging.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class WeatherSdkLogFormatter extends Formatter {

    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));

    // ANSI COLORS
    private static final String RESET = "\u001B[0m";
    private static final String COLOR_DEBUG = "\u001B[90m";  // gray
    private static final String COLOR_INFO  = "\u001B[32m";  // green
    private static final String COLOR_WARN  = "\u001B[33m";  // yellow
    private static final String COLOR_ERROR = "\u001B[31m";  // red

    @Override
    public String format(LogRecord record) {
        String timestamp = DATE_FORMAT.get().format(new Date(record.getMillis()));

        // owner (class)
        String owner = extractSimpleClassName(record.getLoggerName());

        // choose color + map level text
        Level lvl = record.getLevel();
        String levelString = mapLevel(lvl);
        String color = mapColor(lvl);

        StringBuilder sb = new StringBuilder();

        sb.append(color)
                .append(timestamp)
                .append(" ")
                .append(levelString)
                .append(" (")
                .append(owner)
                .append(") ")
                .append(formatMessage(record))
                .append(RESET)
                .append("\n");

        // stacktrace if exists
        if (record.getThrown() != null) {
            sb.append(color);
            for (StackTraceElement el : record.getThrown().getStackTrace()) {
                sb.append("\tat ").append(el.toString()).append("\n");
            }
            sb.append(RESET);
        }

        return sb.toString();
    }

    private String extractSimpleClassName(String fqcn) {
        if (fqcn == null) return "Unknown";
        int idx = fqcn.lastIndexOf('.');
        return (idx > 0 ? fqcn.substring(idx + 1) : fqcn);
    }

    private String mapLevel(Level level) {
        if (level == Level.FINE || level == Level.FINER || level == Level.FINEST)
            return "[DEBUG]";
        if (level == Level.INFO)
            return "[INFO ]";
        if (level == Level.WARNING)
            return "[WARN ]";
        if (level == Level.SEVERE)
            return "[ERROR]";
        return "[" + level.getName() + "]";
    }

    private String mapColor(Level level) {
        if (level == Level.FINE || level == Level.FINER || level == Level.FINEST)
            return COLOR_DEBUG;
        if (level == Level.INFO)
            return COLOR_INFO;
        if (level == Level.WARNING)
            return COLOR_WARN;
        if (level == Level.SEVERE)
            return COLOR_ERROR;
        return RESET;
    }
}
