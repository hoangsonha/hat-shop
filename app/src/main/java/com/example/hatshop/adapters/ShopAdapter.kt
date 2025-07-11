package com.example.hatshop.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        val btnMap = view.findViewById<Button>(R.id.btnXemMap)
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

        holder.btnMap.setOnClickListener {
            val lat = shop.latitude
            val lng = shop.longitude
            val uri = Uri.parse("geo:$lat,$lng?q=$lat,$lng(${Uri.encode(shop.name)})")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")
            it.context.startActivity(intent)
        }
    }
}