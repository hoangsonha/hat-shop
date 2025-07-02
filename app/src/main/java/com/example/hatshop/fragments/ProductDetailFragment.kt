package com.example.hatshop.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.hatshop.R
import com.example.hatshop.database.DBHelper
import com.example.hatshop.models.CartItem
import com.example.hatshop.models.Product

class ProductDetailFragment : Fragment(R.layout.fragment_product_detail) {

    private var userId: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val product = arguments?.getSerializable("product") as? Product ?: return
        val dbHelper = DBHelper(requireContext())
        val shop = dbHelper.getShopById(product.shopId)

        val prefs = requireActivity().getSharedPreferences("hatshop", Context.MODE_PRIVATE)
        userId = prefs.getInt("userId", -1)

        view.findViewById<TextView>(R.id.tvDetailName).text = product.name
        view.findViewById<TextView>(R.id.tvDetailPrice).text = "Giá: ${product.price} đ"
        view.findViewById<TextView>(R.id.tvDetailDescription).text = product.description
        view.findViewById<TextView>(R.id.tvDetailStock).text = "Kho: ${product.stock}"
        view.findViewById<TextView>(R.id.tvDetailShopName).apply {
            text = "Cửa hàng: ${shop?.name ?: "Không rõ"}"
            setOnClickListener {
                val frag = ShopProductFragment.newInstance(product.shopId)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null)
                    .commit()
            }
        }

        view.findViewById<Button>(R.id.btnAddToCart).setOnClickListener {
            val result = dbHelper.addToCart(CartItem(0, userId, product.id, 1))
            if (result != -1L) {
                Toast.makeText(requireContext(), "Đã thêm ${product.name} vào giỏ", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Không thể thêm vào giỏ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun newInstance(product: Product): ProductDetailFragment {
            val fragment = ProductDetailFragment()
            val bundle = Bundle()
            bundle.putSerializable("product", product)
            fragment.arguments = bundle
            return fragment
        }
    }
}