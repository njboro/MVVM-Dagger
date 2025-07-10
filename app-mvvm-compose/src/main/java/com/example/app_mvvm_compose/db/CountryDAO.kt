package com.example.app_mvvm_compose.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.app_mvvm_compose.model.Desh

@Dao
interface CountryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCountries(countries : List<Desh>)

    @Query("SELECT * FROM Desh")
    fun getCountries() : List<Desh>
}