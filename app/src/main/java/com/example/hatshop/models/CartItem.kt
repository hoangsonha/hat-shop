package com.example.hatshop.models

data class CartItem(
    val id: Int = 0,
    val userId: Int,
    val productId: Int,
    val quantity: Int
)