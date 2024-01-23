package com.matrix.android_104_android.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.matrix.android_104_android.R
import com.matrix.android_104_android.databinding.FragmentAddProductBinding
import com.matrix.android_104_android.model.Product
import com.matrix.android_104_android.viewmodel.AddProductViewModel
import kotlinx.coroutines.launch


class AddProductFragment : Fragment() {
    private lateinit var binding: FragmentAddProductBinding
    private lateinit var addProductViewModel: AddProductViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addProductViewModel = ViewModelProvider(this)[AddProductViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(inflater)

        addProduct()
        return binding.root
    }


    private fun addProduct() {
        val token = activity?.getSharedPreferences("UserDetail", Context.MODE_PRIVATE)
            ?.getString("Token", "")
        binding.btnSubmit.setOnClickListener {
            val category = binding.edtCategory.text.toString()
            val description = binding.edtDescription.text.toString()
            val price =
                if (binding.edtPrice.text.toString().isNotEmpty()) binding.edtPrice.text.toString()
                    .toInt() else 0

            val title = binding.edtTitle.text.toString()

            addProductViewModel.addProduct(
                Product(
                    "",
                    category,
                    description,
                    0.0,
                    0,
                    emptyList(),
                    price,
                    0.0,
                    0,
                    "",
                    title
                ), token!!
            )

            addProductViewModel.isSuccessful.observe(viewLifecycleOwner) {

                if (it) {
                    Snackbar.make(requireView(), "Successfully added!", Snackbar.LENGTH_SHORT)
                        .show()
                    findNavController().popBackStack()
                } else {
                    Snackbar.make(
                        requireView(),
                        "There was an error in insertion",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }


            }

        }

    }

}