package com.matrix.android_104_android.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.matrix.android_104_android.db.RoomDb
import com.matrix.android_104_android.db.WishListEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WishListViewModel : ViewModel() {
    private val _products = MutableLiveData<List<WishListEntity>>()

    val products: LiveData<List<WishListEntity>>
        get() = _products

    fun getProducts(context: Context) {
        val roomDb = RoomDb.accessDB(context.applicationContext)
        val wishListDao = roomDb?.wishListDao()
        CoroutineScope(Dispatchers.IO).launch {
            val products = wishListDao?.getAll()
            _products.postValue(products ?: emptyList())
        }
    }
}