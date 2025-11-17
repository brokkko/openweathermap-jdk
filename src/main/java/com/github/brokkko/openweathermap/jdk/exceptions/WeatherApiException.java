package com.github.brokkko.openweathermap.jdk.exceptions;

/**
 * Exception representing an error returned by the weather API.
 * Includes the HTTP status code for reference.
 */
public class WeatherApiException extends WeatherSdkException {

    /**
     * Exception status code.
     */
    private final int statusCode;

    /**
     * Creates a new {@code WeatherApiException}.
     *
     * @param message description of the api condition
     * @param statusCode exception status code
     */
    public WeatherApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Creates a new {@code WeatherApiException}.
     *
     * @param message description of the api condition
     * @param statusCode exception status code
     * @param cause   the underlying cause of the api
     */
    public WeatherApiException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    /**
     * Gets error status code.
     *
     * @return error status code
     */
    public int getStatusCode() {
        return statusCode;
    }
}
