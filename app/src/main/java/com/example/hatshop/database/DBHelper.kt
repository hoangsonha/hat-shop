package com.example.hatshop.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.hatshop.models.CartItem
import com.example.hatshop.models.CartWithProduct
import com.example.hatshop.models.Order
import com.example.hatshop.models.OrderDetail
import com.example.hatshop.models.OrderItem
import com.example.hatshop.models.Product
import com.example.hatshop.models.Shop
import com.example.hatshop.models.User

class DBHelper(context: Context) : SQLiteOpenHelper(context, "hatshop.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE User (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                fullName TEXT,
                email TEXT,
                phone TEXT,
                address TEXT
            );
        """)

        db.execSQL("""
            CREATE TABLE Shop (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                address TEXT,
                latitude REAL,
                longitude REAL
            );
        """)

        db.execSQL("""
            CREATE TABLE Product (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                price REAL,
                description TEXT,
                imagePath TEXT,
                stock INTEGER,
                shopId INTEGER,
                FOREIGN KEY(shopId) REFERENCES Shop(id)
            );
        """)

        db.execSQL("""
            CREATE TABLE Cart (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                userId INTEGER,
                productId INTEGER,
                quantity INTEGER,
                FOREIGN KEY(userId) REFERENCES User(id),
                FOREIGN KEY(productId) REFERENCES Product(id)
            );
        """)

        db.execSQL("""
            CREATE TABLE OrderTable (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                userId INTEGER,
                totalAmount REAL,
                orderDate TEXT,
                FOREIGN KEY(userId) REFERENCES User(id)
            );
        """)

        db.execSQL("""
            CREATE TABLE OrderDetail (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                orderId INTEGER,
                productId INTEGER,
                quantity INTEGER,
                price REAL,
                FOREIGN KEY(orderId) REFERENCES OrderTable(id),
                FOREIGN KEY(productId) REFERENCES Product(id)
            );
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS OrderDetail")
        db.execSQL("DROP TABLE IF EXISTS OrderTable")
        db.execSQL("DROP TABLE IF EXISTS Cart")
        db.execSQL("DROP TABLE IF EXISTS Product")
        db.execSQL("DROP TABLE IF EXISTS Shop")
        db.execSQL("DROP TABLE IF EXISTS User")
        onCreate(db)
    }


    // ------------------------
    // User
    // ------------------------
    fun getUserById(id: Int): User? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM User WHERE id = ?", arrayOf(id.toString()))
        var user: User? = null

        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                username = cursor.getString(cursor.getColumnIndexOrThrow("username")),
                password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                fullName = cursor.getString(cursor.getColumnIndexOrThrow("fullName")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                phone = cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                address = cursor.getString(cursor.getColumnIndexOrThrow("address"))
            )
        }
        cursor.close()
        return user
    }

    fun insertUser(user: User): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("username", user.username)
            put("password", user.password)
            put("fullName", user.fullName)
            put("email", user.email)
            put("phone", user.phone)
            put("address", user.address)
        }
        return db.insert("User", null, values)
    }

    fun loginUser(username: String, password: String): User? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM User WHERE username = ? AND password = ?", arrayOf(username, password))
        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getInt(0),
                username = cursor.getString(1),
                password = cursor.getString(2),
                fullName = cursor.getString(3),
                email = cursor.getString(4),
                phone = cursor.getString(5),
                address = cursor.getString(6)
            )
        }
        cursor.close()
        return user
    }

    fun getOrdersByUserId(userId: Int): List<Order> {
        val list = mutableListOf<Order>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM OrderTable WHERE userId = ?", arrayOf(userId.toString()))
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Order(
                        id = cursor.getInt(0),
                        userId = cursor.getInt(1),
                        totalAmount = cursor.getDouble(2),
                        orderDate = cursor.getString(3)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getOrderDetailsDisplay(orderId: Int): List<OrderItem> {
        val list = mutableListOf<OrderItem>()
        val db = readableDatabase
        val cursor = db.rawQuery("""
        SELECT P.name, OD.quantity, OD.price
        FROM OrderDetail OD
        JOIN Product P ON OD.productId = P.id
        WHERE OD.orderId = ?
    """.trimIndent(), arrayOf(orderId.toString()))

        if (cursor.moveToFirst()) {
            do {
                list.add(
                    OrderItem(
                        productName = cursor.getString(0),
                        quantity = cursor.getInt(1),
                        price = cursor.getDouble(2)
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        return list
    }

    fun getCartItems(userId: Int): List<CartItem> {
        val list = mutableListOf<CartItem>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Cart WHERE userId = ?", arrayOf(userId.toString()))
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    CartItem(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                        productId = cursor.getInt(cursor.getColumnIndexOrThrow("productId")),
                        quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    // ------------------------
    // SHOP
    // ------------------------

    fun getShopById(shopId: Int): Shop? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Shop WHERE id = ?", arrayOf(shopId.toString()))
        var shop: Shop? = null
        if (cursor.moveToFirst()) {
            shop = Shop(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                address = cursor.getString(cursor.getColumnIndexOrThrow("address")),
                latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude")),
                longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"))
            )
        }
        cursor.close()
        return shop
    }

    fun getShopNameById(shopId: Int): String? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT name FROM Shop WHERE id = ?", arrayOf(shopId.toString()))
        var name: String? = null
        if (cursor.moveToFirst()) {
            name = cursor.getString(0)
        }
        cursor.close()
        return name
    }

    fun insertShop(shop: Shop): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", shop.name)
            put("address", shop.address)
            put("latitude", shop.latitude)
            put("longitude", shop.longitude)
        }
        return db.insert("Shop", null, values)
    }

    fun getAllShops(): List<Shop> {
        val list = mutableListOf<Shop>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Shop", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Shop(
                        id = cursor.getInt(0),
                        name = cursor.getString(1),
                        address = cursor.getString(2),
                        latitude = cursor.getDouble(3),
                        longitude = cursor.getDouble(4)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    // ------------------------
    // PRODUCT
    // ------------------------
    fun insertProduct(product: Product): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", product.name)
            put("price", product.price)
            put("description", product.description)
            put("imagePath", product.imagePath)
            put("stock", product.stock)
            put("shopId", product.shopId)
        }
        return db.insert("Product", null, values)
    }

    fun getAllProducts(): List<Product> {
        val list = mutableListOf<Product>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Product", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Product(
                        id = cursor.getInt(0),
                        name = cursor.getString(1),
                        price = cursor.getDouble(2),
                        description = cursor.getString(3),
                        imagePath = cursor.getString(4),
                        stock = cursor.getInt(5),
                        shopId = cursor.getInt(6)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getProductsByShop(shopId: Int): List<Product> {
        val list = mutableListOf<Product>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Product WHERE shopId = ?", arrayOf(shopId.toString()))
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Product(
                        id = cursor.getInt(0),
                        name = cursor.getString(1),
                        price = cursor.getDouble(2),
                        description = cursor.getString(3),
                        imagePath = cursor.getString(4),
                        stock = cursor.getInt(5),
                        shopId = cursor.getInt(6)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    // ------------------------
    // CART
    // ------------------------
    fun addToCart(item: CartItem): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("userId", item.userId)
            put("productId", item.productId)
            put("quantity", item.quantity)
        }
        return db.insert("Cart", null, values)
    }

    fun getCartByUser(userId: Int): List<CartItem> {
        val list = mutableListOf<CartItem>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Cart WHERE userId = ?", arrayOf(userId.toString()))
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    CartItem(
                        id = cursor.getInt(0),
                        userId = cursor.getInt(1),
                        productId = cursor.getInt(2),
                        quantity = cursor.getInt(3)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getCartWithProduct(userId: Int): List<CartWithProduct> {
        val list = mutableListOf<CartWithProduct>()
        val db = readableDatabase
        val cursor = db.rawQuery("""
        SELECT Cart.id, Product.name, Product.imagePath, Product.price, Cart.quantity
        FROM Cart
        INNER JOIN Product ON Cart.productId = Product.id
        WHERE Cart.userId = ?
    """, arrayOf(userId.toString()))

        while (cursor.moveToNext()) {
            list.add(
                CartWithProduct(
                    cartId = cursor.getInt(0),
                    productName = cursor.getString(1),
                    productImage = cursor.getString(2),
                    price = cursor.getDouble(3),
                    quantity = cursor.getInt(4)
                )
            )
        }

        cursor.close()
        return list
    }

    fun removeCartItem(cartId: Int): Boolean {
        val db = writableDatabase
        return db.delete("Cart", "id = ?", arrayOf(cartId.toString())) > 0
    }

    fun clearCart(userId: Int) {
        val db = writableDatabase
        db.delete("Cart", "userId = ?", arrayOf(userId.toString()))
    }



}