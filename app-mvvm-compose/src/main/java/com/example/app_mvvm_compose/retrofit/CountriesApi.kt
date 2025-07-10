package com.example.app_mvvm_compose.retrofit

import com.example.app_mvvm_compose.model.Desh
import retrofit2.Response
import retrofit2.http.GET

interface CountriesApi {

    @GET("DevTides/countries/master/countriesV2.json") //this is the end point that we need for this project to get the api response.
    suspend fun getCountries() : Response<List<Desh>> //we are using coroutines here so making the function suspend.
}