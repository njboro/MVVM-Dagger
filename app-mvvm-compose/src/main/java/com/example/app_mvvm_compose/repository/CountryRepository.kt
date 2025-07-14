package com.example.app_mvvm_compose.repository

import com.example.app_mvvm_compose.db.CountryDB
import com.example.app_mvvm_compose.model.Desh
import com.example.app_mvvm_compose.retrofit.CountriesApi
import com.example.app_mvvm_compose.viewmodel.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class CountryRepository @Inject constructor(private val countriesApi: CountriesApi,
                        private val countryDB: CountryDB) {

    private val _countries = MutableStateFlow<List<Desh>>(
        value = listOf()
    )

    val countries : StateFlow<List<Desh>>
        get() = _countries

    //Suspend function to call API
    suspend fun getCountries() {
        val result = countriesApi.getCountries()
        if (result.body() != null) {
            result.body()?.let {
                body ->
                    countryDB.getCountryDAO().addCountries(body)
                    _countries.value = body
            }
        }
    }

    suspend fun getCountryList(): UIState<List<Desh>> = try {
        val countries = countriesApi.getCountries().body()
        UIState.Success(countries)
    } catch (e : Exception) {
        UIState.Error("Failed to load posts: ${e.message}")
    } as UIState<List<Desh>>

}