package com.github.brokkko.openweathermap.jdk.factories;

import com.github.brokkko.openweathermap.jdk.enums.LogLevel;
import com.github.brokkko.openweathermap.jdk.enums.LoggerType;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.logging.impl.DefaultWeatherLogger;

/**
 * Factory class responsible for creating {@link WeatherLogger} instances
 * based on the selected {@link LoggerType}.
 * <p>
 * The factory hides the concrete logger implementation and ensures
 * that the appropriate logger is returned depending on the provided type.
 */
public class LoggerFactory {

    /**
     * Creates a {@link WeatherLogger}.
     */
    public LoggerFactory() {
    }

    /**
     * Creates a {@link WeatherLogger} according to the specified logger type.
     *
     * @param type     the logger implementation type to instantiate
     * @param logLevel the desired logging level
     * @param owner    the owning class, used for logger context
     * @return a configured {@link WeatherLogger} instance
     * @throws NullPointerException if {@code type}, {@code owner} or {@code logLevel} is null
     */
    public static WeatherLogger create(LoggerType type, LogLevel logLevel, Class<?> owner) {
        return switch (type) {
            case DEFAULT -> new DefaultWeatherLogger(owner, logLevel);
        };
    }
}

