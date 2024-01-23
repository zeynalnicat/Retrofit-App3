package com.matrix.android_104_android.db.product

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.matrix.android_104_android.model.ProductRoomModel


@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productEntity: ProductEntity):Long

    @Query("SELECT * FROM Products")
    suspend fun getAll():List<ProductEntity>

    @Query("SELECT * FROM Products WHERE title LIKE '%' || :name || '%'")
    suspend fun getSearched(name: String): List<ProductEntity>

    @Query("Select count(id) from products")
    suspend fun checkDb():Int


}