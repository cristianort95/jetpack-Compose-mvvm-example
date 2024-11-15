package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class Episode (
    val id: Int,
    val name: String,
    @SerializedName("air_date")
    val airDate: String, // Cambiar el nombre a airDate
    val episode: String,
    val characters: List<String>,
    val url: String,
    val created: String
)