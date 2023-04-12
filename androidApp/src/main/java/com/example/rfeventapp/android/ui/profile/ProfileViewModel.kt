package com.example.rfeventapp.android.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rfeventapp.android.firebase.FirebaseAuthRepo
import com.example.rfeventapp.usecase.loggedInUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {
    val authService = FirebaseAuthRepo() //TODO: Dep inject

    private val _showLanguageDialog = MutableStateFlow(false)
    val showLanguageDialog = _showLanguageDialog.asStateFlow()
    private val _showNotificationDialog = MutableStateFlow(false)
    val showNotificationDialog = _showNotificationDialog.asStateFlow()
    private val _notificationReminder = MutableStateFlow(true)
    val notificationReminder = _notificationReminder.asStateFlow()
    private val _notificationNews = MutableStateFlow(true)
    val notificationNews = _notificationNews.asStateFlow()
    private val _notificationChanges = MutableStateFlow(true)
    val notificationChanges = _notificationChanges.asStateFlow()


    fun toggleLanguageDialog() {
        viewModelScope.launch {
            _showLanguageDialog.value = !_showLanguageDialog.value
        }
    }

    fun toggleNotificationDialog() {
        viewModelScope.launch {
            _showNotificationDialog.value = !_showNotificationDialog.value
        }
    }

    fun toggleNotificationReminder() {
        viewModelScope.launch {
            _notificationReminder.value = !_notificationReminder.value
        }
    }

    fun toggleNotificationNews() {
        viewModelScope.launch {
            _notificationNews.value = !_notificationNews.value
        }
    }
    fun toggleNotificationChanges() {
        viewModelScope.launch {
            _notificationChanges.value = !_notificationChanges.value
        }
    }

    fun campNameCheck(campName: String): String {
        var result = "No Camp"
        if (campName.isNotEmpty()) {
            result = campName
        }
        return result
    }

}