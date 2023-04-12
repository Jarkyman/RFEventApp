package com.example.rfeventapp.android.ui.profile

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rfeventapp.android.firebase.FirebaseAuthRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditPassViewModel: ViewModel() {
    val authService = FirebaseAuthRepo() //TODO: Dep inject

    private val _passwordOld = MutableStateFlow("")
    val passwordOld = _passwordOld.asStateFlow()
    private val _passwordNew = MutableStateFlow("")
    val passwordNew = _passwordNew.asStateFlow()
    private val _passwordNewConfirm = MutableStateFlow("")
    val passwordNewConfirm = _passwordNewConfirm.asStateFlow()

    private val _isPasswordOldVisible = MutableStateFlow(false)
    var isPasswordOldVisible = _isPasswordOldVisible.asStateFlow()
    private val _isPasswordNewVisible = MutableStateFlow(false)
    var isPasswordNewVisible = _isPasswordNewVisible.asStateFlow()
    private val _isPasswordNewConfirmVisible = MutableStateFlow(false)
    var isPasswordNewConfirmVisible = _isPasswordNewConfirmVisible.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun updatePasswordOld(input: String) {
        viewModelScope.launch {
            _passwordOld.value = input
        }
    }

    fun updatePasswordNew(input: String) {
        viewModelScope.launch {
            _passwordNew.value = input
        }
    }

    fun updatePasswordNewConfirm(input: String) {
        viewModelScope.launch {
            _passwordNewConfirm.value = input
        }
    }

    fun passwordOldVisibleToggle() {
        viewModelScope.launch {
            _isPasswordOldVisible.value = !_isPasswordOldVisible.value
        }
    }

    fun passwordNewVisibleToggle() {
        viewModelScope.launch {
            _isPasswordNewVisible.value = !_isPasswordNewVisible.value
        }
    }

    fun passwordNewConfirmVisibleToggle() {
        viewModelScope.launch {
            _isPasswordNewConfirmVisible.value = !_isPasswordNewConfirmVisible.value
        }
    }

    fun savePassword(onClick: () -> Unit, context: Context) {
        viewModelScope.launch {
            _loading.value = true
            val authUser = authService.updatePassword(
                passwordOld.value, passwordNew.value, passwordNewConfirm.value
            )
            if (authUser.first) {
                onClick()
            } else {
                Toast.makeText(context, authUser.second, Toast.LENGTH_LONG).show()
                _loading.value = false
            }
        }
    }
}