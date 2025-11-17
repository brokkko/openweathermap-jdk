package com.github.brokkko.openweathermap.jdk.factories;

import com.github.brokkko.openweathermap.jdk.enums.LogLevel;
import com.github.brokkko.openweathermap.jdk.enums.LoggerType;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.logging.impl.DefaultWeatherLogger;

public class LoggerFactory {
    public static WeatherLogger create(LoggerType type, LogLevel logLevel, Class<?> owner) {
        return switch (type) {
            case DEFAULT -> new DefaultWeatherLogger(owner, logLevel);
        };
    }
}
