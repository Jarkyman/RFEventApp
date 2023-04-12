package com.example.rfeventapp.android.navigation

import com.example.rfeventapp.android.R


sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int,
) {
    object Map: BottomBarScreen(
        route = "MAP",
        title = "Map",
        icon = R.drawable.map
    )
    object Home: BottomBarScreen(
        route = "HOME",
        title = "Home",
        icon = R.drawable.users
    )
    object Calendar: BottomBarScreen(
        route = "CALENDAR",
        title = "Calendar",
        icon = R.drawable.calendar
    )
    object Profile: BottomBarScreen(
        route = "PROFILE",
        title = "Profile",
        icon = R.drawable.profile
    )
}