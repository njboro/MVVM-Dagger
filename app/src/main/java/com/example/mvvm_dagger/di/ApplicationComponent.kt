package com.example.mvvm_dagger.di

import com.example.mvvm_dagger.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity) // for field inject property inside the Main Activity
    /*
    * Dagger will check MainActivity for the fields inside to provide values.
    * Inorder to do that we need this inject method
    * */
}