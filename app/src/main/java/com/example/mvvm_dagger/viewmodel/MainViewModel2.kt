package com.example.mvvm_dagger.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mvvm_dagger.repository.CountryRepository
import javax.inject.Inject

class MainViewModel2 @Inject constructor(private val repository: CountryRepository, testForMultiBinding: TestForMultiBinding) : ViewModel() {

}