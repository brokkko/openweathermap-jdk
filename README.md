# OpenWeatherMap SDK

[![Release](https://github.com/brokkko/openweathermap-jdk/actions/workflows/release.yml/badge.svg)](https://github.com/brokkko/openweathermap-jdk/actions/workflows/release.yml)
[![CI](https://github.com/brokkko/openweathermap-jdk/actions/workflows/ci.yml/badge.svg?branch=master)](https://github.com/brokkko/openweathermap-jdk/actions/workflows/ci.yml)
[![Coverage](https://codecov.io/gh/brokkko/openweathermap-jdk/branch/master/graph/badge.svg)](https://codecov.io/gh/brokkko/openweathermap-jdk)
[![GitHub release](https://img.shields.io/github/v/release/brokkko/openweathermap-jdk)](https://github.com/brokkko/openweathermap-jdk/releases)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Java SDK for OpenWeatherMap API - A comprehensive, type-safe library for accessing current weather data with built-in caching, retry policies, and flexible configuration options.

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

## Requirements

- Java 21 or higher
- Maven 3.6+
- OpenWeatherMap API key ([Get one here](https://openweathermap.org/api))

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

### Documentation
* [OpenWeatherMap Java Sdk - v1.0.0](docs/Release_v1.0.0.md)

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Author

**Natalia Strannikova**  
Email: nsstrannikova@mail.ru  
GitHub: [@brokkko](https://github.com/brokkko)
