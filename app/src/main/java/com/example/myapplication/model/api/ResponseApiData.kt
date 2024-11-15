package com.example.myapplication.model.api

data class ResponseApiData<Item>(
    val info: Info,
    val results: Item
)