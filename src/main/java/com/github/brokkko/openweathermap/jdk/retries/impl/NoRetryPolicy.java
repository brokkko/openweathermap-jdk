package com.github.brokkko.openweathermap.jdk.retries.impl;

import com.github.brokkko.openweathermap.jdk.exceptions.NoRetryException;
import com.github.brokkko.openweathermap.jdk.retries.RetryPolicy;
import com.github.brokkko.openweathermap.jdk.retries.RetryableOperation;

public class NoRetryPolicy implements RetryPolicy {

    @Override
    public <T> T executeWithRetry(RetryableOperation<T> operation) {
        try {
            return operation.run();
        } catch (Exception e) {
            throw new NoRetryException(e);
        }
    }

}
