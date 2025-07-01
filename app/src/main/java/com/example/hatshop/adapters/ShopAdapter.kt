package com.example.hatshop.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hatshop.models.Shop
import com.example.hatshop.R


class ShopAdapter(
    private val shops: List<Shop>,
    private val onClick: (Shop) -> Unit
) : RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

    inner class ShopViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tvShopName)
        val tvAddress = view.findViewById<TextView>(R.id.tvShopAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop, parent, false)
        return ShopViewHolder(view)
    }

    override fun getItemCount(): Int = shops.size

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shop = shops[position]
        holder.tvName.text = shop.name
        holder.tvAddress.text = shop.address

        holder.itemView.setOnClickListener {
            onClick(shop)
        }
    }
}