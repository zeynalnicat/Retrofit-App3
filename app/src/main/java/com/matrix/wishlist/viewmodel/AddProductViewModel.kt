package com.matrix.wishlist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matrix.wishlist.api.ProductApi
import com.matrix.wishlist.api.RetrofitInstance
import com.matrix.wishlist.model.Product
import kotlinx.coroutines.launch


class AddProductViewModel : ViewModel() {
    val isSuccessful = MutableLiveData<Boolean>()
     fun addProduct(product: Product, token: String) {
        viewModelScope.launch {
            val productApi = RetrofitInstance.getRetrofitInstance()?.create(ProductApi::class.java)
            val response = productApi?.add(product)
            isSuccessful.postValue(response?.isSuccessful)
        }


    }
}