package com.example.linker.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.linker.core.database.model.UserEntity

@Dao
interface UserDao {
@Insert
suspend fun insert(user: UserEntity): Long
@Query("SELECT * FROM users WHERE username = :u LIMIT 1")
suspend fun findByUsername(u: String): UserEntity?
}