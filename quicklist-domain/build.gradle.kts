plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("kotlin-kapt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = ConfigData.jvmVersion
    }
}

dependencies {
    implementation(GeneralDependencies.hiltCore)
    implementation(GeneralDependencies.coroutinesCore)
    kapt(GeneralDependencies.hiltCompiler)
}

tasks.register("makeTest") {
    dependsOn("test")
}
