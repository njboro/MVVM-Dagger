package com.example.app_mvvm_compose.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.app_mvvm_compose.model.Desh

@Database(entities = [Desh::class], version = 1)
abstract class CountryDB : RoomDatabase(){
    abstract fun getCountryDAO() : CountryDAO
}