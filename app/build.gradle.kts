plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.mikepenz.aboutlibraries.plugin")
    kotlin("kapt")
}

val composeVersion = "1.1.0-beta03"

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "de.moritzruth.jamrss"
        minSdk = 29
        targetSdk = 31
        versionCode = 3
        versionName = "1.0.2"

        vectorDrawables {
            useSupportLibrary = true
        }

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf(
            "-Xopt-in=androidx.compose.animation.ExperimentalAnimationApi",
            "-Xopt-in=androidx.compose.ui.ExperimentalComposeUiApi",
            "-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi",
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlin.ExperimentalStdlibApi"
        )
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("com.rometools:rome:1.16.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    val aboutlibrariesVersion = "10.0.0-b02"
    implementation("com.mikepenz:aboutlibraries-core:$aboutlibrariesVersion")
    implementation("com.mikepenz:aboutlibraries-compose:$aboutlibrariesVersion")

    // Android
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Google
    implementation("com.google.android.material:material:1.4.0")

    // Jetpack Compose
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.navigation:navigation-compose:2.4.0-rc01")

    // Accompanist
    val accompanistVersion = "0.21.3-beta"
    implementation("com.google.accompanist:accompanist-swiperefresh:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-placeholder-material:$accompanistVersion")

    // Room
    val roomVersion = "2.4.0"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
}