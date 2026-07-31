plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
}

allprojects {
    group = "beez.design"
    version = "0.1.0-SNAPSHOT"
}
