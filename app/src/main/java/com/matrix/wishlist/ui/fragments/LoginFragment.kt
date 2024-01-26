package com.matrix.wishlist.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.matrix.wishlist.R
import com.matrix.wishlist.api.RetrofitInstance
import com.matrix.wishlist.databinding.FragmentLoginBinding
import com.matrix.wishlist.resource.Resource
import com.matrix.wishlist.ui.MainActivity
import com.matrix.wishlist.viewmodel.LoginViewModel

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private var sharedPreference: SharedPreferences? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        bottomNavigation()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreference = activity?.getSharedPreferences("UserDetail", Context.MODE_PRIVATE)
        setLogin()
        sharedPreference?.let {
            val isLogged = it.getBoolean("isLogged", false)
            if (isLogged == true) {
                val token = it.getString("Token", "")
                token?.let {
                    RetrofitInstance.token = it
                }
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }


    }

    private fun setLogin() {
        binding.button.setOnClickListener {
            loginViewModel.checkLogin(
                binding.edtUsername.text.toString(),
                binding.edtPassword.text.toString()
            )
        }

        loginViewModel.userDetail.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    sharedPreference?.let { sP ->
                        val editor = sP.edit()
                        editor.putString("Token", it.data.token)
                        RetrofitInstance.token = it.data.token
                        editor.putBoolean("isLogged", true)
                        editor.putString("Name", it.data.firstName + " " + it.data.lastName)
                        editor.putString("Username", it.data.username)
                        editor.putString("ImgProfile", it.data.image)
                        editor.apply()


                    }
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }

                is Resource.Error -> {
                    Snackbar.make(
                        requireView(),
                        it.exception.message!!,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading ->{

                }
            }
        }
    }



private fun bottomNavigation() {
    val activity = requireActivity() as MainActivity
    activity.setBottomNavigation(false)
}

}