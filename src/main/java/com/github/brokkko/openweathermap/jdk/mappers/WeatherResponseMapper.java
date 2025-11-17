package com.github.brokkko.openweathermap.jdk.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.brokkko.openweathermap.jdk.enums.UnitSystem;
import com.github.brokkko.openweathermap.jdk.exceptions.WeatherSerializationException;
import com.github.brokkko.openweathermap.jdk.logging.WeatherLogger;
import com.github.brokkko.openweathermap.jdk.models.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.TimeZone;

import static com.github.brokkko.openweathermap.jdk.constants.JsonFieldNameConstants.*;
import static com.github.brokkko.openweathermap.jdk.constants.LogMessages.JSON_PARSE_ERROR_MESSAGE;

/**
 * Official API response documentation:
 * Parameters:
 * --- coord
 *      |- coord.lon City geo location, longitude
 *      |- coord.lat City geo location, latitude
 * --- weather (more info Weather condition codes)
 *      |- weather.id Weather condition id
 *      |- weather.main Group of weather parameters (Rain, Snow, Extreme etc.)
 *      |- weather.description Weather condition within the group
 *      |- weather.icon Weather icon id
 * --- base Internal parameter
 * --- main
 *      |- main.temp Temperature. UnitSystem Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
 *      |- main.feels_like Temperature. This temperature parameter accounts for the human perception of weather. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
 *      |- main.pressure Atmospheric pressure (on the sea level, if there is no sea_level or grnd_level data), hPa
 *      |- main.humidity Humidity, %
 *      |- main.temp_min Minimum temperature at the moment. This is deviation from current temp that is possible for large cities and megalopolises geographically expanded (use these parameter optionally). UnitSystem Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
 *      |- main.temp_max Maximum temperature at the moment. This is deviation from current temp that is possible for large cities and megalopolises geographically expanded (use these parameter optionally). UnitSystem Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
 *      |- main.sea_level Atmospheric pressure on the sea level, hPa
 *      |- main.grnd_level Atmospheric pressure on the ground level, hPa
 * --- wind
 *      |- wind.speed Wind speed. UnitSystem Default: meter/sec, Metric: meter/sec, Imperial: miles/hour.
 *      |- wind.deg Wind direction, degrees (meteorological)
 *      |- wind.gust Wind gust. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour
 * --- clouds
 *      |- clouds.all Cloudiness, %
 * --- rain
 *      |- rain.1h Rain volume for the last 1 hour, mm
 *      |- rain.3h Rain volume for the last 3 hours, mm
 * --- snow
 *      |- snow.1h Snow volume for the last 1 hour, mm
 *      |- snow.3h Snow volume for the last 3 hours, mm
 * --- dt Time of data calculation, unix, UTC
 * --- sys
 *      |- sys.type Internal parameter
 *      |- sys.id Internal parameter
 *      |- sys.message Internal parameter
 *      |- sys.country Country code (GB, JP etc.)
 *      |- sys.sunrise Sunrise time, unix, UTC
 *      |- sys.sunset Sunset time, unix, UTC
 * --- timezone Shift in seconds from UTC
 * --- id City ID
 * --- name City name
 * --- cod Internal parameter
 */
public class WeatherResponseMapper {
    private final UnitSystem unitSystem;
    private final WeatherLogger logger;

    // TODO: вынести константы
    public WeatherResponseMapper(UnitSystem unitSystem, WeatherLogger logger) {
        this.unitSystem = unitSystem != null ? unitSystem : UnitSystem.STANDARD;
        this.logger = logger;
    }

    public Weather mapJsonToWeather(String json) {
        final ObjectMapper objectMapper = new ObjectMapper();
        Weather weather;
        try {
            final JsonNode root = objectMapper.readTree(json);
            weather = parseWeather(root);
        } catch (JsonProcessingException e) {
            logger.error(JSON_PARSE_ERROR_MESSAGE, e);
            throw new WeatherSerializationException(JSON_PARSE_ERROR_MESSAGE, e);
        }
        return weather;
    }

