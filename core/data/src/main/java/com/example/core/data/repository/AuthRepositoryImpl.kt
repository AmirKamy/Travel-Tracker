package com.example.core.data.repository

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.core.data.SessionManager
import com.example.core.data.datasource.AuthLocalDataSource
import com.example.linker.core.database.model.UserEntity
import com.example.linker.core.database.model.toModel
import com.example.linker.core.model.RegisterParams
import com.example.linker.core.model.User
import com.linker.core.domain.irepository.AuthRepository
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val ds: AuthLocalDataSource,
    private val session: SessionManager,
) : AuthRepository {
    override suspend fun register(params: RegisterParams): Result<Unit> = runCatching {
        val hash = BCrypt.withDefaults().hashToString(12, params.passwordRaw.toCharArray())
        val entity = UserEntity(
            firstName   = params.firstName,
            lastName    = params.lastName,
            age         = params.age,
            birthDate   = params.birthDate,
            username    = params.username.lowercase(),
            passwordHash= hash
        )
        ds.insert(entity)
    }

    override suspend fun login(username: String, passwordRaw: String): Result<User> = runCatching {
        val e = ds.findByUsername(username.lowercase()) ?: error("کاربر یافت نشد")
        val ok = BCrypt.verifyer().verify(passwordRaw.toCharArray(), e.passwordHash).verified
        if (!ok) error("نام کاربری یا رمز نادرست است")
        e.toModel()
    }


    override suspend fun logout() {
        session.setUser(null)
    }

    override fun currentUser(): Flow<User?> = session.user


    private fun hash(s: String): String =
        MessageDigest.getInstance("SHA-256").digest(s.toByteArray())
            .joinToString("") { "%02x".format(it) }
}