package com.matrix.android_104_android.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.matrix.android_104_android.R
import com.matrix.android_104_android.databinding.FragmentHomeBinding
import com.matrix.android_104_android.ui.MainActivity
import com.matrix.android_104_android.ui.adapters.ProductAdapter
import com.matrix.android_104_android.viewmodel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private var sPreferences: SharedPreferences? = null
    private var token: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        bottomNavigation()
        setSharedPreference()
        homeViewModel.updateToken(token)
        homeViewModel.getProducts()
        homeViewModel.getCategories()
        setSearchBar()
        setChipGroup()
        setAdapter()
        return binding.root
    }

    private fun setSearchBar() {
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString()
                homeViewModel.updateProducts(searchText)
            }
        })
    }

    private fun setChipGroup() {

        homeViewModel.categories.observe(viewLifecycleOwner) { categories ->
            categories.forEach {
                val chip = Chip(requireContext())
                chip.text = it
                chip.isClickable = true
                chip.setOnClickListener { view ->
                    if (it == "All") {
                        homeViewModel.getProducts()
                    } else {
                        homeViewModel.getDueCategory(it)
                    }
                    chip.isSelected = !chip.isSelected
                }
                binding.chipGroup.addView(chip)
            }
        }
    }

    private fun setAdapter() {
        homeViewModel.products.observe(viewLifecycleOwner) { productList ->
            productList?.let {
                val adapter = ProductAdapter(requireContext()) {
                    findNavController().navigate(
                        R.id.action_homeFragment_to_singleProductFragment,
                        it
                    )
                }
                adapter.submitList(it.products)
                binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
                binding.recyclerView.adapter = adapter
            }

        }

    }

    private fun setSharedPreference() {
        sPreferences = activity?.getSharedPreferences("UserDetail", Context.MODE_PRIVATE)
        token = sPreferences?.getString("Token", "")!!
    }

    private fun bottomNavigation() {
        val activity = activity as MainActivity
        activity.setBottomNavigation(true)
    }

}