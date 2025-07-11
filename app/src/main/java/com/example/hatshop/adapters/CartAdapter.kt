package com.example.hatshop.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hatshop.R
import com.example.hatshop.models.CartWithProduct

class CartAdapter(
    private val context: Context,
    private val cartItems: List<CartWithProduct>,
    private val onRemove: (Int) -> Unit,
    private val onItemCheckedChange: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val selectedItems = mutableSetOf<CartWithProduct>()

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkbox: CheckBox = view.findViewById(R.id.checkbox)
        val imgProduct: ImageView = view.findViewById(R.id.imgProduct)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvQty: TextView = view.findViewById(R.id.tvQty)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val btnRemove: ImageView = view.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        holder.tvName.text = item.productName
        holder.tvQty.text = "x${item.quantity}"
        holder.tvPrice.text = "${item.price.toInt()} VNĐ"
        Glide.with(context).load(item.productImage).into(holder.imgProduct)

        holder.btnRemove.setOnClickListener {
            onRemove(item.cartId)
        }

        holder.checkbox.setOnCheckedChangeListener(null)
        holder.checkbox.isChecked = selectedItems.contains(item)
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selectedItems.add(item) else selectedItems.remove(item)
            onItemCheckedChange()
        }
    }

    fun getSelectedItems(): List<CartWithProduct> = selectedItems.toList()
}
