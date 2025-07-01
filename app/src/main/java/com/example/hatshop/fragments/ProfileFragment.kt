package com.example.hatshop.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hatshop.R
import com.example.hatshop.controllers.LoginActivity
import com.example.hatshop.database.DBHelper
import com.example.hatshop.databinding.FragmentProfileBinding
import com.example.hatshop.models.User

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbHelper: DBHelper
    private var userId: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DBHelper(requireContext())

        val prefs = requireActivity().getSharedPreferences("hatshop", Context.MODE_PRIVATE)
        userId = prefs.getInt("userId", -1)

        val user: User? = dbHelper.getUserById(userId)
        user?.let {
            binding.tvUsername.text = it.username
            binding.tvFullName.text = it.fullName
            binding.tvEmail.text = it.email
            binding.tvPhone.text = it.phone
            binding.tvAddress.text = it.address
        }

        binding.btnLogout.setOnClickListener {
            prefs.edit().remove("userId").apply()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
