package com.matrix.wishlist.db.category

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "Category", indices = [Index(value = ["name"], unique = true)])
data class CategoryEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)