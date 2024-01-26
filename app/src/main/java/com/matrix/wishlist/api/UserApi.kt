package com.matrix.wishlist.api

import com.matrix.wishlist.model.LoginRequest
import com.matrix.wishlist.model.UserDetail
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest):Response<UserDetail>

}