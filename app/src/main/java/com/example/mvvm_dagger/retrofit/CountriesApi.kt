package com.example.mvvm_dagger.retrofit

import com.example.mvvm_dagger.model.Country
import retrofit2.Response
import retrofit2.http.GET

interface CountriesApi {

    @GET("DevTides/countries/master/countriesV2.json") //this is the end point that we need for this project to get the api response.
    suspend fun getCountries() : Response<List<Country>> //we are using coroutines here so making the function suspend.
}