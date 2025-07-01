package com.example.hatshop.controllers

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hatshop.adapters.ProductAdapter
import com.example.hatshop.database.DBHelper
import com.example.hatshop.databinding.ActivityProductListBinding
import com.example.hatshop.models.CartItem

class ProductListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductListBinding
    private lateinit var dbHelper: DBHelper
    private var userId: Int = -1
    private var shopId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)
        shopId = intent.getIntExtra("shopId", -1)
        userId = intent.getIntExtra("userId", -1)

        val productList = dbHelper.getProductsByShop(shopId)
        val adapter = ProductAdapter(productList) { product ->
            val result = dbHelper.addToCart(CartItem(0, userId, product.id, 1))
            if (result != -1L) {
                Toast.makeText(this, "Đã thêm ${product.name} vào giỏ", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Không thể thêm vào giỏ", Toast.LENGTH_SHORT).show()
            }
        }

        binding.recyclerProducts.adapter = adapter
    }
}