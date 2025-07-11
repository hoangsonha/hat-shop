package com.example.hatshop.models

data class CartWithProduct(
    val cartId: Int,
    val productId: Int,
    val productName: String,
    val productImage: String,
    val price: Double,
    val quantity: Int
)