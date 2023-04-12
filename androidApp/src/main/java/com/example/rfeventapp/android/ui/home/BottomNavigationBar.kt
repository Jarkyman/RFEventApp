package com.example.rfeventapp.android

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.rfeventapp.android.navigation.HomeNavGraph
import com.example.rfeventapp.android.navigation.BottomBarScreen
import com.example.rfeventapp.utils.AppColor

@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
            )
        }
    ) {paddingValues ->
        HomeNavGraph(navController = navController, paddingValues = paddingValues)
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
) {
    val items = listOf(
        BottomBarScreen.Map,
        BottomBarScreen.Home,
        BottomBarScreen.Calendar,
        BottomBarScreen.Profile
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    val isBottomBarDestination = items.any { it.route == currentDestination?.route }
    if (isBottomBarDestination) {
        BottomAppBar(
            backgroundColor = Color(AppColor.darkGray),
            modifier = Modifier.height(80.dp),
            elevation = 5.dp
        ) {
            items.forEach { item ->
                val selected = currentDestination?.route == item.route
                BottomNavigationItem(
                    icon = {
                        Column(horizontalAlignment = CenterHorizontally) {
                            Icon(
                                painterResource(id = item.icon),
                                contentDescription = item.title,
                            )
                        }
                    },
                    label = {
                        Text(
                            text = item.title,
                            fontSize = 9.sp
                        )
                    },
                    selectedContentColor = Color(AppColor.orange),
                    unselectedContentColor = Color(AppColor.lightGray),
                    alwaysShowLabel = true,
                    selected = selected,
                    onClick = {
                        navController.navigate(item.route) {

                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        }
    }

}