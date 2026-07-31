pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    // Kotlin/Wasm adds its Node.js distribution repository during configuration.
    // Project repositories are preferred so that toolchain repository remains usable.
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
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
