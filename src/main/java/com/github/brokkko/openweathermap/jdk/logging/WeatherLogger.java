package com.github.brokkko.openweathermap.jdk.logging;

import java.util.logging.Level;

/**
 * Logger.
 */
public interface WeatherLogger {
    /**
     * Logs a debug-level message using {@link Level#FINE}.
     *
     * @param msg the message to log
     */
    void debug(String msg);
    /**
     * Logs an info-level message using {@link Level#INFO}.
     *
     * @param msg the message to log
     */
    void info(String msg);
    /**
     * Logs a warning-level message using {@link Level#WARNING}.
     *
     * @param msg the message to log
     */
    void warn(String msg);
    /**
     * Logs an error-level message using {@link Level#SEVERE} and attaches a throwable.
     *
     * @param msg the error message to log
     * @param t   the associated exception or cause
     */
    void error(String msg, Throwable t);
}
