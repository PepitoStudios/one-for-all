plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.6.10-1.0.2"
    id("kotlin-kapt")
}

android {
    namespace = "com.unatxe.quicklist.data"
    compileSdk = ConfigData.compileSdkVersion

    defaultConfig {
        minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = ConfigData.jvmVersion
    }
    buildToolsVersion = ConfigData.buildToolsVersion
}

dependencies {

    implementation(GeneralDependencies.androidXCore)
    implementation(GeneralDependencies.coroutinesAndroid)

    testImplementation(Testing.jUnit)
    androidTestImplementation(Testing.extJUnit)
    androidTestImplementation(Testing.espressoCore)

    implementation(GeneralDependencies.hilt)
    kapt(GeneralDependencies.hiltCompiler)

    implementation(project(":quicklist-domain"))

    implementation(Room.runtime)
    ksp(Room.compiler)
    implementation(Room.ktx)
    testImplementation(Room.testing)







}
