package com.matrix.wishlist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.matrix.wishlist.db.category.CategoryDao
import com.matrix.wishlist.db.category.CategoryEntity
import com.matrix.wishlist.db.product.ProductDao
import com.matrix.wishlist.db.product.ProductEntity
import com.matrix.wishlist.db.wishlist.Dao
import com.matrix.wishlist.db.wishlist.WishListEntity


@Database(entities = [WishListEntity::class,ProductEntity::class,CategoryEntity::class], version = 1)
abstract class RoomDb : RoomDatabase() {
    abstract fun wishListDao(): Dao
    abstract fun productDao():ProductDao

    abstract fun categoryDao():CategoryDao

    companion object {
        private var INSTANCE: RoomDb? = null
        fun accessDB(context: Context): RoomDb? {
            if (INSTANCE == null) {
                synchronized(RoomDb::class) {
                    INSTANCE =
                        Room.databaseBuilder(
                            context.applicationContext,
                            RoomDb::class.java,
                            "WishList"
                        )
                            .build()
                }
            }
            return INSTANCE
        }
    }
}