package com.github.brokkko.openweathermap.jdk.constants;

/**
 * A centralized set of string constants representing JSON field names
 * returned by the OpenWeather API.
 * <p>
 * These constants are used throughout the SDK during JSON parsing,
 * data mapping, and model creation. Defining all JSON keys in one place
 * helps avoid hard-coded strings, prevents typographical errors,
 * and simplifies maintenance when the API structure changes.
 * <p>
 * The constants correspond to actual fields returned by OpenWeather,
 * such as {@code "temp"}, {@code "humidity"}, {@code "wind"}, etc.
 */
public final class JsonFieldNameConstants {

    private JsonFieldNameConstants() {}

    /** JSON array containing weather condition objects. */
    public static final String WEATHER = "weather";

    /** Unix timestamp of the weather data calculation. */
    public static final String DT = "dt";

    /** Object containing temperature and humidity information. */
    public static final String MAIN = "main";

    /** Weather condition description (human readable). */
    public static final String DESCRIPTION = "description";

    /** Weather icon identifier. */
    public static final String ICON = "icon";

    /** Current temperature. */
    public static final String TEMP = "temp";

    /** Perceived temperature. */
    public static final String FEELS_LIKE = "feels_like";

    /** Maximum temperature at the moment. */
    public static final String TEMP_MAX = "temp_max";

    /** Minimum temperature at the moment. */
    public static final String TEMP_MIN = "temp_min";

    /** Sea-level atmospheric pressure. */
    public static final String SEA_LEVEL = "sea_level";

    /** Ground-level atmospheric pressure. */
    public static final String GRND_LEVEL = "grnd_level";

    /** Air humidity percentage. */
    public static final String HUMIDITY = "humidity";

    /** Object containing wind information. */
    public static final String WIND = "wind";

    /** Wind speed. */
    public static final String SPEED = "speed";

    /** Wind direction in degrees. */
    public static final String DEG = "deg";

    /** Wind gust speed. */
    public static final String GUST = "gust";

    /** Object containing rain volume information. */
    public static final String RAIN = "rain";

    /** Object containing snow volume information. */
    public static final String SNOW = "snow";

    /** Precipitation volume for the last 1 hour. */
    public static final String VALUE_1H = "1h";

    /** Precipitation volume for the last 3 hours. */
    public static final String VALUE_3H = "3h";

    /** Cloudiness information. */
    public static final String CLOUDS = "clouds";

    /** Cloudiness percentage. */
    public static final String ALL = "all";

    /** Object containing system-related weather data (e.g., country, sunrise). */
    public static final String SYS = "sys";

    /** Country code. */
    public static final String COUNTRY = "country";

    /** Sunrise time in Unix timestamp. */
    public static final String SUNRISE = "sunrise";

    /** Sunset time in Unix timestamp. */
    public static final String SUNSET = "sunset";

    /** Coordinates object. */
    public static final String COORD = "coord";

    /** Latitude. */
    public static final String LAT = "lat";

    /** Longitude. */
    public static final String LON = "lon";

    /** Location identifier. */
    public static final String ID = "id";

    /** City or location name. */
    public static final String NAME = "name";

    /** Shift in seconds from UTC for the location's timezone. */
    public static final String TIMEZONE = "timezone";
}
