package com.linker.core.domain.irepository

import com.example.linker.core.model.RegisterParams
import com.example.linker.core.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
suspend fun register(params: RegisterParams): Result<Unit>
suspend fun login(username: String, passwordRaw: String): Result<User>
suspend fun logout()
fun currentUser(): Flow<User?>
}