package com.example.app_mvvm_compose.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {
    private var _count = MutableStateFlow(0)

    val count : StateFlow<Int>
    get() = _count

    fun incrementNumber() {
        _count.value = _count.value + 1
    }
}