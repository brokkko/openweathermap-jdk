package com.github.brokkko.openweathermap.jdk.exceptions;

public class WeatherApiException extends WeatherSdkException {

    private final int statusCode;

    public WeatherApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public WeatherApiException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
