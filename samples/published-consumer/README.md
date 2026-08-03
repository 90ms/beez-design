# Published artifact consumer

This standalone Kotlin Multiplatform fixture resolves BEEZ from the repository-local Maven staging directory instead of using Gradle project dependencies.

From the repository root, publish the three aligned library modules and then build the fixture:

```text
gradle --no-daemon clean publishLibrariesToStagingRepository
gradle --no-daemon -p samples/published-consumer build
```

Pass the same non-default version to both builds when validating a release candidate:

```text
gradle --no-daemon clean publishLibrariesToStagingRepository -PbeezVersion=0.1.0-alpha.1
gradle --no-daemon -p samples/published-consumer build -PbeezVersion=0.1.0-alpha.1
```

The fixture compiles Android and Desktop targets, imports `BeezCheckbox` from `beez-components`, and imports `BeezTheme` through the transitive `beez-foundation` dependency. Material is intentionally absent.
