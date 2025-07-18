package com.example.mvvm_dagger.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mvvm_dagger.model.Country

@Database(entities = [Country::class], version = 1)
abstract class CountryDB : RoomDatabase(){
    abstract fun getCountryDAO() : CountryDAO
}