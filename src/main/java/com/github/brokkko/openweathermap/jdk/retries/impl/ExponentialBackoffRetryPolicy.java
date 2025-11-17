package com.github.brokkko.openweathermap.jdk.retries.impl;

import com.github.brokkko.openweathermap.jdk.exceptions.RetryExhaustedException;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.retries.RetryPolicy;
import com.github.brokkko.openweathermap.jdk.retries.RetryableOperation;

import static com.github.brokkko.openweathermap.jdk.constants.LogMessages.*;

public class ExponentialBackoffRetryPolicy implements RetryPolicy {
    private final int maxAttempts;
    private final long initialDelayMs;
    private final WeatherLogger logger;

    public ExponentialBackoffRetryPolicy(int maxAttempts, long initialDelayMs, WeatherLogger logger) {
        this.maxAttempts = maxAttempts;
        this.initialDelayMs = initialDelayMs;
        this.logger = logger;
    }

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


    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}
