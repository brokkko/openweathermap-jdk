package com.github.brokkko.openweathermap.jdk.schedulers;

import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.services.WeatherCacheService;
import com.github.brokkko.openweathermap.jdk.http.WeatherHttpExecutor;
import com.github.brokkko.openweathermap.jdk.request.RequestSettings;

import java.util.Map;

import static com.github.brokkko.openweathermap.jdk.constants.LogMessages.*;

/**
 * Scheduler that performs one polling pass over all cached entries.
 * Получает snapshot requestSettings из cacheService и обновляет кеш.
 *
 * Ключевые требования:
 * - Быть устойчивым: ошибки по одному ключу логировать и продолжать.
 * - Быть быстрым: не держать глобальные блокировки в cacheService.
 */
public class WeatherPollingScheduler {

    private final WeatherCacheService cacheService;
    private final WeatherHttpExecutor httpExecutor;
    private final WeatherLogger logger;

    public WeatherPollingScheduler(WeatherCacheService cacheService, WeatherHttpExecutor httpExecutor, WeatherLogger logger) {
        this.cacheService = cacheService;
        this.httpExecutor = httpExecutor;
        this.logger = logger;
    }

    /**
     * Выполняет один проход обновления кэша.
     * Метод получает snapshot (копию) ключей и настроек из cacheService, чтобы не блокировать внешний доступ.
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
                // executor может выбросить конкретные исключения (timeout, api error и т.д.)
                String newJson = httpExecutor.execute(settings);
                // сохраняем в кэш вместе с копией RequestSettings (чтобы polling мог использовать её позже)
                cacheService.put(key, newJson, settings.copy());
            } catch (Exception ex) {
                // Логируем и продолжаем обновлять другие ключи
                logger.error(String.format(POLLING_REFRESH_ERROR_MESSAGE, key), ex);
            }
        }
        logger.debug(POLLING_FINISHED_MESSAGE);
    }
}
