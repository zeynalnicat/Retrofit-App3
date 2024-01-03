package com.matrix.android_104_android.api

import com.matrix.android_104_android.model.LoginRequest
import com.matrix.android_104_android.model.UserDetail
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest):Response<UserDetail>

}