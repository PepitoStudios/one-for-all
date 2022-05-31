buildscript {

    repositories {
        // Check that you have Google's Maven repository (if not, add it).
        google()
        mavenCentral()
    }

    dependencies {
        classpath(BuildPlugins.android)
        classpath(BuildPlugins.kotlin)
        classpath(BuildPlugins.crashlytics)
        classpath(BuildPlugins.firebasePerf)
        classpath(BuildPlugins.hilt)
        classpath(BuildPlugins.googleServices)
    }
} // Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version Versions.gradle apply false
    id("com.android.library") version Versions.gradle apply false
    id("com.google.gms.google-services") version Versions.googleServices apply false
    id("org.jetbrains.kotlin.android") version Versions.kotlin apply false
    id("org.jetbrains.kotlin.jvm") version Versions.kotlin apply false
    // id 'org.jmailen.kotlinter' version "3.10.0" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
