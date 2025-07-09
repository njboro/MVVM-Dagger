package com.example.mvvm_dagger

import android.app.Application
import com.example.mvvm_dagger.di.ApplicationComponent
import com.example.mvvm_dagger.di.DaggerApplicationComponent

class CountriesApplication : Application(){

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder().build()
    }
}