plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.unatxe.test"
    compileSdk = ConfigData.compileSdkVersion

    defaultConfig {
        applicationId = "com.unatxe.test"
        minSdk = 22
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(GeneralDependencies.androidXCore)
    implementation(GeneralDependencies.androidXLifecycle)
    implementation(Compose.activityCompose)
    implementation(Compose.ui)
    implementation(Compose.uiTollingPreview)
    implementation(Compose.material3)
    implementation("androidx.compose.material:material:1.5.3")
    testImplementation(Testing.jUnit)
    androidTestImplementation(Testing.extJUnit)
    androidTestImplementation(Testing.espressoCore)
    androidTestImplementation(Compose.uiTestJunit)
    debugImplementation(Compose.uiTolling)
    debugImplementation(Compose.uiTestManifest)
}

sonarqube {
    isSkipProject = true
}
