package com.example.huertohogar_app.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.huertohogar_app.ui.feature_auth.AuthViewModel
import com.example.huertohogar_app.ui.feature_auth.LoginScreen
import com.example.huertohogar_app.ui.feature_auth.RegisterScreen

sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")
    object Home : Screen("home_screen")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onRegisterClick = { navController.navigate(Screen.Register.route) }
                onLoginSuccess = { navController.navigate(Screen.Home.route)}{popUpTo(Screen.Login.route) { inclusive = true; saveState = true; }
                    launchSingleTop = true; restoreState = true; })}
        }
        composable(route = Screen.Register.route)
        {
            RegisterScreen(viewModel = authViewModel,
                onRegisterSuccess = { navController.popBackStack(); navController.navigate(Screen.Home.route) }) {
                navController.navigate(Screen.Login.route)
            }
    }
            composable(route = Screen.Home.route)
            {
                HomeScreen()
            }
}