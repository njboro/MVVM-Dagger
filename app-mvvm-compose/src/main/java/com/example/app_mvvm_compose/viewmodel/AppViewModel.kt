package com.example.app_mvvm_compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_mvvm_compose.model.Desh
import com.example.app_mvvm_compose.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val repository: CountryRepository
)  : ViewModel() {

    private val _uiSate = MutableStateFlow<UIState<List<Desh>>>(UIState.Loading)
    val uiState : StateFlow<UIState<List<Desh>>> = _uiSate

    init {
        loadCountries()
    }

    fun loadCountries() {
        viewModelScope.launch {
            _uiSate.value = UIState.Loading
            _uiSate.value = repository.getCountryList()
        }
    }
}

sealed class UIState<out T> {
    object Loading : UIState<Nothing>()
    data class Success<T>(val data : T) : UIState<T> ()
    data class Error(val message: String) : UIState<Nothing> ()
}