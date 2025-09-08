package com.linker.core.domain.irepository

import com.example.linker.core.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
suspend fun register(user: User): Result<Unit>
suspend fun login(username: String, password: String): Result<User>
suspend fun logout()
fun currentUser(): Flow<User?>
}