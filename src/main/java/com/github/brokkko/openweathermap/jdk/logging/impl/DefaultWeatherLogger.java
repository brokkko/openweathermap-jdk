package com.github.brokkko.openweathermap.jdk.logging.impl;

import com.github.brokkko.openweathermap.jdk.enums.LogLevel;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultWeatherLogger implements WeatherLogger {

    private final Logger logger;

    public DefaultWeatherLogger(Class<?> clazz, LogLevel level) {
        this.logger = Logger.getLogger(clazz.getName());
        this.logger.setUseParentHandlers(false);
        Handler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);
        logger.setLevel(Level.ALL);
        handler.setFormatter(new WeatherSdkLogFormatter());

        handler.setLevel(toJdkLevel(level));
        this.logger.addHandler(handler);

        this.logger.setLevel(toJdkLevel(level));
    }

    @Override
    public void debug(String msg) {
        logger.fine(msg);
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    @Override
    public void warn(String msg) {
        logger.warning(msg);
    }

    @Override
    public void error(String msg, Throwable t) {
        logger.log(Level.SEVERE, msg, t);
    }

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
