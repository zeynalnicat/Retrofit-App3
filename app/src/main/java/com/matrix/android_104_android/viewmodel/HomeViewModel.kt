package com.matrix.android_104_android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matrix.android_104_android.api.RetrofitInstance
import com.matrix.android_104_android.model.ProductList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _products = MutableLiveData<ProductList>()
    val products: LiveData<ProductList>
        get() = _products

    private val _token = MutableLiveData<String>()
    fun updateToken(newToken: String) {
        _token.value = newToken
    }

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>>
        get() = _categories

    fun getProducts() {
        val productApi = RetrofitInstance.getProductApi(_token.value ?: "")
        CoroutineScope(Dispatchers.IO).launch {
            val response = productApi.getProducts()
            if (response.isSuccessful) {
                _products.postValue(response.body())
            }
        }
    }

    fun getCategories() {
        val productApi = RetrofitInstance.getProductApi(_token.value ?: "")
        viewModelScope.launch {
            val response = productApi.getCategories()
            if (response.isSuccessful) {
                response.body()?.let {
                    val tempList = it.toMutableList()
                    tempList.add(0,"All")
                    _categories.postValue(tempList)
                }
            }

        }
    }

    suspend fun search(query: String): Response<ProductList> {
        val productApi = RetrofitInstance.getProductApi(_token.value ?: "")
        return productApi.search(query)
    }

    fun getDueCategory(category:String){
        val productApi = RetrofitInstance.getProductApi(_token.value?:"")
        viewModelScope.launch {
            val response = productApi.getSearchedCategory(category)
            if(response.isSuccessful){
                _products.postValue(response.body())
            }
        }
    }

    fun updateProducts(query: String){
        viewModelScope.launch {
           val products = search(query)
            _products.postValue(products.body())
        }

    }

}