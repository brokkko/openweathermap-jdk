package com.github.brokkko.openweathermap.jdk.logging;

public interface WeatherLogger {
    void debug(String msg);
    void info(String msg);
    void warn(String msg);
    void error(String msg, Throwable t);
}
