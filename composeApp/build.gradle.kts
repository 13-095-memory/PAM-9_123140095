import java.util.Properties
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)

            // Koin Android
            implementation("io.insert-koin:koin-android:3.5.3")
            implementation("io.insert-koin:koin-androidx-compose:3.5.3")

            // Navigation Android
            implementation("androidx.navigation:navigation-compose:2.7.7")

            // Ktor - Android Engine
            implementation(libs.ktor.client.okhttp)
        }

        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation("org.jetbrains.compose.material:material:1.6.11")
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Koin Core + Compose
            implementation("io.insert-koin:koin-core:4.0.0")
            implementation("io.insert-koin:koin-compose:4.0.0")
            implementation("io.insert-koin:koin-compose-viewmodel:4.0.0")

            // Navigation
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha10")

            // Coroutines
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

            // DateTime
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
            implementation("org.jetbrains.compose.material:material-icons-extended:1.6.11")
            implementation("org.jetbrains.compose.material:material-icons-core:1.6.11")

            // Ktor - Core + Serialization
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.serialization.json)
        }


        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.ktor.client.cio)
        }

        iosMain.dependencies {
                implementation(libs.ktor.client.darwin)
            }

    }
}

android {
    namespace = "com.memory.platform_specificfeatures"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.memory.platform_specificfeatures"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        // Baca API key dari local.properties
        val localProps = rootProject.file("local.properties")
        val properties = Properties()
        if (localProps.exists()) {
            properties.load(localProps.inputStream())
        }
        buildConfigField(
            "String", "GEMINI_API_KEY",
            "\"${properties.getProperty("GEMINI_API_KEY", "")}\""
        )
        buildConfigField(
            "String", "GROQ_API_KEY",
            "\"${properties.getProperty("GROQ_API_KEY", "")}\""
        )
    }
    buildFeatures { buildConfig = true }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.memory.platform_specificfeatures.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.memory.platform_specificfeatures"
            packageVersion = "1.0.0"
        }
    }
}