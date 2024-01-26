package com.matrix.wishlist.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.matrix.wishlist.db.RoomDb
import com.matrix.wishlist.viewmodel.WishListViewModel

class WishListFactory(private val roomDb: RoomDb):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WishListViewModel(roomDb) as T
    }
}