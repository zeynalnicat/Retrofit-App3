package com.matrix.android_104_android.ui.fragments

import android.content.Context
import android.content.SharedPreferences
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
import com.matrix.android_104_android.databinding.FragmentLoginBinding
import com.matrix.android_104_android.ui.MainActivity
import com.matrix.android_104_android.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private var sharedPreference: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        sharedPreference = activity?.getSharedPreferences("UserDetail", Context.MODE_PRIVATE)
        bottomNavigation()
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        setLogin()
        setSharedPreference()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isLogged = sharedPreference?.getBoolean("isLogged", false)
        if (isLogged == true) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }

    }

    private fun setLogin() {
        binding.button.setOnClickListener {
            lifecycleScope.launch {
                val check = loginViewModel.checkLogin(
                    binding.edtUsername.text.toString(),
                    binding.edtPassword.text.toString()
                )
                if (check) {
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                } else {
                    Snackbar.make(
                        requireView(),
                        "Wrong username or password",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setSharedPreference() {
        sharedPreference?.let { sP ->
            if (!loginViewModel.checkNull()) {
                loginViewModel.userDetail.observe(viewLifecycleOwner) {
                    val editor = sP.edit()
                    editor.putString("Token", it.token)
                    editor.putBoolean("isLogged", true)
                    editor.putString("Name", it.firstName + " " + it.lastName)
                    editor.putString("Username", it.username)
                    editor.putString("ImgProfile", it.image)
                    editor.apply()
                }
            }
        }
    }

    private fun bottomNavigation() {
        val activity = requireActivity() as MainActivity
        activity.setBottomNavigation(false)
    }

}