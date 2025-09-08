package com.example.traveltracker.welcomescreen

sealed class WelcomeDestination(val route: String) {
    data object WelcomeScreen : WelcomeDestination("welcome_screen")
}