package com.github.brokkko.openweathermap.jdk.exceptions;

public class WeatherSdkException extends RuntimeException {
  public WeatherSdkException(String message) {
    super(message);
  }

  public WeatherSdkException(String message, Throwable cause) {
    super(message, cause);
  }
}
