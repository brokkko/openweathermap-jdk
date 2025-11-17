package com.github.brokkko.openweathermap.jdk.enums;

public enum ResponseType {
    HTML("html"),
    XML("xml");

    private final String value;

    ResponseType(String value) {
        this.value = value;
    }

    /**
     * Returns response type value.
     * @return value.
     */
    public String getValue() {
        return value;
    }
}
