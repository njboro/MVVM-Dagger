package com.example.app_mvvm_compose.di

import com.example.app_mvvm_compose.retrofit.CountriesApi
import com.example.app_mvvm_compose.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit() : Retrofit {
        val okHttpBuilder = OkHttpClient.Builder()
        val httpLoginInterceptor = HttpLoggingInterceptor()
        httpLoginInterceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttpBuilder.addInterceptor(httpLoginInterceptor)

        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpBuilder.build())
            .build()
    }

    @Singleton
    @Provides
    fun providesCountriesApi(retrofit: Retrofit) : CountriesApi {
        return retrofit.create(CountriesApi::class.java)
    }
}