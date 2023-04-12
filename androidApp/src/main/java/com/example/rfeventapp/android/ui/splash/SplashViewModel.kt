package com.example.rfeventapp.android.ui.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.rfeventapp.android.firebase.FirebaseAuthRepo
import com.example.rfeventapp.android.navigation.Graph
import com.example.rfeventapp.usecase.loggedInUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel: ViewModel() {
    private val authRepo = FirebaseAuthRepo()

    init {
        viewModelScope.launch {

        }
    }

    fun switchPage(navController: NavController) {
        if (authRepo.getCurrentUser() != null) {
            navController.navigate(Graph.HOME) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        }else {
            navController.navigate(Graph.AUTHENTICATION) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        }
    }

    fun handelAppInfo(navController: NavController, context: Context) {
        viewModelScope.launch {
            println("loading user")
            if (authRepo.getCurrentUser() != null) {
                authRepo.getUser()
                //FirebaseEventRepo().postTestEvent()
                println("User -> ${loggedInUser!!.firstName} is found")
                println("loaded user")
            }else {
                println("No user found")
            }
            delay(1000)
            switchPage(navController)
        }
        //authService.logoutUser()
    }
}