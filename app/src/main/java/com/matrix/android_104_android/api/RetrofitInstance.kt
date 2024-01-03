package com.matrix.android_104_android.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val url = "https://dummyjson.com/"

object RetrofitInstance {
    val userApi = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserApi::class.java)

    fun getProductApi(token: String): ProductApi {
        var productApi: ProductApi? = null
        val okHttpClient =
            OkHttpClient().newBuilder().addInterceptor(TokenInterceptor(token)).build()

        productApi = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApi::class.java)
        return productApi
    }

}


class TokenInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authRequest = request.newBuilder().addHeader(
            "Authorization", "Bearer $token"
        ).build()
        return chain.proceed(authRequest)
    }
}