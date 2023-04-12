package com.example.rfeventapp.android.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rfeventapp.android.firebase.FirebaseAuthRepo
import com.example.rfeventapp.android.ui.calender.CalenderScreen
import com.example.rfeventapp.android.ui.event.CreateEventScreen
import com.example.rfeventapp.android.ui.event.CreateEventViewModel
import com.example.rfeventapp.android.ui.event.MyEventsScreen
import com.example.rfeventapp.android.ui.home.EventDetailScreen
import com.example.rfeventapp.android.ui.home.EventDetailViewModel
import com.example.rfeventapp.android.ui.home.StartScreen
import com.example.rfeventapp.android.ui.login.SignOutScreen
import com.example.rfeventapp.android.ui.map.MapScreen
import com.example.rfeventapp.android.ui.profile.EditPassScreen
import com.example.rfeventapp.android.ui.profile.EditProfileScreen
import com.example.rfeventapp.android.ui.profile.ProfileScreen
import com.example.rfeventapp.domain.Event
import com.example.rfeventapp.usecase.selectedEvent
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun HomeNavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = BottomBarScreen.Home.route
    ) {
        authNavGraph(navController = navController)
        composable(route = BottomBarScreen.Map.route) {
            MapScreen(
                paddingValues = paddingValues,
                //name = BottomBarScreen.Map.route,
            )
        }
        composable(route = BottomBarScreen.Home.route) {
            StartScreen(
                paddingValues = paddingValues,
                //name = BottomBarScreen.Home.route,
                onEventClick = {
                    println(it.toJson())
                    selectedEvent = it
                    navController.navigate(EventScreen.EventDetail.route)
                }
            )
        }
        composable(route = BottomBarScreen.Calendar.route) {
            CalenderScreen(
                paddingValues = paddingValues,
                //name = BottomBarScreen.Calendar.route,
                onEventClick = {
                    selectedEvent = it
                    navController.navigate(EventScreen.EventDetail.route)
                }
            )
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(
                paddingValues = paddingValues,
                //name = BottomBarScreen.Profile.route,
                onCreateEventClick = {
                    navController.navigate(EventScreen.EventCreate.route)
                },
                onMyEventsClick = {
                    navController.navigate(EventScreen.MyEvents.route)
                },
                onEditClick = {
                    navController.navigate(Graph.EDIT)
                },
                onEditPassClick = {
                    navController.navigate(Graph.PASS)
                },
                onSignOutClick = {
                    FirebaseAuthRepo().logoutUser()

                    navController.navigate(AuthScreen.SignOut.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = AuthScreen.SignOut.route) {
            SignOutScreen()
        }
        composable(
            route = EventScreen.EventDetail.route,
        ) {
            EventDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = getViewModel<EventDetailViewModel>(
                    parameters = { parametersOf(selectedEvent) }
                )
            )
        }
        composable(
            route = EventScreen.EventCreate.route + "{edit}",
            arguments = listOf(navArgument("edit") {
                type = NavType.BoolType
            })
        ) {
            val bool = it.arguments?.getBoolean("edit")
            CreateEventScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = getViewModel<CreateEventViewModel>(
                    parameters = { parametersOf(selectedEvent) }
                )
            )
        }
        composable(
            route = EventScreen.EventCreate.route,
        ) {
            CreateEventScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = getViewModel<CreateEventViewModel>(
                    parameters = { parametersOf(Event()) }
                )
            )
        }
        composable(route = EventScreen.MyEvents.route) {
            MyEventsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onEventClick = {
                    selectedEvent = it
                    navController.navigate(EventScreen.EventCreate.route + true)
                }
            )
        }
        editNavGraph(navController = navController)
        passNavGraph(navController = navController)
    }
}

sealed class EventScreen(val route: String) {
    object EventCreate : EventScreen(route = "EVENT_CREATE")
    object EventDetail : EventScreen(route = "EVENT_DETAIL")
    object MyEvents : EventScreen(route = "MY_EVENTS")
}

fun NavGraphBuilder.editNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.EDIT,
        startDestination = EditScreen.Edit.route
    ) {
        composable(route = EditScreen.Edit.route) {
            EditProfileScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = {}
            )
        }
    }
}

sealed class EditScreen(val route: String) {
    object Edit : EditScreen(route = "EDIT")
}

fun NavGraphBuilder.passNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.PASS,
        startDestination = PassScreen.Pass.route
    ) {
        composable(route = PassScreen.Pass.route) {
            EditPassScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = {}
            )
        }
    }
}

sealed class PassScreen(val route: String) {
    object Pass : PassScreen(route = "PASS")
}