import Versions.androidXLifecyle
import Versions.compose
import Versions.composeMaterial
import Versions.composeNavigationAnimation

object Compose {
    const val ui = "androidx.compose.ui:ui:$compose"
    const val uiTollingPreview = "androidx.compose.ui:ui-tooling-preview:$compose"
    const val uiTolling = "androidx.compose.ui:ui-tooling:$compose"
    const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:$compose"
    const val uiTestJunit = "androidx.compose.ui:ui-test-junit4:$compose"
    const val runtimeLiveData = "androidx.compose.runtime:runtime-livedata:$compose"
    const val material3 = "androidx.compose.material3:material3:$composeMaterial"
    const val activityCompose = "androidx.activity:activity-compose:1.7.2"
    const val hiltCompose = "androidx.hilt:hilt-navigation-compose:1.0.0"
    const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:$androidXLifecyle"
    const val animationNavigationCompose = "com.google.accompanist:accompanist-navigation-animation:$composeNavigationAnimation"

}

object Firebase {
    const val bom = "com.google.firebase:firebase-bom:${Versions.firebase}"
    const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    const val analytics = "com.google.firebase:firebase-analytics-ktx"
    const val performance = "com.google.firebase:firebase-perf-ktx"
}

object BuildPlugins {
    const val android = "com.android.tools.build:gradle:${Versions.gradle}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val crashlytics = "com.google.firebase:firebase-crashlytics-gradle:" +
        Versions.crashlyticsGradle
    const val firebasePerf = "com.google.firebase:perf-plugin:${Versions.performanceGradle}"
    const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltGradle}"
    const val googleServices = "com.google.gms:google-services:${Versions.googleServices}"
}

object Testing {
    const val jUnit = "junit:junit:${Versions.junit}"
    const val extJUnit = "androidx.test.ext:junit:${Versions.extJunit}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.expressoCore}"
}

object GeneralDependencies {
    const val androidXCore = "androidx.core:core-ktx:${Versions.androidXCore}"
    const val androidXLifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:" +
        Versions.androidXLifecyle // ktlint-disable max-line-length
    const val androidXLifecycleLiveData = "androidx.lifecycle:lifecycle-livedata-ktx:" +
        Versions.androidXLifecyle
    const val androidXSplashScreen = "androidx.core:core-splashscreen:" +
        Versions.androidXSplashScreen
    const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCore = "com.google.dagger:hilt-core:${Versions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:" +
        Versions.coroutines
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:" +
        Versions.coroutines

    const val androidXNavigationCompose = "androidx.navigation:navigation-compose:" + Versions.androidXNavitationCompose
    const val androidXHiltNavigationCompose =  "androidx.hilt:hilt-navigation-compose:${Versions.androidXHiltNavitationCompose}"
}

object Room {
    const val runtime = "androidx.room:room-runtime:${Versions.room}"
    const val compiler = "androidx.room:room-compiler:${Versions.room}"
    const val ktx = "androidx.room:room-ktx:${Versions.room}"
    const val testing = "androidx.room:room-testing:${Versions.room}"
}
