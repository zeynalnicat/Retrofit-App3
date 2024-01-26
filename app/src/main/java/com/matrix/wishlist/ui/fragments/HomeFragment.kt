package com.matrix.wishlist.ui.fragments

import ConnectionLiveData
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.matrix.wishlist.R
import com.matrix.wishlist.databinding.FragmentHomeBinding
import com.matrix.wishlist.db.RoomDb
import com.matrix.wishlist.model.ProductRoomModel
import com.matrix.wishlist.resource.Resource
import com.matrix.wishlist.ui.MainActivity
import com.matrix.wishlist.ui.adapters.ProductAdapter
import com.matrix.wishlist.viewmodel.HomeViewModel
import com.matrix.wishlist.viewmodel.factory.HomeFactory


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var roomDb: RoomDb
    private val homeViewModel: HomeViewModel by viewModels { HomeFactory(roomDb) }
    private lateinit var connectionLiveData: ConnectionLiveData
    private var sPreferences: SharedPreferences? = null
    private var token: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        roomDb = RoomDb.accessDB(requireContext())!!
        bottomNavigation()
        setNavigation()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectionLiveData = ConnectionLiveData(requireContext().applicationContext)
        setSharedPreference()
        homeViewModel.checkDb()

        homeViewModel.products.observe(viewLifecycleOwner) { result ->
            handleProductsResult(result)
        }


        homeViewModel.categories.observe(viewLifecycleOwner) {
            handleCategoryResult(it)
        }

        connectionLiveData.observe(viewLifecycleOwner) { isConnected ->
            handleConnectionChange(isConnected)
        }


    }

    private fun handleProductsResult(result: Resource<List<ProductRoomModel>>) {
        when (result) {
            is Resource.Success -> {
                binding.progressBar.visibility = View.GONE
                setAdapter(result.data)
                homeViewModel.roomSize.observe(viewLifecycleOwner) {
                    if (it == 0) {
                        homeViewModel.insertDB()
                        homeViewModel.insertCategoryDB()
                    }
                }

            }

            is Resource.Error -> {
                Toast.makeText(requireContext(), result.exception.message, Toast.LENGTH_SHORT)
                    .show()
                binding.progressBar.visibility = View.GONE
            }

            else -> {
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun handleCategoryResult(result: Resource<List<String>>) {
        when (result) {
            is Resource.Success -> {
                setChipGroup(result.data)
            }

            is Resource.Error -> {
                Toast.makeText(requireContext(), result.exception.message, Toast.LENGTH_LONG).show()
            }

            is Resource.Loading -> {
            }
        }

    }

    private fun handleConnectionChange(isConnected: Boolean) {
        if (isConnected) {
            homeViewModel.getProducts()
            homeViewModel.getCategories()
            setSearchBar()
            setSpinner(false)
            binding.imgWifi.visibility = View.GONE
            binding.swipeRefreshLayout.setOnRefreshListener {
                homeViewModel.clearProducts()
                homeViewModel.clearCategories()
                homeViewModel.getProducts()
//                homeViewModel.getCategories()

                binding.swipeRefreshLayout.isRefreshing = false
            }

        } else {
            homeViewModel.getFromDB()
            homeViewModel.getDbCategories()
            setSearchBarRoom()
            setSpinner(true)
            binding.imgWifi.visibility = View.VISIBLE
            binding.swipeRefreshLayout.setOnRefreshListener {
                homeViewModel.clearProducts()
                homeViewModel.clearCategories()
                homeViewModel.getFromDB()
                homeViewModel.getDbCategories()
                binding.swipeRefreshLayout.isRefreshing = false
            }

        }
    }

    private fun setSearchBar() {
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString()
                homeViewModel.updateProducts(searchText)
            }
        })
    }

    private fun setSearchBarRoom() {
        binding.edtSearch.doAfterTextChanged {
            homeViewModel.searchRoom(it.toString())
        }
    }

    private fun setChipGroup(categories: List<String>) {

        val chipGroup = binding.chipGroup

        categories.forEach { category ->
            val chip = Chip(requireContext())
            chip.text = category
            chip.isClickable = true

            chip.setOnClickListener { view ->
                homeViewModel.isRoom.observe(viewLifecycleOwner) {
                    if (it) {
                        homeViewModel.getDbDueCategories(category)
                    } else {
                        homeViewModel.getDueCategory(category)
                    }
                }
            }
            chipGroup.addView(chip)

        }


    }


    private fun setAdapter(product: List<ProductRoomModel>) {
        val adapter = ProductAdapter(requireContext()) {
            findNavController().navigate(
                R.id.action_homeFragment_to_singleProductFragment,
                it
            )
        }
        adapter.submitList(product)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter
    }

    private fun setSharedPreference() {
        sPreferences = activity?.getSharedPreferences("UserDetail", Context.MODE_PRIVATE)
        token = sPreferences?.getString("Token", "") ?: ""
    }

    private fun bottomNavigation() {
        val activity = activity as MainActivity
        activity.setBottomNavigation(true)
    }

    private fun setNavigation() {
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addProductFragment)
        }
    }

    private fun setSpinner(isRoom: Boolean) {
        val list = arrayOf("Default", "A-Z", "Z-A", "ASC-Price", "DESC-Price")
        val spinner = binding.spinner
        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, list)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                (p1 as TextView).setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.white
                    )
                )
                homeViewModel.sort(list[p2], isRoom)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                homeViewModel.sort("Default", isRoom)
            }

        }
    }
}
