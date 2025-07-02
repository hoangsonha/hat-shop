package com.example.hatshop.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.hatshop.R
import com.example.hatshop.adapters.ProductAdapter
import com.example.hatshop.database.DBHelper
import com.example.hatshop.models.CartItem

class ShopProductFragment : Fragment(R.layout.fragment_product_list) {

    private var userId: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val shopId = arguments?.getInt("shopId") ?: return
        val dbHelper = DBHelper(requireContext())
        val products = dbHelper.getProductsByShop(shopId)

        val prefs = requireActivity().getSharedPreferences("hatshop", Context.MODE_PRIVATE)
        userId = prefs.getInt("userId", -1)

        val adapter = ProductAdapter(
            list = products,
            onAddToCart = { product ->
                val result = dbHelper.addToCart(CartItem(0, userId, product.id, 1))
                if (result != -1L) {
                    Toast.makeText(requireContext(), "Đã thêm ${product.name} vào giỏ", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Không thể thêm vào giỏ", Toast.LENGTH_SHORT).show()
                }
            },
            onDetailClick = { product ->
                val frag = ProductDetailFragment.newInstance(product)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null)
                    .commit()
            },
            onShopClick = {},
            getShopName = { dbHelper.getShopById(it)?.name }
        )

        view.findViewById<RecyclerView>(R.id.recyclerProducts).adapter = adapter
    }

    companion object {
        fun newInstance(shopId: Int): ShopProductFragment {
            val f = ShopProductFragment()
            f.arguments = Bundle().apply { putInt("shopId", shopId) }
            return f
        }
    }
}