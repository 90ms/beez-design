pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    // Kotlin/Wasm adds its Node.js distribution repository during configuration.
    // Keep settings repositories preferred while allowing that toolchain repository.
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "beez-design"

include(
    ":beez-tokens",
    ":beez-foundation",
    ":beez-components",
)
