package com.github.brokkko.openweathermap.jdk.schedulers;

import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.services.WeatherCacheService;
import com.github.brokkko.openweathermap.jdk.http.WeatherHttpExecutor;
import com.github.brokkko.openweathermap.jdk.request.RequestSettings;

import java.util.Map;

import static com.github.brokkko.openweathermap.jdk.constants.LogMessages.*;

/**
 * Periodically refreshes all cached weather entries by re-fetching them from the API.
 * <p>
 * The scheduler operates on a snapshot of the cache contents to avoid blocking concurrent reads/writes.
 * For each cached entry, it performs an HTTP request using the associated {@link RequestSettings} and
 * stores the updated JSON back into the cache.
 * </p>
 *
 * <p>
 * Errors during refresh of individual entries are logged, but they do not stop the polling cycle.
 * </p>
 */
public class WeatherPollingScheduler {

    private final WeatherCacheService cacheService;
    private final WeatherHttpExecutor httpExecutor;
    private final WeatherLogger logger;

    /**
     * Creates a new polling scheduler.
     *
     * @param cacheService cache used to read and update stored weather responses
     * @param httpExecutor executor used to perform HTTP requests for refreshing entries
     * @param logger       logger for debug/error messages
     */
    public WeatherPollingScheduler(WeatherCacheService cacheService, WeatherHttpExecutor httpExecutor, WeatherLogger logger) {
        this.cacheService = cacheService;
        this.httpExecutor = httpExecutor;
        this.logger = logger;
    }

    /**
     * Performs a single refresh cycle.
     * <p>
     * The method retrieves a snapshot of all cached {@link RequestSettings} from the cache service.
     * If no entries exist, a debug message is logged and the method returns immediately.
     * Otherwise, the scheduler re-fetches the latest data for each entry and updates the cache.
     * Errors for individual entries are logged but do not interrupt the operation.
     * </p>
     */
    public void pollOnce() {
        Map<String, RequestSettings> snapshot = cacheService.getAllRequestSettingsSnapshot();
        if (snapshot == null || snapshot.isEmpty()) {
            logger.debug(POLLING_NO_ENTRIES_MESSAGE);
            return;
        }

        logger.debug(String.format(POLLING_REFRESHING_MESSAGE, snapshot.size()));

        for (Map.Entry<String, RequestSettings> e : snapshot.entrySet()) {
            String key = e.getKey();
            RequestSettings settings = e.getValue();
            try {
                String newJson = httpExecutor.execute(settings);
                cacheService.put(key, newJson, settings.copy());
            } catch (Exception ex) {
                logger.error(String.format(POLLING_REFRESH_ERROR_MESSAGE, key), ex);
            }
        }
        logger.debug(POLLING_FINISHED_MESSAGE);
    }
}
