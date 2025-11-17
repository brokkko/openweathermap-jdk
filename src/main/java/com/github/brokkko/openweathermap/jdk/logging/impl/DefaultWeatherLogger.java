package com.github.brokkko.openweathermap.jdk.logging.impl;

import com.github.brokkko.openweathermap.jdk.enums.LogLevel;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Default implementation of {@link WeatherLogger} based on the JDK {@link Logger}.
 * <p>
 * This logger prints messages to the console using {@link ConsoleHandler} and a custom
 * {@link WeatherSdkLogFormatter}. It supports SDK-defined logging levels from
 * {@link LogLevel} and maps them to corresponding JDK {@link Level} values.
 * <p>
 * ANSI colors are applied via the formatter to improve readability.
 *
 * <h2>Features:</h2>
 * <ul>
 *     <li>Per-class logger name</li>
 *     <li>Configurable log level</li>
 *     <li>Custom formatter with timestamps and colors</li>
 *     <li>Debug, info, warning, and error logging methods</li>
 * </ul>
 */
public class DefaultWeatherLogger implements WeatherLogger {

    private final Logger logger;

    /**
     * Creates a new logger bound to the given class with the specified log level.
     * The constructor configures a {@link ConsoleHandler} with a custom
     * {@link WeatherSdkLogFormatter} and disables parent handlers to avoid
     * duplicate log output. If multiple instances are created for the same class,
     * handlers are cleared to prevent double-logging.
     *
     * @param clazz the class for which the logger is created
     * @param level the desired logging level for this logger
     */
    public DefaultWeatherLogger(Class<?> clazz, LogLevel level) {
        this.logger = Logger.getLogger(clazz.getName());
        this.logger.setUseParentHandlers(false);

        // Remove already installed handlers (if constructor called multiple times)
        for (Handler h : logger.getHandlers()) {
            logger.removeHandler(h);
        }

        Handler handler = new ConsoleHandler();
        handler.setFormatter(new WeatherSdkLogFormatter());
        handler.setLevel(toJdkLevel(level));
        logger.setLevel(toJdkLevel(level));

        logger.addHandler(handler);
    }

    /**
     * Logs a debug-level message using {@link Level#FINE}.
     *
     * @param msg the message to log
     */
    @Override
    public void debug(String msg) {
        logger.fine(msg);
    }

    /**
     * Logs an info-level message using {@link Level#INFO}.
     *
     * @param msg the message to log
     */
    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    /**
     * Logs a warning-level message using {@link Level#WARNING}.
     *
     * @param msg the message to log
     */
    @Override
    public void warn(String msg) {
        logger.warning(msg);
    }

    /**
     * Logs an error-level message using {@link Level#SEVERE} and attaches a throwable.
     *
     * @param msg the error message to log
     * @param t   the associated exception or cause
     */
    @Override
    public void error(String msg, Throwable t) {
        logger.log(Level.SEVERE, msg, t);
    }

    /**
     * Maps SDK-level log severity to JDK {@link Level}.
     *
     * @param level the SDK log level
     * @return corresponding {@link Level}
     */
    private Level toJdkLevel(LogLevel level) {
        return switch (level) {
            case DEBUG -> Level.FINE;
            case INFO  -> Level.INFO;
            case WARN  -> Level.WARNING;
            case ERROR -> Level.SEVERE;
            case OFF   -> Level.OFF;
        };
    }

}
