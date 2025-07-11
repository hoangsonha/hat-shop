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
    private lateinit var cartAdapter: CartAdapter
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

        binding.btnCheckout.setOnClickListener {
            processCheckout()
        }
    }

    private fun loadCart() {
        val cartItems = dbHelper.getCartWithProduct(userId).toMutableList()
        cartAdapter = CartAdapter(requireContext(), cartItems,
            onRemove = { cartId ->
                dbHelper.removeCartItem(cartId)
                loadCart()
            },
            onItemCheckedChange = {
                updateTotal()
            }
        )

        binding.recyclerCart.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerCart.adapter = cartAdapter
        updateTotal()
    }

    private fun updateTotal() {
        val total = cartAdapter.getSelectedItems().sumOf { it.price * it.quantity }
        binding.tvTotal.text = "Tổng: ${total.toInt()} VNĐ"
    }

    private fun processCheckout() {
        val selectedItems = cartAdapter.getSelectedItems()
        if (selectedItems.isEmpty()) {
            Toast.makeText(requireContext(), "Bạn chưa chọn sản phẩm nào", Toast.LENGTH_SHORT).show()
            return
        }

        val totalAmount = selectedItems.sumOf { it.price * it.quantity }
        val orderId = dbHelper.createOrder(userId, totalAmount)
        if (orderId == -1L) {
            Toast.makeText(requireContext(), "Thanh toán thất bại", Toast.LENGTH_SHORT).show()
            return
        }

        for (item in selectedItems) {
            dbHelper.insertOrderDetail(orderId, item.productId, item.quantity, item.price)
            dbHelper.removeCartItem(item.cartId)
        }

        Toast.makeText(requireContext(), "Đặt hàng thành công!", Toast.LENGTH_SHORT).show()
        loadCart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

