package com.matrix.wishlist.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.matrix.wishlist.db.RoomDb
import com.matrix.wishlist.viewmodel.HomeViewModel

class HomeFactory(private val roomDb: RoomDb):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(roomDb) as T
    }
}