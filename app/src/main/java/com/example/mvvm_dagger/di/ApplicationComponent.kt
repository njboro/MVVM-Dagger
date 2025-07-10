package com.example.mvvm_dagger.di

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.mvvm_dagger.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, DatabaseModule::class, ViewModelModule::class])
interface ApplicationComponent {

    /*
    * Dagger will check MainActivity for the fields inside to provide values.
    * Inorder to do that we need this inject method
    * */
    fun inject(mainActivity: MainActivity) // for field inject property inside the Main Activity


    // this is the map function that reruns any map data that in the Dagger reach
    fun getMap() : Map<Class<*>, ViewModel>

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context : Context) : ApplicationComponent
    }
}