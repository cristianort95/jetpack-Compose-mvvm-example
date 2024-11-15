package com.example.myapplication.model.api

data class ResponseRequest<T>(
    val success: Boolean,
    val statusCode: Int,
    val data: T?,
    val info: Info?,
    val message: String=""
)