package com.matrix.android_104_android.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.matrix.android_104_android.db.RoomDb
import com.matrix.android_104_android.viewmodel.HomeViewModel

class HomeFactory(private val roomDb: RoomDb):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(roomDb) as T
    }
}