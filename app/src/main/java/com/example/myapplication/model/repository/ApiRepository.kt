package com.example.myapplication.model.repository

import android.util.Log
import com.example.myapplication.model.Character
import com.example.myapplication.model.Episode
import com.example.myapplication.model.api.ResponseApiData
import com.example.myapplication.model.api.ResponseRequest
import com.example.myapplication.model.api.RetrofitInstance
import retrofit2.Response

class ApiRepository {
    private var service = RetrofitInstance.getCharacterService

    suspend fun getCharacter(page: Int): ResponseRequest<List<Character>> {
        return apiCall { service.getCharacter(page) }
    }

    suspend fun getEpisode(page: Int): ResponseRequest<List<Episode>> {
        return apiCall { service.getEpisode(page) }
    }

    private suspend fun <T> apiCall(apiCall: suspend () -> Response<ResponseApiData<T>>): ResponseRequest<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                ResponseRequest(success = true, statusCode = response.code(), data = response.body()?.results, info = response.body()?.info)
            } else {
                ResponseRequest(success = true, statusCode = response.code(), data = response.body()?.results, info = response.body()?.info, message = response.message())
            }
        } catch (e: Exception) {
            if (e is retrofit2.HttpException) {
                val statusCode = e.code()
                ResponseRequest(success = false, message = e.message ?: "Unknown error occurred", statusCode = statusCode, data = null, info = null)
            } else {
                Log.e("TEST", "fetch Character failed: ${e.message}")
                ResponseRequest(success = false, message = e.message ?: "Unknown error occurred", statusCode = 400, data = null, info = null)
            }
        }
    }

}