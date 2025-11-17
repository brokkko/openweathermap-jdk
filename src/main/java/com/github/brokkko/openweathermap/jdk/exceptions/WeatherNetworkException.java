package com.github.brokkko.openweathermap.jdk.exceptions;

/**
 * Exception representing network-level errors when communicating with the API.
 */
public class WeatherNetworkException extends WeatherSdkException {
    public WeatherNetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
