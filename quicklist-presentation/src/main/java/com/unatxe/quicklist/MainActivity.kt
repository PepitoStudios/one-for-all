package com.unatxe.quicklist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.unatxe.quicklist.features.mainScreen.MainScreen
import com.unatxe.quicklist.features.mainScreen.listScreen.ListScreen
import com.unatxe.quicklist.navigation.NavigationDirections
import com.unatxe.quicklist.navigation.NavigationManager
import com.unatxe.quicklist.ui.theme.One4allTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationManager: NavigationManager

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            navigationManager.commands.collectAsState().value.also { command ->
                if (command.destination.isNotEmpty()) navController.navigate(command.destination)

                navigationManager.clear()
            }
            One4allTheme {
                // A surface container using the 'background' color from the theme
                Surface() {
                    NavHost(navController = navController, startDestination = NavigationDirections.mainScreen.destination) {

                        composable(NavigationDirections.mainScreen.destination) { MainScreen()  }

                        composable(
                            NavigationDirections.ListScreen.route,
                            arguments = NavigationDirections.ListScreen.listArguments
                        ) { backStackEntry ->
                            assert(backStackEntry.arguments != null)
                            ListScreen(backStackEntry.arguments!!.getInt(NavigationDirections.ListScreen.KEY_LIST_ID))
                        }
                    }
                }
            }
        }
    }
}
