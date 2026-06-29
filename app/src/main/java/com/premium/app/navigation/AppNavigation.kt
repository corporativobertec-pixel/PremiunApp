package com.premium.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.premium.app.ui.screens.LoginScreen
import com.premium.app.ui.screens.RegisterScreen
import com.premium.app.ui.screens.WelcomeScreen

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Main : Screen("main") // This will be the screen after login/registration
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Welcome.route) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        // TODO: Implement MainScreen and its nested navigation for BottomNavBar
        composable(Screen.Main.route) {
            // MainScreen will contain the BottomNavBar and its content
            // For now, we'll just put a placeholder
            // Text("Main Screen Content")
        }
    }
}
