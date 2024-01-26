package com.matrix.wishlist.db.category

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao

interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(categoryEntity: CategoryEntity):Long

    @Query("SELECT name from category")
    suspend fun getCategories():List<String>


}