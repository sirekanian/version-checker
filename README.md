## Setup

Using the [plugins DSL][1]:

```groovy
plugins {
    id "org.sirekanyan.version-checker" version "1.0.0"
}
```

Using [legacy plugin application][2]:

```groovy
buildscript {
    repositories {
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath "org.sirekanyan:version-checker:1.0.0"
    }
}

apply plugin: "org.sirekanyan.version-checker"
```

## Run

```bash
./gradlew versionChecker
```

## Options

If you don't want some dependency to be updated to the specific version, consider using `lessThan` inside
a `versionCheckerOptions` block.

For example:

```kotlin
versionCheckerOptions {
    "com.squareup.okhttp3:logging-interceptor" lessThan "4.0"
}
```

## Usages

This plugin is actively used during development of [Spacetime](https://github.com/sirekanian/spacetime),
[Warmongr](https://github.com/sirekanian/warmongr), [Anders Robot](https://github.com/sirekanian/andersrobot),
and [Knigopis](https://github.com/sirekanian/knigopis)

## Links

- [Gradle Plugin Portal][3]

[1]: https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block
[2]: https://docs.gradle.org/current/userguide/plugins.html#sec:old_plugin_application
[3]: https://plugins.gradle.org/plugin/org.sirekanyan.version-checker
