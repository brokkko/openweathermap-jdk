package com.github.brokkko.openweathermap.jdk.retries;

@FunctionalInterface
public interface RetryPolicy {
    <T> T executeWithRetry(RetryableOperation<T> operation);
}
