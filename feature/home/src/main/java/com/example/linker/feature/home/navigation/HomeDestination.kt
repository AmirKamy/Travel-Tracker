package com.example.linker.feature.home.navigation

sealed class HomeDestination(val route: String) {
    data object HomeScreen : HomeDestination("home_screen")
}