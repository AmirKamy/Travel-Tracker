package com.example.traveltracker

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.traveltracker.navigation.AppNavHost
import com.example.traveltracker.navigation.TopLevelDestination


@Composable
fun LinkerApp() {
    val navController = rememberNavController()
    // no need bottom bar
    AppNavHost(navController = navController)
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        TopLevelDestination.entries.forEach { destination ->
            NavigationBarItem(
                icon = { Icon(imageVector = destination.selectedIcon, contentDescription = null) },
                label = { Text(text = stringResource(id = destination.titleTextId)) },
                selected = currentRoute == destination.route,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}