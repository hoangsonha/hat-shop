package com.example.hatshop.models

data class OrderDetail(
    val id: Int = 0,
    val orderId: Int,
    val productId: Int,
    val quantity: Int,
    val price: Double
)