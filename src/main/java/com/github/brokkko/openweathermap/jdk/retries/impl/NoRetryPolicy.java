package com.github.brokkko.openweathermap.jdk.retries.impl;

import com.github.brokkko.openweathermap.jdk.exceptions.NoRetryException;
import com.github.brokkko.openweathermap.jdk.retries.RetryPolicy;
import com.github.brokkko.openweathermap.jdk.retries.RetryableOperation;

/**
 * A retry policy that performs no retries.
 * <p>
 * The operation is executed once. If it fails, the exception is wrapped into a
 * {@link NoRetryException} and rethrown.
 * </p>
 */
public class NoRetryPolicy implements RetryPolicy {

    /**
     * Executes the operation once without retrying.
     *
     * @param operation retryable operation
     * @param <T>       result type
     * @return result of the operation
     *
     * @throws NoRetryException if the operation throws an exception
     */
    @Override
    public <T> T executeWithRetry(RetryableOperation<T> operation) {
        try {
            return operation.run();
        } catch (Exception e) {
            throw new NoRetryException(e);
        }
    }
}
