package com.example.rfeventapp.android.ui.login

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.rfeventapp.android.navigation.RootNavigationGraph

@Composable
fun SignOutScreen() {
    RootNavigationGraph(navController = rememberNavController())
}