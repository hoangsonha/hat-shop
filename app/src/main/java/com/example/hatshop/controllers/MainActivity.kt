package com.example.hatshop.controllers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.hatshop.database.DBHelper
import com.example.hatshop.database.DataSeeder
import com.example.hatshop.databinding.ActivityMainBinding
import com.example.hatshop.R
import com.example.hatshop.fragments.CartFragment
import com.example.hatshop.fragments.HomeFragment
import com.example.hatshop.fragments.ProfileFragment
import com.example.hatshop.fragments.ShopFragment
import android.Manifest

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Giả sử bạn lưu userId vào SharedPreferences sau khi đăng nhập
        val prefs = getSharedPreferences("hatshop", MODE_PRIVATE)
        val userId = prefs.getInt("userId", -1)

        if (userId == -1) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        dbHelper = DBHelper(this)
        DataSeeder.seed(dbHelper)

        createNotificationChannel()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        }

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
        if (intent.getBooleanExtra("open_cart", false)) {
            // Mở CartFragment bằng FragmentManager
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, CartFragment()) // nếu dùng Navigation Component thì dùng NavController
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("hatshop", Context.MODE_PRIVATE)
        val userId = prefs.getInt("userId", -1)
        if (userId != -1) {
            val dbHelper = DBHelper(this)
            val cartItems = dbHelper.getCartItems(userId)
            if (cartItems.isNotEmpty()) {
                showCartNotification(cartItems.size)
            }
        }
    }

    private fun showCartNotification(cartSize: Int) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("open_cart", true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, "cart_channel_id")
            .setSmallIcon(R.drawable.ic_hat)
            .setContentTitle("Bạn có sản phẩm trong giỏ")
            .setContentText("Có $cartSize sản phẩm đang chờ thanh toán")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(1001, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Cart Channel"
            val descriptionText = "Thông báo giỏ hàng"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("cart_channel_id", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
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