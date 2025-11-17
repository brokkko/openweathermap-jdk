package com.github.brokkko.openweathermap.jdk.exceptions;

public class RetryException extends WeatherSdkException {
    public RetryException(String message, Throwable cause) {
        super(message, cause);
    }
}
