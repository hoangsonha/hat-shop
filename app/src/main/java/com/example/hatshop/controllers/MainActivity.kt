package com.example.hatshop.controllers

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hatshop.database.DBHelper
import com.example.hatshop.database.DataSeeder
import com.example.hatshop.databinding.ActivityMainBinding
import com.example.hatshop.R
import com.example.hatshop.fragments.CartFragment
import com.example.hatshop.fragments.HomeFragment
import com.example.hatshop.fragments.ProfileFragment
import com.example.hatshop.fragments.ShopFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hiển thị Fragment mặc định
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, HomeFragment())
                .commit()
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val selectedFragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_shop -> ShopFragment()
                R.id.nav_cart -> CartFragment()
                R.id.nav_profile -> ProfileFragment()
                else -> null
            }
            selectedFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, it)
                    .commit()
            }
            true
        }
    }
}

//class MainActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityMainBinding
//
//    private lateinit var dbHelper: DBHelper
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
////        binding = ActivityMainBinding.inflate(layoutInflater)
////        setContentView(binding.root)
//
//        dbHelper = DBHelper(this)
//        DataSeeder.seed(dbHelper)
//
//        // Giả sử bạn lưu userId vào SharedPreferences sau khi đăng nhập
//        val prefs = getSharedPreferences("hatshop", MODE_PRIVATE)
//        val userId = prefs.getInt("userId", -1)
//
//        if (userId != -1) {
//            // Đã đăng nhập
//            val intent = Intent(this, ShopListActivity::class.java)
//            intent.putExtra("userId", userId)
//            startActivity(intent)
//        } else {
//            // Chưa đăng nhập
//            startActivity(Intent(this, LoginActivity::class.java))
//        }
//
//        finish() // không quay lại MainActivity khi nhấn back
//
//    }
//
//}