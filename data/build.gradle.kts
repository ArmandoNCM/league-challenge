import java.util.Properties

plugins {
    id("com.android.library")
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

val baseUrl = Properties().run {
    val localPropsFile = rootProject.file("local.properties")
    if (localPropsFile.exists()) {
        localPropsFile.reader().use { load(it) }
    }
    getProperty("BASE_URL")?.takeIf { it.isNotBlank() } ?: "localhost"
}

android {
    namespace = "life.league.challenge.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        // Base URL for REST API
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        buildConfig = true
    }

    androidResources {
        @Suppress("UnstableApiUsage")
        testFixtures.enable = true
    }
}

dependencies {
    // Domain module
    implementation(project(":domain"))

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // REST API
    implementation(libs.bundles.rest.api)

    // JUnit
    testImplementation(libs.junit)

    // Test Fixtures
    testFixturesImplementation(project(":domain"))
    testFixturesImplementation(libs.hilt.android)
    kspTestFixtures(libs.hilt.compiler)
}