    private Weather parseWeather(JsonNode rootNode) {
        final JsonNode weatherArrayNode = rootNode.get(WEATHER);
        final JsonNode weatherNode = weatherArrayNode != null ? weatherArrayNode.get(0) : null;
        final Weather weather = new Weather();

        weather.setWeatherState(parseWeatherState(weatherNode));
        weather.setTemperature(parseTemperature(rootNode));
        weather.setAtmosphericPressure(parsePressure(rootNode));
        weather.setHumidity(parseHumidity(rootNode));
        weather.setWind(parseWind(rootNode));
        weather.setRain(parseRain(rootNode));
        weather.setSnow(parseSnow(rootNode));
        weather.setClouds(parseClouds(rootNode));
        weather.setLocation(parseLocation(rootNode));

        final JsonNode dtNode = rootNode.get(DT);
        if (dtNode != null) {
            weather.setCalculationTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(dtNode.asInt()), TimeZone.getDefault().toZoneId()));
        }
        return weather;
    }

    private WeatherState parseWeatherState(JsonNode weatherNode) {
        if (weatherNode == null) {
            return null;
        }
        final WeatherState weatherState = new WeatherState(
                weatherNode.get(ID).asInt(),
                weatherNode.get(MAIN).asText(),
                weatherNode.get(DESCRIPTION).asText()
        );
        weatherState.setIconId(weatherNode.get(ICON).asText());
        return weatherState;
    }

    private Temperature parseTemperature(JsonNode rootNode) {
        final JsonNode mainNode = rootNode.get(MAIN);
        final double tempValue = mainNode.get(TEMP).asDouble();
        final Temperature temperature = Temperature.withValue(tempValue, unitSystem.getTemperatureUnit());

        final JsonNode feelsLikeNode = mainNode.get(FEELS_LIKE);
        if (feelsLikeNode != null) {
            temperature.setFeelsLike(feelsLikeNode.asDouble());
        }
        final JsonNode tempMaxNode = mainNode.get(TEMP_MAX);
        if (tempMaxNode != null) {
            temperature.setMaxTemperature(tempMaxNode.asDouble());
        }
        final JsonNode tempMinNode = mainNode.get(TEMP_MIN);
        if (tempMinNode != null) {
            temperature.setMinTemperature(tempMinNode.asDouble());
        }
        return temperature;
    }

    private AtmosphericPressure parsePressure(JsonNode rootNode) {
        final JsonNode mainNode = rootNode.get(MAIN);
        final AtmosphericPressure atmosphericPressure = AtmosphericPressure.withValue(mainNode.get("pressure").asDouble());

        final JsonNode seaLevelNode = mainNode.get(SEA_LEVEL);
        final JsonNode groundLevelNode = mainNode.get(GRND_LEVEL);
        if (seaLevelNode != null) {
            atmosphericPressure.setSeaLevelValue(seaLevelNode.asDouble());
        }
        if (groundLevelNode != null) {
            atmosphericPressure.setGroundLevelValue(groundLevelNode.asDouble());
        }
        return atmosphericPressure;
    }

    private Humidity parseHumidity(JsonNode rootNode) {
        final JsonNode mainNode = rootNode.get(MAIN);
        return Humidity.withValue((byte) (mainNode.get(HUMIDITY).asInt()));
    }

    private Wind parseWind(JsonNode rootNode) {
        final JsonNode windNode = rootNode.get(WIND);
        double speed = windNode.get(SPEED).asDouble();

        final Wind wind = Wind.withValue(speed, unitSystem.getWindUnit());

        final JsonNode degNode = windNode.get(DEG);
        if (degNode != null) {
            wind.setDegrees(degNode.asDouble());
        }
        final JsonNode gustNode = windNode.get(GUST);
        if (gustNode != null) {
            wind.setGust(gustNode.asDouble());
        }
        return wind;
    }

    private Rain parseRain(JsonNode rootNode) {
        final JsonNode rainNode = rootNode.get(RAIN);
        if (rainNode != null) {
            final JsonNode oneHourNode = rainNode.get(VALUE_1H);
            final JsonNode threeHourNode = rainNode.get(VALUE_3H);
            if (oneHourNode != null && threeHourNode != null) {
                return Rain.withValues(oneHourNode.asDouble(), threeHourNode.asDouble());
            } else if (oneHourNode != null) {
                return Rain.withOneHourLevelValue(oneHourNode.asDouble());
            } else if (threeHourNode != null) {
                return Rain.withThreeHourLevelValue(threeHourNode.asDouble());
            }
        }
        return null;
    }

    private Snow parseSnow(JsonNode rootNode) {
        final JsonNode snowNode = rootNode.get(SNOW);
        if (snowNode != null) {
            final JsonNode oneHourNode = snowNode.get(VALUE_1H);
            final JsonNode threeHourNode = snowNode.get(VALUE_3H);
            if (oneHourNode != null && threeHourNode != null) {
                return Snow.withValues(oneHourNode.asDouble(), threeHourNode.asDouble());
            } else if (oneHourNode != null) {
                return Snow.withOneHourLevelValue(oneHourNode.asDouble());
            } else if (threeHourNode != null) {
                return Snow.withThreeHourLevelValue(threeHourNode.asDouble());
            }
        }
        return null;
    }

    private Clouds parseClouds(JsonNode rootNode) {
        final JsonNode cloudsNode = rootNode.get(CLOUDS);
        final JsonNode allValueNode = cloudsNode.get(ALL);
        if (allValueNode != null) {
            return Clouds.withValue((byte) allValueNode.asInt());
        }
        return null;
    }

    private Location parseLocation(JsonNode rootNode) {
        final Location location = Location.withValues(rootNode.get(ID).asInt(), rootNode.get(NAME).asText());

        final JsonNode timezoneNode = rootNode.get(TIMEZONE);
        if (timezoneNode != null) {
            location.setZoneOffset(ZoneOffset.ofTotalSeconds(timezoneNode.asInt()));
        }

        final JsonNode sysNode = rootNode.get(SYS);
        if (sysNode != null) {
            final JsonNode countryNode = sysNode.get(COUNTRY);
            if (countryNode != null) {
                location.setCountryCode(countryNode.asText());
            }

            final JsonNode sunriseNode = sysNode.get(SUNRISE);
            final JsonNode sunsetNode = sysNode.get(SUNSET);
            if (sunriseNode != null) {
                location.setSunriseTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(sunriseNode.asInt()), TimeZone.getDefault().toZoneId()));
            }
            if (sunsetNode != null) {
                location.setSunsetTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(sunsetNode.asInt()), TimeZone.getDefault().toZoneId()));
            }
        }

        final JsonNode coordNode = rootNode.get(COORD);
        if (coordNode != null) {
            location.setCoordinate(parseCoordinate(coordNode));
        }
        return location;
    }

    private Coordinate parseCoordinate(JsonNode rootNode) {
        final JsonNode latitudeNode = rootNode.get(LAT);
        final JsonNode longitudeNode = rootNode.get(LON);
        if (latitudeNode != null && longitudeNode != null) {
            return Coordinate.of(latitudeNode.asDouble(), longitudeNode.asDouble());
        }
        return null;
    }

}
