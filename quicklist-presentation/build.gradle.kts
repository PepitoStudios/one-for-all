plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    // id 'org.jmailen.kotlinter'
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.unatxe.quicklist"
    compileSdk = ConfigData.compileSdkVersion

    defaultConfig {
        applicationId = "com.unatxe.quicklist"
        minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion
        versionCode = ConfigData.versionCode
        versionName = ConfigData.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        register("release") {
            val path = projectDir.getAbsolutePath() + "/quicklist-release-sign.json"
            val file = File(path)
            val jsonFile = groovy.json.JsonSlurper()
                .parseText(file.readText()) as Map<String, String>
            storeFile = file("quicklist-release-sign.jks")
            storePassword = jsonFile["storePassword"]
            keyAlias = jsonFile["keyAlias"]
            keyPassword = jsonFile["keyPassword"]
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }

    hilt {
        enableAggregatingTask = true
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
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    dependenciesInfo {
        includeInApk = true
        includeInBundle = true
    }
    buildToolsVersion = ConfigData.buildToolsVersion
}

dependencies {

    implementation(GeneralDependencies.androidXCore)
    implementation(GeneralDependencies.androidXLifecycle)
    implementation(GeneralDependencies.androidXLifecycleLiveData)
    implementation(GeneralDependencies.androidXSplashScreen)
    implementation(GeneralDependencies.coroutinesAndroid)

    testImplementation(Testing.jUnit)
    androidTestImplementation(Testing.extJUnit)
    androidTestImplementation(Testing.espressoCore)

    // Import the BoM for the Firebase platform
    implementation(platform(Firebase.bom))

    implementation(Firebase.crashlytics)
    implementation(Firebase.analytics)
    implementation(Firebase.performance)

    implementation(Compose.ui)
    implementation(Compose.uiTollingPreview)
    debugImplementation(Compose.uiTolling)
    debugImplementation(Compose.uiTestManifest)
    androidTestImplementation(Compose.uiTestJunit)
    implementation(Compose.material3)
    implementation(Compose.activityCompose)
    implementation(Compose.viewModelCompose)
    implementation(Compose.runtimeLiveData)

    implementation(GeneralDependencies.hilt)
    kapt(GeneralDependencies.hiltCompiler)

    implementation(project(":quicklist-domain"))
    implementation(project(":quicklist-data"))

    implementation(Room.runtime)
}

kapt {
    correctErrorTypes = true
}

task<Wrapper>("wrapper") {
    gradleVersion = ConfigData.gradle
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = ConfigData.jvmVersion
        freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }
}

tasks.register("prepareKotlinBuildScriptModel") {}

tasks.register("makeTest") {
    dependsOn("testDebugUnitTest")
    dependsOn("connectedDebugAndroidTest")
}
