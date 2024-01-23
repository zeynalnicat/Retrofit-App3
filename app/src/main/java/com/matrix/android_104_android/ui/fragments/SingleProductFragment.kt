package com.matrix.android_104_android.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.matrix.android_104_android.databinding.FragmentSingleProductBinding
import com.matrix.android_104_android.model.Product
import com.matrix.android_104_android.model.ProductRoomModel


class SingleProductFragment : Fragment() {
   private lateinit var binding:FragmentSingleProductBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSingleProductBinding.inflate(inflater)
        setLayout()
        setNavigation()
        return binding.root
    }

    private fun setLayout(){
        arguments?.let {
            val product = it.getSerializable("product") as ProductRoomModel
            Glide.with(binding.root)
                .load(product.images)
                .into(binding.imgProduct)
            binding.ratingBar.rating = product.rating.toFloat()
            binding.txtTitle.text = product.title
            binding.txtBrand.text = product.brand
            binding.txtDescription.text = product.description
            binding.txtStock.text = product.stock.toString()
            binding.txtPrice.text = "$${product.price}"
        }
    }

    private fun setNavigation(){
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }



}