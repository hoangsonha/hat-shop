package com.example.hatshop.database

import com.example.hatshop.models.Product
import com.example.hatshop.models.Shop

object DataSeeder {
    fun seed(db: DBHelper) {
        if (db.getAllShops().isNotEmpty()) return // tránh seed trùng

        // ===== 1. Thêm shop mẫu =====
        val shop1Id = db.insertShop(
            Shop(
                id = 0,
                name = "Hat Store District 1",
                address = "123 Lê Lợi, Q1, TP.HCM",
                latitude = 10.776889,
                longitude = 106.700806
            )
        ).toInt()

        val shop2Id = db.insertShop(
            Shop(
                id = 0,
                name = "Hat Boutique Hà Nội",
                address = "456 Tràng Tiền, Hoàn Kiếm, Hà Nội",
                latitude = 21.028511,
                longitude = 105.854444
            )
        ).toInt()

        // ===== 2. Thêm sản phẩm cho shop 1 =====
        db.insertProduct(
            Product(
                id = 0,
                name = "Nón bucket basic",
                price = 129000.0,
                description = "Nón bucket thời trang phong cách basic",
                imagePath = "",  // có thể cập nhật sau
                stock = 20,
                shopId = shop1Id
            )
        )

        db.insertProduct(
            Product(
                id = 0,
                name = "Nón snapback đen",
                price = 159000.0,
                description = "Nón hiphop dạng snapback cực chất",
                imagePath = "",
                stock = 15,
                shopId = shop1Id
            )
        )

        // ===== 3. Thêm sản phẩm cho shop 2 =====
        db.insertProduct(
            Product(
                id = 0,
                name = "Mũ lưỡi trai trắng",
                price = 119000.0,
                description = "Mũ lưỡi trai trắng thanh lịch",
                imagePath = "",
                stock = 25,
                shopId = shop2Id
            )
        )

        db.insertProduct(
            Product(
                id = 0,
                name = "Mũ beret thời trang",
                price = 179000.0,
                description = "Mũ nồi (beret) phong cách Hàn Quốc",
                imagePath = "",
                stock = 12,
                shopId = shop2Id
            )
        )
    }
}