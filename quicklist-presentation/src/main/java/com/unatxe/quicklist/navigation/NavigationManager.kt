package com.unatxe.quicklist.navigation
import com.unatxe.quicklist.navigation.NavigationDirections.Default
import kotlinx.coroutines.flow.MutableStateFlow

class NavigationManager {

    var commands = MutableStateFlow(Default)

    fun navigate(
        directions: NavigationCommand
    ) {
        commands.value = directions

    }

    fun clear() {
        commands.value = Default
    }

}
