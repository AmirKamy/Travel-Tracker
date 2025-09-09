package com.linker.core.domain.authusecase

import com.example.linker.core.model.RegisterParams
import com.linker.core.domain.irepository.AuthRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(private val repo: AuthRepository) {
    suspend operator fun invoke(params: RegisterParams) = repo.register(params)
}
class LoginUseCase @Inject constructor(private val repo: AuthRepository) {
suspend operator fun invoke(username: String, password: String) = repo.login(username, password)
}
class LogoutUseCase @Inject constructor(private val repo: AuthRepository) {
suspend operator fun invoke() = repo.logout()
}
class CurrentUserUseCase @Inject constructor(private val repo: AuthRepository) {
operator fun invoke() = repo.currentUser()
}