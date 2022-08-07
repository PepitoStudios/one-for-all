package com.unatxe.quicklist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.unatxe.quicklist.features.listScreen.ListScreen
import com.unatxe.quicklist.features.listScreen.ListViewModel
import com.unatxe.quicklist.features.mainScreen.MainScreen
import com.unatxe.quicklist.features.mainScreen.MainViewModel
import com.unatxe.quicklist.navigation.NavigationDirections
import com.unatxe.quicklist.navigation.NavigationManager
import com.unatxe.quicklist.ui.theme.One4allTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationManager: NavigationManager

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberAnimatedNavController()

            navigationManager.commands.collectAsState().value.also { command ->
                if (command.destination.isNotEmpty()) navController.navigate(command.destination)

                navigationManager.clear()
            }

            One4allTheme {
                // A surface container using the 'background' color from the theme
                Surface() {
                     NormalFlow(navController)
                    //TestFlow(navController = navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@ExperimentalCoroutinesApi
@Composable
private fun MainActivity.TestFlow(navController: NavHostController) {
    val navigationCommand = NavigationDirections.ListScreen.listScreen(1)
    AnimatedNavHost(
        navController = navController,
        // startDestination = NavigationDirections.mainScreen.destination
        startDestination = "listScreen?idList=1"
    ) {
        composable(
            navigationCommand.destination,
            arguments = listOf(
                navArgument(
                    NavigationDirections.ListScreen.KEY_LIST_ID
                ) {
                    defaultValue = 1
                    type = NavType.IntType
                }
            )

        ) { backStackEntry ->
            assert(backStackEntry.arguments != null)
            ListScreen(hiltViewModel<ListViewModel>())
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@ExperimentalCoroutinesApi
@Composable
private fun MainActivity.NormalFlow(navController: NavHostController) {
    AnimatedNavHost(
        navController = navController,
        startDestination = NavigationDirections.mainScreen.destination
    ) {
        composable(
            NavigationDirections.mainScreen.destination,
            enterTransition = {
                when (initialState.destination.route) {
                    NavigationDirections.ListScreen.route ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    NavigationDirections.ListScreen.route ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    NavigationDirections.ListScreen.route ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    NavigationDirections.ListScreen.route ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else -> null
                }
            }
        ) {
            MainScreen(hiltViewModel<MainViewModel>())
        }

        composable(
            NavigationDirections.ListScreen.route,
            arguments = NavigationDirections.ListScreen.listArguments,
            enterTransition = {
                when (initialState.destination.route) {
                    NavigationDirections.mainScreen.destination ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    NavigationDirections.mainScreen.destination ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    NavigationDirections.mainScreen.destination ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    NavigationDirections.mainScreen.destination ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    else -> null
                }
            }
        ) { backStackEntry ->
            assert(backStackEntry.arguments != null)
            ListScreen(hiltViewModel<ListViewModel>())
        }
    }
}
