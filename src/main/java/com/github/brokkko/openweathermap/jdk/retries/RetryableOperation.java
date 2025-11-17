package com.github.brokkko.openweathermap.jdk.retries;

import com.github.brokkko.openweathermap.jdk.exceptions.RetryException;

@FunctionalInterface
public interface RetryableOperation<T> {
    T run() throws RetryException;
}
