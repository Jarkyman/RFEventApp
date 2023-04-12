package com.example.rfeventapp.android.ui.login

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.rfeventapp.android.firebase.FirebaseAuthRepo
import com.example.rfeventapp.android.navigation.Graph
import com.example.rfeventapp.domain.User
import com.example.rfeventapp.utils.MAX_INPUT_LETTERS
import com.example.rfeventapp.utils.Validation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import com.example.rfeventapp.utils.Result

class SignUpViewModel(
    //private val firebaseAuth: FirebaseAuthService
) : ViewModel() {
    val firebaseAuth = FirebaseAuthRepo() //TODO: Dep inject

    private val _firstName = MutableStateFlow("")
    val firstName = _firstName.asStateFlow()
    private val _lastName = MutableStateFlow("")
    val lastName = _lastName.asStateFlow()
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()
    private val _campName = MutableStateFlow("")
    val campName = _campName.asStateFlow()
    private val _birthday = MutableStateFlow(TextFieldValue())
    val birthday = _birthday.asStateFlow()
    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()
    private val _passwordConfirm = MutableStateFlow("")
    val passwordConfirm = _passwordConfirm.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _isPasswordVisible = MutableStateFlow(false)
    var isPasswordVisible = _isPasswordVisible.asStateFlow()

    private val _isPasswordConfirmVisible = MutableStateFlow(false)
    var isPasswordConfirmVisible = _isPasswordConfirmVisible.asStateFlow()

    fun updateFirstName(input: String) {
        viewModelScope.launch {
            if(Validation().checkString(input) == Result.Success) {
                _firstName.value = input
            }
        }
    }

    fun updateLastName(input: String) {
        viewModelScope.launch {
            if(Validation().checkString(input) == Result.Success) {
                _lastName.value = input
            }
        }
    }

    fun updateEmail(input: String) {
        viewModelScope.launch {
            if(Validation().checkStringWithoutSpace(input) == Result.Success) {
                _email.value = input
            }
        }
    }

    fun updateCampName(input: String) {
        viewModelScope.launch {
            if(Validation().checkString(input) == Result.Success && input.length <= MAX_INPUT_LETTERS) {
                _campName.value = input
            }
        }
    }

    fun updateBirthday(input: TextFieldValue) {
        viewModelScope.launch {
            _birthday.value = input
        }
    }

    fun updatePassword(input: String) {
        viewModelScope.launch {
            if(Validation().checkStringWithoutSpace(input) == Result.Success) {
                _password.value = input
            }
        }
    }

    fun updatePasswordConfirm(input: String) {
        viewModelScope.launch {
            if(Validation().checkStringWithoutSpace(input) == Result.Success) {
                _passwordConfirm.value = input
            }
        }
    }

    fun passwordVisibleToggle() {
        viewModelScope.launch {
            _isPasswordVisible.value = !_isPasswordVisible.value
        }
    }

    fun passwordConfirmVisibleToggle() {
        viewModelScope.launch {
            _isPasswordConfirmVisible.value = !_isPasswordConfirmVisible.value
        }
    }

    fun handleSignUp(navController: NavController, context: Context) {
        val newUser = User(
            firstName = firstName.value,
            lastName = lastName.value,
            email = email.value,
            campName = campName.value,
            birthday = birthday.value.text,
            createdAt = LocalDateTime.now().toString(),
            updatedAt = LocalDateTime.now().toString()
        )
        viewModelScope.launch {
            _loading.value = true
            val createUser = firebaseAuth.createUser(newUser, password.value, passwordConfirm.value)
            if (createUser.first) {
                navController.navigate(Graph.HOME) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            } else {
                Toast.makeText(context, createUser.second, Toast.LENGTH_LONG).show()
                _loading.value = false
            }
        }
    }
}