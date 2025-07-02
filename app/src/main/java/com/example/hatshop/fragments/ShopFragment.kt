package com.example.hatshop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hatshop.R
import com.example.hatshop.adapters.ShopAdapter
import com.example.hatshop.database.DBHelper
import com.example.hatshop.databinding.FragmentShopBinding

class ShopFragment : Fragment() {
    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbHelper: DBHelper
    private var userId: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DBHelper(requireContext())
        val prefs = requireActivity().getSharedPreferences("hatshop", AppCompatActivity.MODE_PRIVATE)
        userId = prefs.getInt("userId", -1)

        val shopList = dbHelper.getAllShops()
        if (shopList.isEmpty()) {
            Toast.makeText(requireContext(), "Không có cửa hàng nào trong hệ thống", Toast.LENGTH_SHORT).show()
            return
        }

        val adapter = ShopAdapter(shopList) { shop ->
            val fragment = ProductListFragment.newInstance(shop.id)

            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment) // đảm bảo id đúng là container chính
                .addToBackStack(null) // để có thể quay lại
                .commit()
        }
        binding.recyclerShops.adapter = adapter

        binding.recyclerShops.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerShops.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}