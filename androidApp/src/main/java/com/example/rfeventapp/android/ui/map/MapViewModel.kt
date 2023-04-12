package com.example.rfeventapp.android.ui.map

import androidx.compose.animation.core.AnimationState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapViewModel: ViewModel() {

    private val _expanded = MutableStateFlow(false)
    val expanded = _expanded.asStateFlow()
    private val _rotationState = AnimationState(if (_expanded.value) 180f else 0f)
    val rotationState = _rotationState

    fun toggleExpanded() {
        viewModelScope.launch {
            _expanded.value = !_expanded.value
        }
    }

}