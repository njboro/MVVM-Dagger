package com.example.app_mvvm_compose.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Desh(
    @PrimaryKey
    @SerializedName("name")
    val countryName: String,
    @SerializedName("capital")
    val capital : String?,
    @SerializedName("flagPNG")
    val flag : String?
)
