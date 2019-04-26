# sonar-plugin-core

[![pipeline status](https://gitlab.com/raatiniemi/sonar-plugin-core/badges/master/pipeline.svg)](https://gitlab.com/raatiniemi/sonar-plugin-core/commits/master)
[![quality gate](https://sonarcloud.io/api/project_badges/measure?project=me.raatiniemi.sonar%3Acore&metric=alert_status)](https://sonarcloud.io/dashboard?id=me.raatiniemi.sonar%3Acore)
[![code test coverage](https://sonarcloud.io/api/project_badges/measure?project=me.raatiniemi.sonar%3Acore&metric=coverage)](https://sonarcloud.io/dashboard?id=me.raatiniemi.sonar%3Acore)
[![code smells](https://sonarcloud.io/api/project_badges/measure?project=me.raatiniemi.sonar%3Acore&metric=code_smells)](https://sonarcloud.io/dashboard?id=me.raatiniemi.sonar%3Acore)

This repository contains core utilities for building plugins for SonarQube.

## Usage

In order to include the code from this repository, you'll first need to add the repository.

```gradle
repositories {
    maven {
        url  "https://dl.bintray.com/raatiniemi/maven"
    }
}
```

And, then you need to declare it as a dependency using `compile 'me.raatiniemi.sonar:core:$latestVersion'`.

*Dependency examples are using Gradle, for additional dependency options you can checkout
[sonar-plugin-core at bintray](https://bintray.com/raatiniemi/maven/sonar-plugin-core).*
