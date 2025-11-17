### Implemented features:
* Current weather data by city name, or coordinates
* Fluent API (`client.query().byCityName("London").retrieve().asJava()`)
* Type-safe weather data models
* Flexible SDK modes (On-demand and Polling)
* Built-in caching with TTL and size limits
* Retry policies with exponential backoff
* Multi-language support (50+ languages)
* Multiple unit systems (Metric, Imperial, Standard)
* Comprehensive logging (`WeatherLogger`, ANSI colors, log levels)
* Exception handling

### Maven (GitHub Packages)

1. Add GitHub Packages repository to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/brokkko/openweathermap-jdk</url>
    </repository>
</repositories>
```

2. Add the dependency:

```xml
<dependency>
    <groupId>com.github.brokkko</groupId>
    <artifactId>openweathermap-jdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

3. Configure authentication in `~/.m2/settings.xml`:

```xml
<settings>
    <servers>
        <server>
            <id>github</id>
            <username>YOUR_GITHUB_USERNAME</username>
            <password>YOUR_GITHUB_TOKEN</password>
        </server>
    </servers>
</settings>
```

> **Note:** You need a GitHub Personal Access Token with `read:packages` scope. [Create one here](https://github.com/settings/tokens).

### Gradle (GitHub Packages)

1. Add repository and dependency to `build.gradle`:

```gradle
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/brokkko/openweathermap-jdk")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.token") ?: System.getenv("TOKEN")
        }
    }
}

dependencies {
    implementation 'com.github.brokkko:openweathermap-jdk:1.0.0'
}
```

2. Add credentials to `~/.gradle/gradle.properties`:

```properties
gpr.user=YOUR_GITHUB_USERNAME
gpr.token=YOUR_GITHUB_TOKEN
```

## Quick Start

### Basic Usage (On-Demand Mode)

```java
// Create client
OpenWeatherMapClient client = OpenWeatherMapClient.builder()
    .apiKey("your-api-key-here")
    .mode(SdkMode.ON_DEMAND)
    .logLevel(LogLevel.INFO)
    .logger(LoggerType.DEFAULT)
    .retryPolicy(RetryPolicyType.EXPONENTIAL_BACKOFF)
    .httpTimeoutSeconds(10)
    .build();

// Query weather by city name
Weather weather = client.query()
    .byCityName("London")
    .unitSystem(UnitSystem.METRIC)
    .language(Language.ENGLISH)
    .retrieve()
    .asJava();

// Access weather data
System.out.println("Temperature: " + weather.getTemperature().getValue() + "°C");
System.out.println("Humidity: " + weather.getHumidity().getValue() + "%");
System.out.println("Description: " + weather.getWeatherState().getDescription());

// Clean up
client.destroy();
```

### Polling Mode (Auto-Refresh)

```java
OpenWeatherMapClient client = OpenWeatherMapClient.builder()
    .apiKey("your-api-key-here")
    .mode(SdkMode.POLLING_MODE)
    .logLevel(LogLevel.INFO)
    .logger(LoggerType.DEFAULT)
    .retryPolicy(RetryPolicyType.EXPONENTIAL_BACKOFF)
    .httpTimeoutSeconds(10)
    .pollingIntervalMinutes(10)
    .build();

// First query fetches and caches data
Weather weather = client.query()
    .byCityName("Paris")
    .unitSystem(UnitSystem.METRIC)
    .language(Language.ENGLISH)
    .retrieve()
    .asJava();

// Subsequent queries return cached data (refreshed every 10 minutes)
Weather cachedWeather = client.query()
    .byCityName("Paris")
    .unitSystem(UnitSystem.METRIC)
    .language(Language.ENGLISH)
    .retrieve()
    .asJava();
```

### Query Options

```java
// By city name
Weather weather = client.query()
    .byCityName("Tokyo")
    .unitSystem(UnitSystem.METRIC)
    .language(Language.ENGLISH)
    .retrieve()
    .asJava();

// By coordinates
Weather weather = client.query()
      .byCoordinates(Coordinate.of(51.5074, -0.1278))
      .unitSystem(UnitSystem.METRIC)
      .language(Language.ENGLISH)
      .retrieve()
      .asJava();
```
You are able to set preferable options (via chain methods) and execute appropriate request.

## Weather Data Models

### Temperature
```java
Temperature temp = weather.getTemperature();
double value = temp.getValue();           // Current temperature
double feelsLike = temp.getFeelsLike();   // Feels like temperature
double min = temp.getMinTemperature();    // Minimum temperature
double max = temp.getMaxTemperature();    // Maximum temperature
String unit = temp.getUnit();             // °C, °F, or K
```

### Wind
```java
Wind wind = weather.getWind();
double speed = wind.getSpeed();           // Wind speed
double degrees = wind.getDegrees();       // Wind direction
double gust = wind.getGust();             // Wind gust
String unit = wind.getUnit();             // m/s or miles/hour
```

### Location
```java
Location location = weather.getLocation();
String name = location.getName();                     // City name
String country = location.getCountryCode();           // Country code
Coordinate coord = location.getCoordinate();          // Lat/Lon
LocalDateTime sunrise = location.getSunriseTime();    // Sunrise
LocalDateTime sunset = location.getSunsetTime();      // Sunset
```

### Other Data
```java
AtmosphericPressure pressure = weather.getAtmosphericPressure();
Humidity humidity = weather.getHumidity();
Clouds clouds = weather.getClouds();
Rain rain = weather.getRain();
Snow snow = weather.getSnow();
WeatherState state = weather.getWeatherState();
LocalDateTime time = weather.getCalculationTime();
```

### Constants and options

#### Language
| Constant                     | Description                   |
|------------------------------|-------------------------------|
| Language.AFRIKAANS           | Afrikaans language.           |
| Language.ALBANIAN            | Albanian language.            |
| Language.ARABIC              | Arabic language.              |
| Language.AZERBAIJANI         | Azerbaijani language.         |
| Language.BULGARIAN           | Bulgarian language.           |
| Language.CATALAN             | Catalan language.             |
| Language.CZECH               | Czech language.               |
| Language.DANISH              | Danish language.              |
| Language.GERMAN              | German language.              |
| Language.GREEK               | Greek language.               |
| Language.ENGLISH             | English language.             |
| Language.BASQUE              | Basque language.              |
| Language.PERSIAN             | Persian (Farsi) language.     |
| Language.FINNISH             | Finnish language.             |
| Language.FRENCH              | French language.              |
| Language.GALICIAN            | Galician language.            |
| Language.HEBREW              | Hebrew language.              |
| Language.HINDI               | Hindi language.               |
| Language.CROATIAN            | Croatian language.            |
| Language.HUNGARIAN           | Hungarian language.           |
| Language.INDONESIAN          | Indonesian language.          |
| Language.ITALIAN             | Italian language.             |
| Language.JAPANESE            | Japanese language.            |
| Language.KOREAN              | Korean language.              |
| Language.LATVIAN             | Latvian language.             |
| Language.LITHUANIAN          | Lithuanian language.          |
| Language.MACEDONIAN          | Macedonian language.          |
| Language.NORWEGIAN           | Norwegian language.           |
| Language.DUTCH               | Dutch language.               |
| Language.POLISH              | Polish language.              |
| Language.PORTUGUESE          | Portuguese language.          |
| Language.PORTUGUES_BRAZIL    | Português Brasil language.    |
| Language.ROMANIAN            | Romanian language.            |
| Language.RUSSIAN             | Russian language.             |
| Language.SWEDISH             | Swedish language.             |
| Language.SLOVAK              | Slovak language.              |
| Language.SLOVENIAN           | Slovenian language.           |
| Language.SPANISH             | Spanish language.             |
| Language.SERBIAN             | Serbian language.             |
| Language.THAI                | Thai language.                |
| Language.TURKISH             | Turkish language.             |
| Language.UKRAINIAN           | Ukrainian language.           |
| Language.VIETNAMESE          | Vietnamese language.          |
| Language.CHINESE_SIMPLIFIED  | Chinese Simplified language.  |
| Language.CHINESE_TRADITIONAL | Chinese Traditional language. |
| Language.ZULU                | Zulu language.                |

#### Unit System
| Constant             | Description                                    |
|----------------------|------------------------------------------------|
| UnitSystem.METRIC    | Celsius, meter/sec, hPa, mm (rain, snow).      |
| UnitSystem.IMPERIAL  | Fahrenheit, miles/hour, hPa, mm (rain, snow).  |
| UnitSystem.STANDARD  | Kelvin, meter/sec, hPa, mm (rain, snow).       |

### Dependencies
* com.fasterxml.jackson.core:jackson-databind:2.13.4.2
* org.junit.jupiter:junit-jupiter-api:5.10.0 (*test*)
* org.junit.jupiter:junit-jupiter-engine:5.10.0 (*test*)
* org.mockito:mockito-core:5.20.0 (*test*)
