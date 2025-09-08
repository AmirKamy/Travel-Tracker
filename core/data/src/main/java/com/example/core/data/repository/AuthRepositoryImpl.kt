package com.example.core.data.repository

import com.example.core.data.SessionManager
import com.example.core.data.datasource.AuthLocalDataSource
import com.example.linker.core.database.model.toEntity
import com.example.linker.core.database.model.toModel
import com.example.linker.core.model.User
import com.linker.core.domain.irepository.AuthRepository
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val ds: AuthLocalDataSource,
    private val session: SessionManager,
) : AuthRepository {
    override suspend fun register(user: User): Result<Unit> = runCatching {
        val existing = ds.findByUsername(user.username)
        require(existing == null) { "Username exists" }
        ds.insert(user.toEntity())
    }


    override suspend fun login(username: String, password: String): Result<User> = runCatching {
        val e = ds.findByUsername(username) ?: error("Not found")
        val ok = e.passwordHash == hash(password)
        require(ok) { "Wrong password" }
        val u = e.toModel()
        session.setUser(u)
        u
    }


    override suspend fun logout() {
        session.setUser(null)
    }

    override fun currentUser(): Flow<User?> = session.user


    private fun hash(s: String): String =
        MessageDigest.getInstance("SHA-256").digest(s.toByteArray())
            .joinToString("") { "%02x".format(it) }
}