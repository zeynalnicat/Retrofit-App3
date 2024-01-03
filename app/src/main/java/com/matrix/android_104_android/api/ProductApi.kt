package com.matrix.android_104_android.api


import com.matrix.android_104_android.model.ProductList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {

    @GET("auth/products/categories")
    suspend fun getCategories(): Response<List<String>>

    @GET("auth/products")
    suspend fun getProducts(): Response<ProductList>

    @GET("auth/products/category/{category}")
    suspend fun getSearchedCategory(@Path("category") category: String): Response<ProductList>

    @GET("auth/products/search")
    suspend fun search(@retrofit2.http.Query("q") query: String): Response<ProductList>

}