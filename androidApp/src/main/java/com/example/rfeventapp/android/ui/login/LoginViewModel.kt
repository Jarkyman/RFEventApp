package com.example.rfeventapp.android.ui.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.rfeventapp.android.firebase.FirebaseAuthRepo
import com.example.rfeventapp.android.navigation.Graph
import com.example.rfeventapp.utils.Validation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.rfeventapp.utils.Result

class LoginViewModel(
    //private val firebaseAuth: FirebaseAuthService
) : ViewModel() {
    val firebaseAuth = FirebaseAuthRepo() //TODO: Dep inject

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()
    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()
    private val _isPasswordVisible = MutableStateFlow(false)
    var isPasswordVisible = _isPasswordVisible.asStateFlow()

    fun updateEmail(input: String) {
        viewModelScope.launch {
            if (Validation().checkStringWithoutSpace(input) == Result.Success) {
                _email.value = input
            }
        }
    }

    fun updatePassword(input: String) {
        viewModelScope.launch {
            if (Validation().checkStringWithoutSpace(input) == Result.Success) {
                _password.value = input
            }
        }
    }

    fun passwordVisibleToggle() {
        viewModelScope.launch {
            _isPasswordVisible.value = !_isPasswordVisible.value
        }
    }

    fun handleLogin(
        navController: NavController,
        context: Context
    ) {
        viewModelScope.launch {
            val loginUser = firebaseAuth.loginUser(email.value, password.value)

            println("LOGIN ???? " + loginUser)
            if (loginUser.first) {
                navController.navigate(Graph.HOME) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            } else {
                Toast.makeText(context, loginUser.second, Toast.LENGTH_LONG).show();
            }
        }
    }
}