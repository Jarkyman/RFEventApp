package com.example.rfeventapp.android.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.rfeventapp.android.ui.login.ForgotScreen
import com.example.rfeventapp.android.ui.login.LoginScreen
import com.example.rfeventapp.android.ui.login.SignUpScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.Login.route
    ) {
        composable(route = AuthScreen.Login.route) {
            LoginScreen(
                navController = navController
            )
        }
        composable(route = AuthScreen.SignUp.route) {
            SignUpScreen(
                navController = navController
            )
        }
        composable(route = AuthScreen.Forgot.route) {
            ForgotScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen(route = "LOGIN")
    object SignUp : AuthScreen(route = "SIGN_UP")
    object SignOut : AuthScreen(route = "SIGN_OUT")
    object Forgot : AuthScreen(route = "FORGOT")
}