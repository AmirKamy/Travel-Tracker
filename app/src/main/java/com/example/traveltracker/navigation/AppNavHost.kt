package com.example.traveltracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.linker.feature.home.MapRoute
import com.example.linker.feature.home.navigation.HomeDestination
import com.example.linker.login.LoginRoute
import com.example.linker.login.navigation.LoginDestination
import com.example.linker.register.RegisterRoute
import com.example.linker.register.navigation.RegisterDestination
import com.example.traveltracker.welcomescreen.WelcomeDestination
import com.example.traveltracker.welcomescreen.WelcomeScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = WelcomeDestination.WelcomeScreen.route
    ) {

        composable(LoginDestination.LoginScreen.route) {
            LoginRoute(
                onLoggedIn = {
                    navController.navigate(HomeDestination.HomeScreen.route) {
                        popUpTo(
                            WelcomeDestination.WelcomeScreen.route
                        ) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(RegisterDestination.RegisterScreen.route) {
                        {
                            popUpTo(
                                WelcomeDestination.WelcomeScreen.route
                            ) { inclusive = true }
                        }
                    }
                })
        }
        composable(RegisterDestination.RegisterScreen.route) {
            RegisterRoute(
                onRegistered = {
                    navController.navigate(LoginDestination.LoginScreen.route) {
                        popUpTo(
                            WelcomeDestination.WelcomeScreen.route
                        )
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(LoginDestination.LoginScreen.route) {
                        {
                            popUpTo(
                                WelcomeDestination.WelcomeScreen.route
                            ) { inclusive = true }
                        }
                    }
                })
        }

        composable(WelcomeDestination.WelcomeScreen.route) {
            WelcomeScreen(
                onLogin = { navController.navigate(LoginDestination.LoginScreen.route) },
                onRegister = { navController.navigate(RegisterDestination.RegisterScreen.route) }
            )
        }

        composable(HomeDestination.HomeScreen.route) {
            MapRoute(onLogout = {
                navController.navigate(WelcomeDestination.WelcomeScreen.route) { popUpTo(0) }
            })
        }
    }
}
