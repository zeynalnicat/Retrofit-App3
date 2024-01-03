package com.matrix.android_104_android.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.matrix.android_104_android.R
import com.matrix.android_104_android.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private var sPreferences: SharedPreferences? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater)
        sPreferences = activity?.getSharedPreferences("UserDetail",Context.MODE_PRIVATE)
        setSharedPreference()
        setLogOut()
        return binding.root
    }

    private fun setSharedPreference() {
       sPreferences?.let {
           binding.txtName.text = it.getString("Name","N/A")
           binding.txtUsername.text = it.getString("Username","N/A")
           Glide.with(binding.root).load(it.getString("ImgProfile","")).into(binding.imgProfile)
       }
    }

    private fun setLogOut(){
        binding.btnLogOut.setOnClickListener {
            val editor = sPreferences?.edit()
            editor?.apply {
                remove("Token")
                remove("Username")
                putBoolean("isLogged",false)
                val check = commit()
                if(check){
                    findNavController().navigate(R.id.action_accountFragment_to_loginFragment)
                }
            }


        }
    }

}