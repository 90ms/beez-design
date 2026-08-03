import org.gradle.api.publish.PublishingExtension

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
}

val beezVersion = providers.gradleProperty("beezVersion")
    .orElse("0.1.0-SNAPSHOT")

allprojects {
    group = "beez.design"
    version = beezVersion.get()
}

subprojects {
    pluginManager.withPlugin("maven-publish") {
        configure<PublishingExtension> {
            repositories {
                maven {
                    name = "staging"
                    url = rootProject.layout.buildDirectory
                        .dir("staging-repository")
                        .get()
                        .asFile
                        .toURI()
                }
            }
        }
    }
}

tasks.register("publishLibrariesToStagingRepository") {
    group = "publishing"
    description = "Publishes all BEEZ library variants to the local staging repository."
    dependsOn(
        ":beez-tokens:publishAllPublicationsToStagingRepository",
        ":beez-foundation:publishAllPublicationsToStagingRepository",
        ":beez-components:publishAllPublicationsToStagingRepository",
    )
}
