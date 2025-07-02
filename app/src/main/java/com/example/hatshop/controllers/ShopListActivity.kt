//package com.example.hatshop.controllers
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.hatshop.adapters.ShopAdapter
//import com.example.hatshop.database.DBHelper
//import com.example.hatshop.databinding.ActivityShopListBinding
//
//class ShopListActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityShopListBinding
//    private lateinit var dbHelper: DBHelper
//    private var userId: Int = -1
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityShopListBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        userId = intent.getIntExtra("userId", -1)
//        dbHelper = DBHelper(this)
//
//        val shopList = dbHelper.getAllShops()
//        if (shopList.isEmpty()) {
//            Toast.makeText(this, "Không có cửa hàng nào trong hệ thống", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val adapter = ShopAdapter(shopList) { shop ->
//            val intent = Intent(this, ProductListActivity::class.java)
//            intent.putExtra("shopId", shop.id)
//            intent.putExtra("userId", userId)
//            startActivity(intent)
//        }
//
//        binding.recyclerShops.adapter = adapter
//    }
//}