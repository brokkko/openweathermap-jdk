package com.github.brokkko.openweathermap.jdk.retries;

import com.github.brokkko.openweathermap.jdk.exceptions.RetryException;

/**
 * Functional interface representing an operation that can be executed
 * with retry logic applied by a {@link RetryPolicy}.
 *
 * @param <T> the type of result returned by the operation
 */
@FunctionalInterface
public interface RetryableOperation<T> {

    /**
     * Executes the operation. If the operation fails and needs to be retried,
     * it should throw a {@link RetryException}, which allows the retry policy
     * to decide whether another attempt should be made.
     *
     * @return the result of the operation
     * @throws RetryException if the operation fails and may be retried
     */
    T run() throws RetryException;
}
