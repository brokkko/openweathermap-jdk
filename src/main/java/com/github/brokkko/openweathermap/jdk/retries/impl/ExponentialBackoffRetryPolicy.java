package com.github.brokkko.openweathermap.jdk.retries.impl;

import com.github.brokkko.openweathermap.jdk.exceptions.RetryExhaustedException;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.retries.RetryPolicy;
import com.github.brokkko.openweathermap.jdk.retries.RetryableOperation;

import static com.github.brokkko.openweathermap.jdk.constants.LogMessages.*;

/**
 * A retry policy that retries a failed operation using exponential backoff.
 * <p>
 * Each failed attempt doubles the delay before the next retry. The delay starts
 * from {@code initialDelayMs}. The policy logs every retry attempt, warning when a retry
 * fails, and logs an error when the maximum number of attempts is exhausted.
 * </p>
 *
 * <p>When all attempts fail, a {@link RetryExhaustedException} is thrown.</p>
 */
public class ExponentialBackoffRetryPolicy implements RetryPolicy {
    private final int maxAttempts;
    private final long initialDelayMs;
    private final WeatherLogger logger;

    /**
     * Constructs the exponential backoff policy.
     *
     * @param maxAttempts    maximum number of retry attempts before giving up
     * @param initialDelayMs initial delay before the first retry
     * @param logger         logger used to record retry progress
     */
    public ExponentialBackoffRetryPolicy(int maxAttempts, long initialDelayMs, WeatherLogger logger) {
        this.maxAttempts = maxAttempts;
        this.initialDelayMs = initialDelayMs;
        this.logger = logger;
    }

    /**
     * Executes the given retryable operation. On failure, retries the operation using exponential backoff.
     *
     * @param operation operation to execute
     * @param <T>       return type
     * @return operation result
     *
     * @throws RetryExhaustedException if all attempts fail
     */
    @Override
    public <T> T executeWithRetry(RetryableOperation<T> operation) {
        long delay = initialDelayMs;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                logger.debug(String.format(RETRY_ATTEMPT_MESSAGE, attempt));
                return operation.run();
            } catch (Exception ex) {
                if (attempt == maxAttempts) {
                    logger.error(String.format(RETRY_EXHAUSTED_MESSAGE, attempt), ex);
                    throw new RetryExhaustedException(attempt, ex);
                }
                logger.warn(String.format(RETRY_FAILED_MESSAGE, ex.getMessage(), delay));
                sleep(delay);
                delay *= 2;
            }
        }

        throw new IllegalStateException("Unreachable");
    }

    /**
     * Sleeps for the given amount of milliseconds. Extracted into its own method
     * to allow predictable testing via mocking/spying.
     *
     * @param ms delay in milliseconds
     */
    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}
