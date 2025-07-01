package com.example.hatshop.controllers

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hatshop.database.DBHelper
import com.example.hatshop.databinding.ActivityRegisterBinding
import com.example.hatshop.models.User

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        binding.btnRegister.setOnClickListener {
            val username = binding.edtUsername.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            val fullName = binding.edtFullName.text.toString().trim()
            val phone = binding.edtPhone.text.toString().trim()
            val address = binding.edtAddress.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Tên đăng nhập và mật khẩu không được để trống", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(
                id = 0,
                username = username,
                password = password,
                fullName = fullName,
                email = null,
                phone = phone,
                address = address
            )

            val result = dbHelper.insertUser(user)
            if (result != -1L) {
                Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Tên tài khoản đã tồn tại", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvGotoLogin.setOnClickListener {
            finish()
        }
    }
}