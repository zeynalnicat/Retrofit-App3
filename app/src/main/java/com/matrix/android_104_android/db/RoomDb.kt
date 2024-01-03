package com.matrix.android_104_android.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [WishListEntity::class], version = 1)
abstract class RoomDb : RoomDatabase() {
    abstract fun wishListDao(): Dao

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