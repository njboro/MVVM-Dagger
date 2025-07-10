package com.example.mvvm_dagger.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm_dagger.model.Country
import com.example.mvvm_dagger.repository.CountryRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
* viewModel needs repository class object to access the data from the model.
* so we are passing that in the constructor itself
* */
class MainViewModel @Inject constructor(private val repository: CountryRepository,
    private val testForMultiBinding: TestForMultiBinding) : ViewModel() {

    val countriesLiveData : LiveData<List<Country>>
        get() = repository.countries

    val countryLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        loading.value = true
        fetchCountries()
    }

    private fun fetchCountries() {
        viewModelScope.launch {
            repository.getCountries()
            loading.value = false
        }
    }

    /*
    * init method will call right after the viewmodel is created.
    * so all the codes inside the init method will execute first
    * */

    /*init {
        //this is the coroutine that we use to connect the api
        viewModelScope.launch {
            repository.getCountries()
        }
    }*/

}

class TestForMultiBinding @Inject constructor() {
    fun testSomething() {

    }
}