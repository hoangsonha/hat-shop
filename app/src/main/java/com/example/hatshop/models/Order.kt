package com.example.hatshop.models

data class Order(
    val id: Int = 0,
    val userId: Int,
    val totalAmount: Double,
    val orderDate: String
)