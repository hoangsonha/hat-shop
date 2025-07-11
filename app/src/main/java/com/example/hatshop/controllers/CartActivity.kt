package com.example.hatshop.controllers

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hatshop.adapters.CartAdapter
import com.example.hatshop.database.DBHelper
import com.example.hatshop.R

//class CartActivity : AppCompatActivity() {
//    private lateinit var db: DBHelper
//    private lateinit var recycler: RecyclerView
//    private lateinit var tvTotal: TextView
//    private lateinit var btnCheckout: Button
//    private var userId: Int = -1
//    private lateinit var adapter: CartAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_cart)
//
//        db = DBHelper(this)
//        userId = intent.getIntExtra("userId", -1)
//
//        recycler = findViewById(R.id.recyclerCart)
//        tvTotal = findViewById(R.id.tvTotal)
//        btnCheckout = findViewById(R.id.btnCheckout)
//
//        btnCheckout.setOnClickListener {
//            db.clearCart(userId)
//            Toast.makeText(this, "Đã thanh toán!", Toast.LENGTH_SHORT).show()
//            loadCart()
//        }
//
//        loadCart()
//    }
//
//    private fun loadCart() {
//        val items = db.getCartWithProduct(userId).toMutableList()
//
//        adapter = CartAdapter(this, items) { cartId ->
//            db.removeCartItem(cartId)
//            loadCart()
//        }
//
//        recycler.adapter = adapter
//        recycler.layoutManager = LinearLayoutManager(this)
//
//        val total = items.sumOf { it.price * it.quantity }
//        tvTotal.text = "Tổng tiền: $total VNĐ"
//    }
//}