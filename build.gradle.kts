// Top-level build file where you can add configuration options common to all subprojects/modules.
buildscript {
    val kotlinVersion by extra("2.3.10")

    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:9.1.0")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:compose-compiler-gradle-plugin:$kotlinVersion")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
