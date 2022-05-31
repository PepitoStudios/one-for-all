package com.unatxe.quicklist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.unatxe.quicklist.features.mainScreen.MainScreen
import com.unatxe.quicklist.ui.theme.One4allTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        /*DaggerDataComponent.builder().context(applicationContext)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    applicationContext,
                    DataModuleDependencies::class.java
                ),
                EntryPointAccessors.fromApplication(
                    applicationContext,
                    DomainModuleDependencies::class.java
                )
            ).build().inject(this)*/

        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            One4allTheme {
                // A surface container using the 'background' color from the theme
                Surface() {
                    MainScreen()
                }
            }
        }
    }
}
