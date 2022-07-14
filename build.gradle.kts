buildscript {

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath(BuildPlugins.android)
        classpath(BuildPlugins.kotlin)
        classpath(BuildPlugins.crashlytics)
        classpath(BuildPlugins.firebasePerf)
        classpath(BuildPlugins.hilt)
        classpath(BuildPlugins.googleServices)
        classpath("org.jacoco:org.jacoco.core:0.8.8")
    }
}

plugins {
    id("com.android.application") version Versions.gradle apply false
    id("com.android.library") version Versions.gradle apply false
    id("com.google.gms.google-services") version Versions.googleServices apply false
    id("org.jetbrains.kotlin.android") version Versions.kotlin apply false
    id("org.jetbrains.kotlin.jvm") version Versions.kotlin apply false
    id("com.github.ben-manes.versions") version "0.42.0"
    // id 'org.jmailen.kotlinter' version "3.10.0" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}




