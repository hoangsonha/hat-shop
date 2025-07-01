package com.example.hatshop.models

data class Shop(
    val id: Int = 0,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)