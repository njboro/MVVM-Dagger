package com.example.app_mvvm_compose.di

import android.content.Context
import androidx.room.Room
import com.example.app_mvvm_compose.db.CountryDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideCountryDB(@ApplicationContext context : Context) : CountryDB {
        return Room.databaseBuilder(context, CountryDB::class.java, "CountryDB").build()
    }
}