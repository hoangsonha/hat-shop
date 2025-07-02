package com.example.hatshop.models

import java.io.Serializable

data class Product(
    val id: Int = 0,
    val name: String,
    val price: Double,
    val description: String,
    val imagePath: String,
    val stock: Int,
    val shopId: Int
): Serializable