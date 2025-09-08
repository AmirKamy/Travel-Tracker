package com.example.linker.register.navigation

sealed class RegisterDestination(val route: String) {
    data object RegisterScreen : RegisterDestination("register_screen")
}