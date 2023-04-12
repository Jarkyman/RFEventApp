package com.example.rfeventapp.android.ui.profile

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rfeventapp.android.firebase.FirebaseAuthRepo
import com.example.rfeventapp.domain.User
import com.example.rfeventapp.usecase.loggedInUser
import com.example.rfeventapp.utils.MAX_INPUT_LETTERS
import com.example.rfeventapp.utils.Result
import com.example.rfeventapp.utils.Validation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class EditProfileViewModel: ViewModel() {
    val authService = FirebaseAuthRepo() //TODO: Dep inject

    private val _firstName = MutableStateFlow(loggedInUser!!.firstName)
    val firstName = _firstName.asStateFlow()
    private val _lastName = MutableStateFlow(loggedInUser!!.lastName)
    val lastName = _lastName.asStateFlow()
    private val _email = MutableStateFlow(loggedInUser!!.email)
    val email = _email.asStateFlow()
    private val _campName = MutableStateFlow(loggedInUser!!.campName)
    val campName = _campName.asStateFlow()
    private val _birthday = MutableStateFlow(TextFieldValue(loggedInUser!!.birthday))
    val birthday = _birthday.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

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

    fun updateProfile(onBackClick: () -> Unit, context: Context) {
        var emailChanged = false
        var nameChange = false

        if (loggedInUser!!.email != email.value) {
            emailChanged = true
        }
        if (loggedInUser!!.firstName != firstName.value || loggedInUser!!.lastName != lastName.value) {
            nameChange = true
        }

        val updatedUser = User(
            id = loggedInUser!!.id,
            createdAt = loggedInUser!!.createdAt,
            firstName = firstName.value.ifEmpty { loggedInUser!!.firstName },
            lastName = lastName.value.ifEmpty { loggedInUser!!.lastName },
            campName = campName.value,
            email = email.value.ifEmpty { loggedInUser!!.email },
            birthday = birthday.value.text.ifEmpty { loggedInUser!!.birthday },
            updatedAt = LocalDateTime.now().toString()
        )
        viewModelScope.launch {
            _loading.value = true
            val updateState = authService.updateUser(updatedUser, emailChanged, nameChange)
            println(updateState)
            if (updateState.first) {
                onBackClick()
            }else {
                Toast.makeText(context, updateState.second, Toast.LENGTH_LONG).show()
                _loading.value = false
            }
        }
    }

    fun getLocaleDateBirthday(): LocalDate {
        val birthdaySplit = loggedInUser!!.birthday.split("/")
        return LocalDate.of(
            birthdaySplit[2].toInt(),
            birthdaySplit[1].toInt(),
            birthdaySplit[0].toInt()
        )
    }

}