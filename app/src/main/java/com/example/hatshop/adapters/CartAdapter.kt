package com.example.hatshop.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hatshop.R
import com.example.hatshop.models.CartWithProduct

class CartAdapter(
    private val context: Context,
    private val items: MutableList<CartWithProduct>,
    private val onRemove: (Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvName)
        val price: TextView = view.findViewById(R.id.tvPrice)
        val quantity: TextView = view.findViewById(R.id.tvQty)
        val image: ImageView = view.findViewById(R.id.imgProduct)
        val btnRemove: ImageView = view.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.productName
        holder.price.text = "Giá: ${item.price * item.quantity} VNĐ"
        holder.quantity.text = "x${item.quantity}"

        Glide.with(context).load(item.productImage).into(holder.image)

        holder.btnRemove.setOnClickListener {
            onRemove(item.cartId)
        }
    }

    override fun getItemCount(): Int = items.size
}