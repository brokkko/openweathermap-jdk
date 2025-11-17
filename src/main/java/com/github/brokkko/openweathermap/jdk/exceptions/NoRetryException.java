package com.github.brokkko.openweathermap.jdk.exceptions;

public class NoRetryException extends RetryException {
    public NoRetryException(Throwable cause) {
        super("Operation failed and retry is disabled", cause);
    }
}
