package com.example.core.data.datasource

import com.example.linker.core.database.dao.UserDao
import com.example.linker.core.database.model.UserEntity
import javax.inject.Inject

class AuthLocalDataSource @Inject constructor(private val userDao: UserDao) {
suspend fun insert(user: UserEntity) = userDao.insert(user)
suspend fun findByUsername(username: String) = userDao.findByUsername(username)
}