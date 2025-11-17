package com.github.brokkko.openweathermap.jdk.enums;

/**
 * Represents languages that can be used at WeatherOpenMap API.
 */
public enum Language {
    /**
     * Afrikaans language.
     */
    AFRIKAANS("af"),

    /**
     * Albanian language.
     */
    ALBANIAN("al"),

    /**
     * Arabic language.
     */
    ARABIC("ar"),

    /**
     * Azerbaijani language.
     */
    AZERBAIJANI("az"),

    /**
     * Bulgarian language.
     */
    BULGARIAN("bg"),

    /**
     * Catalan language.
     */
    CATALAN("ca"),

    /**
     * Czech language.
     */
    CZECH("cz"),

    /**
     * Danish language
     */
    DANISH("da"),

    /**
     * German language.
     */
    GERMAN("de"),

    /**
     * Greek language.
     */
    GREEK("el"),

    /**
     * English language.
     */
    ENGLISH("en"),

    /**
     * Basque language.
     */
    BASQUE("eu"),

    /**
     * Persian language.
     */
    PERSIAN("fa"),

    /**
     * Finnish language.
     */
    FINNISH("fi"),

    /**
     * French language.
     */
    FRENCH("fr"),

    /**
     * Galician language.
     */
    GALICIAN("gl"),

    /**
     * Hebrew language.
     */
    HEBREW("he"),

    /**
     * Hindi language.
     */
    HINDI("hi"),

    /**
     * Croatian language.
     */
    CROATIAN("hr"),

    /**
     * Hungarian language.
     */
    HUNGARIAN("hu"),

    /**
     * Indonesian language.
     */
    INDONESIAN("id"),

    /**
     * Italian language.
     */
    ITALIAN("it"),

    /**
     * Japanese language.
     */
    JAPANESE("ja"),

    /**
     * Korean language.
     */
    KOREAN("kr"),

    /**
     * Latvian language.
     */
    LATVIAN("la"),

    /**
     * Lithuanian language.
     */
    LITHUANIAN("lt"),

    /**
     * Macedonian language.
     */
    MACEDONIAN("mk"),

    /**
     * Norwegian language.
     */
    NORWEGIAN("no"),

    /**
     * Dutch language.
     */
    DUTCH("nl"),

    /**
     * Polish language.
     */
    POLISH("pl"),

    /**
     * Portuguese language.
     */
    PORTUGUESE("pt"),

    /**
     * Portuguese Brazil language.
     */
    PORTUGUESE_BRAZIL("pt_br"),

    /**
     * Romanian language.
     */
    ROMANIAN ("ro"),

    /**
     * Russian language.
     */
    RUSSIAN("ru"),

    /**
     * Swedish language.
     */
    SWEDISH("se"),

    /**
     * Slovak language.
     */
    SLOVAK("sk"),

    /**
     * Slovenian language.
     */
    SLOVENIAN("sl"),

    /**
     * Spanish language.
     */
    SPANISH("es"),

    /**
     * Serbian language.
     */
    SERBIAN("sr"),

    /**
     * Thai language.
     */
    THAI("th"),

    /**
     * Turkish language.
     */
    TURKISH("tr"),

    /**
     * Ukrainian language.
     */
    UKRAINIAN("uk"),

    /**
     * Vietnamese language.
     */
    VIETNAMESE("vi"),

    /**
     * Chinese simplified language.
     */
    CHINESE_SIMPLIFIED("zh_cn"),

    /**
     * Chinese traditional language.
     */
    CHINESE_TRADITIONAL("zh_tw"),

    /**
     * Zulu language.
     */
    ZULU("zu");

    private final String value;

    Language(String value) {
        this.value = value;
    }

    /**
     * Returns language value.
     * @return value.
     */
    public String getValue() {
        return value;
    }
}
