plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("kotlin-kapt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(GeneralDependencies.hiltCore)
    implementation(GeneralDependencies.coroutinesCore)
    kapt(GeneralDependencies.hiltCompiler)
}

