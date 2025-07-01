package com.example.hatshop.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hatshop.R
import com.example.hatshop.adapters.CartAdapter
import com.example.hatshop.database.DBHelper
import com.example.hatshop.databinding.FragmentCartBinding

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbHelper: DBHelper
    private var userId: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DBHelper(requireContext())

        val prefs = requireActivity().getSharedPreferences("hatshop", Context.MODE_PRIVATE)
        userId = prefs.getInt("userId", -1)

        loadCart()
    }

    private fun loadCart() {
        val cartItems = dbHelper.getCartWithProduct(userId).toMutableList()
        if (cartItems.isEmpty()) {
            Toast.makeText(requireContext(), "Giỏ hàng của bạn trống", Toast.LENGTH_SHORT).show()
        }

        val adapter = CartAdapter(requireContext(), cartItems) { cartId ->
            dbHelper.removeCartItem(cartId)
            loadCart()
        }
        binding.recyclerCart.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerCart.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
