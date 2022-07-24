package com.unatxe.quicklist.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

object NavigationDirections {

    val mainScreen  = object : NavigationCommand {

        override val arguments = emptyList<NamedNavArgument>()

        override val destination = "mainScreen"

    }

    object ListScreen {
        const val KEY_LIST_ID = "idList"
        const val route = "listScreen?$KEY_LIST_ID={$KEY_LIST_ID}"
        const val NO_VALUE = 0
        val listArguments = listOf(
            navArgument(KEY_LIST_ID) { defaultValue = 0; type = NavType.IntType }
        )

        fun listScreen(
            listId: Int? = null
        ) = object : NavigationCommand {

            override val arguments = listArguments

            override val destination = if (listId == null) "listScreen?$KEY_LIST_ID=$NO_VALUE" else "listScreen?$KEY_LIST_ID=$listId"
        }
    }

    val Default = object : NavigationCommand {

        override val arguments = emptyList<NamedNavArgument>()

        override val destination = ""

    }
}
