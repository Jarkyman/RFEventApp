package com.example.rfeventapp.android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rfeventapp.android.HomeScreen
import com.example.rfeventapp.android.ui.splash.SplashScreen

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.SPLASH
    ) {
        authNavGraph(navController = navController)
        composable(route = Graph.HOME) {
            HomeScreen()
        }
        composable(route = Graph.SPLASH){
            SplashScreen(
                navController = navController
            )
        }
    }
}

object Graph {
    const val SPLASH = "splash_graph"
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "home_graph"
    const val EVENT_DETAILS = "details_graph"
    const val EDIT = "edit"
    const val PASS = "pass"
}