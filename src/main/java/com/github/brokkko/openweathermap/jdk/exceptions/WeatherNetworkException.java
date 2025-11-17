package com.github.brokkko.openweathermap.jdk.exceptions;

public class WeatherNetworkException extends WeatherSdkException {
    public WeatherNetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
