package com.matrix.android_104_android.db.wishlist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("WishList")
data class WishListEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val brand: String,
    val category: String,
    val description: String,
    val discountPercentage: Double,
    val price: Int,
    val rating: Double,
    val stock: Int,
    val thumbnail: String,
    val title: String,
    val productId: Int
)

