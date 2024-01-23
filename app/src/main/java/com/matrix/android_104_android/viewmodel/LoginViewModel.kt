package com.matrix.android_104_android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matrix.android_104_android.api.RetrofitInstance
import com.matrix.android_104_android.api.UserApi
import com.matrix.android_104_android.model.LoginRequest
import com.matrix.android_104_android.model.UserDetail
import com.matrix.android_104_android.resource.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _userDetail = MutableLiveData<Resource<UserDetail>>()
    val userDetail: LiveData<Resource<UserDetail>>
        get() = _userDetail



    fun checkLogin(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userApi = RetrofitInstance.getRetrofitInstance()?.create(UserApi::class.java)
                val response = userApi?.login(LoginRequest(username, password))
                response?.let {
                    if (it.isSuccessful) {
                        _userDetail.postValue(Resource.Success(it.body()!!))


                    }else{
                        _userDetail.postValue(Resource.Error(Exception("There was an error while logging in")))
                    }
                }
            }catch (e:Exception){
                _userDetail.postValue(Resource.Error(e))
            }
        }
    }

    fun checkNull(): Boolean {
        return userDetail.value != null
    }

}