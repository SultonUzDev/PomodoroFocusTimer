plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    kotlin("plugin.serialization")
    alias(libs.plugins.hilt)
}


android {
    namespace = "com.sultonuzdev.pft"
    //noinspection GradleDependency
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sultonuzdev.pft"
        minSdk = 27
        targetSdk = 35
        versionCode = 4
        versionName = "1.0.4"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        release {

            ndk {
                debugSymbolLevel ="SYMBOL_TABLE"
            }
            isMinifyEnabled = true
            isShrinkResources=true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isMinifyEnabled = false
            isShrinkResources=false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {

        compose = true
    }



    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11" // ✅ FIXED version
    }
}

dependencies {
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    // Lifecycle
    implementation(libs.bundles.lifecycle)

    // Compose UI
    implementation(libs.bundles.compose)
    debugImplementation(libs.bundles.compose.debug)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Room Database
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler) // Using KSP instead of KAPT

    // DataStore Preferences
    implementation(libs.androidx.datastore.preferences)

    // Dependency Injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler) // Using KSP instead of KAPT

    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // Testing
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.android.testing)
}