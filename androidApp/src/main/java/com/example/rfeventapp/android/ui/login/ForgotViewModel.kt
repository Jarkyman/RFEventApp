package com.example.rfeventapp.android.ui.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rfeventapp.android.firebase.FirebaseAuthRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForgotViewModel: ViewModel() {
    val firebaseAuth = FirebaseAuthRepo() //TODO: Dep inject

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    fun updateEmail(input: String) {
        viewModelScope.launch {
            _email.value = input
        }
    }

    fun handleForgot(context: Context) {
        viewModelScope.launch {
            val response = firebaseAuth.forgotPassword(_email.value)
            Toast.makeText(context, response.second, Toast.LENGTH_LONG).show()
        }
    }
}