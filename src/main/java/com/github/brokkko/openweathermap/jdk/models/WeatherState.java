package com.github.brokkko.openweathermap.jdk.models;

import com.github.brokkko.openweathermap.jdk.enums.WeatherCondition;

import java.util.Objects;

/**
 * The type Weather state.
 */
public class WeatherState {
    private final int id;
    private final String name;
    private final String description;
    private String iconId;
    private final WeatherCondition weatherConditionEnum;

    public WeatherState(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.weatherConditionEnum = WeatherCondition.getById(id);
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets icon id.
     *
     * @return the icon id
     */
    public String getIconId() {
        return iconId;
    }

    /**
     * Sets icon id.
     *
     * @param iconId the icon id
     */
    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    /**
     * Gets weather condition enum.
     *
     * @return the weather condition enum
     */
    public WeatherCondition getWeatherConditionEnum() {
        return weatherConditionEnum;
    }

    /**
     * Gets weather icon url.
     *
     * @return the weather icon url
     */
    public String getWeatherIconUrl() {
        if (iconId != null) {
            return WeatherCondition.getIconUrl(iconId);
        }
        if (weatherConditionEnum != null) {
            return weatherConditionEnum.getDayIconUrl();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherState that = (WeatherState) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(iconId, that.iconId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, iconId, weatherConditionEnum);
    }

    @Override
    public String toString() {
        return "Weather state: " + name + "(" + description + ").";
    }
}
