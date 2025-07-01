package com.example.hatshop.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hatshop.models.Product
import com.example.hatshop.R

class ProductAdapter(
    private val list: List<Product>,
    private val onAddToCart: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tvProductName)
        val tvPrice = view.findViewById<TextView>(R.id.tvPrice)
        val btnAdd = view.findViewById<Button>(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(v)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = list[position]
        holder.tvName.text = product.name
        holder.tvPrice.text = "Giá: ${product.price} đ"
        holder.btnAdd.setOnClickListener { onAddToCart(product) }
    }
}