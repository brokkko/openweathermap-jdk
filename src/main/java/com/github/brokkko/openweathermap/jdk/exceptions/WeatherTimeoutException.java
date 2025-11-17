package com.github.brokkko.openweathermap.jdk.exceptions;

public class WeatherTimeoutException extends WeatherSdkException {

  public WeatherTimeoutException(String message, Throwable cause) {
    super(message, cause);
  }
}
