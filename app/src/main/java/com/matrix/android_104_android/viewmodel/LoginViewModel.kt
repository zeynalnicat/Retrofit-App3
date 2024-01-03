package com.matrix.android_104_android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matrix.android_104_android.api.RetrofitInstance
import com.matrix.android_104_android.model.LoginRequest
import com.matrix.android_104_android.model.UserDetail
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _userDetail = MutableLiveData<UserDetail>()
    val userDetail: LiveData<UserDetail>
        get() = _userDetail



     suspend fun checkLogin(username: String, password: String):Boolean {
            val userApi = RetrofitInstance.userApi
            val response = userApi.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                _userDetail.postValue(response.body())
                return true
            }
         return false

    }

    fun checkNull(): Boolean {
        return userDetail.value != null
    }

}