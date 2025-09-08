package com.example.linker.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val birthDate: String,
    @ColumnInfo(index = true) val username: String,
    val passwordHash: String
)