plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false

    kotlin("plugin.serialization") version "1.9.22" apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
}
