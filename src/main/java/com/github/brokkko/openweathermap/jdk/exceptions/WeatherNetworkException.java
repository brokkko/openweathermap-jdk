package com.github.brokkko.openweathermap.jdk.exceptions;

/**
 * Exception representing network-level errors when communicating with the API.
 */
public class WeatherNetworkException extends WeatherSdkException {

    /**
     * Creates a new {@code WeatherNetworkException}.
     *
     * @param message description of the network condition
     * @param cause   the underlying cause of the network
     */
    public WeatherNetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
