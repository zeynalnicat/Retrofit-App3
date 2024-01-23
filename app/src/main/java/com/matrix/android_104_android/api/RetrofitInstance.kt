package com.matrix.android_104_android.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val url = "https://dummyjson.com/"

object RetrofitInstance {
    var token = ""
    private var retrofitInstance :Retrofit? = null


    fun getRetrofitInstance():Retrofit?{
        if(retrofitInstance==null){
            val okHttpClient =
                OkHttpClient().newBuilder().addInterceptor(TokenInterceptor()).build()

            retrofitInstance = Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofitInstance
    }



}


class TokenInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authRequest = request.newBuilder().addHeader(
            "Authorization", "Bearer ${RetrofitInstance.token}"
        ).build()
        return chain.proceed(authRequest)
    }
}