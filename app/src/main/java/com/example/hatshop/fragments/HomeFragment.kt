package com.example.hatshop.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hatshop.R
import com.example.hatshop.adapters.ProductAdapter
import com.example.hatshop.database.DBHelper
import com.example.hatshop.databinding.FragmentHomeBinding
import com.example.hatshop.databinding.FragmentProductListBinding
import com.example.hatshop.models.CartItem

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbHelper: DBHelper
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DBHelper(requireContext())

        val prefs = requireActivity().getSharedPreferences("hatshop", Context.MODE_PRIVATE)
        userId = prefs.getInt("userId", -1)

        val productList = dbHelper.getAllProducts()

        val adapter = ProductAdapter(
            list = productList,
            onAddToCart = { product ->
                val result = dbHelper.addToCart(CartItem(0, userId, product.id, 1))
                if (result != -1L) {
                    Toast.makeText(requireContext(), "Đã thêm ${product.name} vào giỏ", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Không thể thêm vào giỏ", Toast.LENGTH_SHORT).show()
                }
            },
            onDetailClick = { product ->
                // mở fragment chi tiết
                val detailFragment = ProductDetailFragment.newInstance(product)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, detailFragment)
                    .addToBackStack(null)
                    .commit()
            },
            onShopClick = {},
            getShopName = { dbHelper.getShopById(it)?.name }
        )

        binding.recyclerProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerProducts.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(shopId: Int): ProductListFragment {
            val fragment = ProductListFragment()
            val bundle = Bundle().apply { putInt("shopId", shopId) }
            fragment.arguments = bundle
            return fragment
        }
    }
}