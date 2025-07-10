package com.example.mvvm_dagger.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvm_dagger.db.CountryDB
import com.example.mvvm_dagger.model.Country
import com.example.mvvm_dagger.retrofit.CountriesApi
import javax.inject.Inject

/** Here dagger will check for CountriesApi since we are using constructor injection here
 * and Dagger will provide the object here automatically
 */
class CountryRepository @Inject constructor(private val countriesApi: CountriesApi, private val countryDB: CountryDB) {
    private val _countries = MutableLiveData<List<Country>>() //private member only
    val countries : LiveData<List<Country>>
        get() = _countries

    //Suspend function to call the api
    suspend fun getCountries() {

        /*if (countriesApi.getCountries().body() != null) {
            _countries.postValue(countriesApi.getCountries().body())
        }*/
        val result = countriesApi.getCountries()
        if (result.body() != null) {
            result.body()?.let {
                body ->
                countryDB.getCountryDAO().addCountries(body)
                _countries.postValue(body)
            }
        }
    }
}