package com.matrix.android_104_android.db.wishlist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface Dao {
    @Insert
    suspend fun insert(wishListEntity: WishListEntity):Long

    @Query("Select * from wishlist where productId==:id")
    suspend fun checkInDb(id:Int):List<WishListEntity>

    @Query("Select * from wishlist")
    suspend fun getAll():List<WishListEntity>

    @Delete
    suspend fun remove(wishListEntity: WishListEntity)

    @Query("Select * from wishlist where productId=:id")
    suspend fun getId(id:Int): WishListEntity

}