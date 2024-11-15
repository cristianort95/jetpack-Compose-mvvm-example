package com.example.myapplication.model.api

import com.example.myapplication.model.Character
import com.example.myapplication.model.Episode
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    @GET("character/")
    suspend fun getCharacter(@Query("page") page: Int): Response<ResponseApiData<List<Character>>>

    @GET("episode/")
    suspend fun getEpisode(@Query("page") page: Int): Response<ResponseApiData<List<Episode>>>
}