plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version Versions.ksp
    id("kotlin-kapt")
    id("jacoco")
}

apply(from = "../jacoco.gradle.kts")

android {
    namespace = "com.unatxe.quicklist.data"
    compileSdk = ConfigData.compileSdkVersion

    defaultConfig {
        minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion

        testInstrumentationRunner = "com.unatxe.quicklist.data.CustomTestRunner"
        consumerProguardFiles("consumer-rules.pro")

        ksp {
            this.arg(
                "room.schemaLocation",
                "$projectDir/schemas".toString()
            )
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
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
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.38.1")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.38.1")

    implementation(GeneralDependencies.hilt)
    kapt(GeneralDependencies.hiltCompiler)

    implementation(project(":quicklist-domain"))

    implementation(Room.runtime)
    ksp(Room.compiler)
    implementation(Room.ktx)
    testImplementation(Room.testing)
}
