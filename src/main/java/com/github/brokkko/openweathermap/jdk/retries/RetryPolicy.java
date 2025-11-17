package com.github.brokkko.openweathermap.jdk.retries;

/**
 * Functional interface representing a retry policy strategy.
 * <p>
 * A retry policy defines how an operation should be retried
 * when it fails, and decides whether to continue retrying or
 * propagate an exception.
 */
@FunctionalInterface
public interface RetryPolicy {

    /**
     * Executes the given operation using the retry strategy implemented
     * by this policy. The policy determines the number of retry attempts,
     * delays between attempts, and whether the operation should stop retrying.
     *
     * @param operation the operation to execute with retry logic
     * @param <T> the return type of the operation
     * @return the result of the executed operation
     */
    <T> T executeWithRetry(RetryableOperation<T> operation);
}
