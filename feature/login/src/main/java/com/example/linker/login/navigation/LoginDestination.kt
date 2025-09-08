package com.example.linker.login.navigation

sealed class LoginDestination(val route: String) {
    data object LoginScreen : LoginDestination("login_screen")
}