package com.example.linker.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linker.core.model.User
import com.linker.core.domain.authusecase.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.security.MessageDigest
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val register: RegisterUserUseCase
) : ViewModel() {

    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun onFirstNameChanged(v: String)  { uiState = uiState.copy(firstName = v) }
    fun onLastNameChanged(v: String)   { uiState = uiState.copy(lastName = v) }
    fun onAgeChanged(v: String)        { uiState = uiState.copy(age = v) }
    fun onBirthDateChanged(v: String)  { uiState = uiState.copy(birthDate = v) }
    fun onUsernameChanged(v: String)   { uiState = uiState.copy(username = v) }
    fun onPasswordChanged(v: String)   { uiState = uiState.copy(password = v) }

    fun onRegister(onDone: ()->Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true, error = null)
            val u = User(
                firstName = uiState.firstName,
                lastName  = uiState.lastName,
                age       = uiState.age.toIntOrNull() ?: 0,
                birthDate = uiState.birthDate,
                username  = uiState.username,
                passwordHash = sha256(uiState.password)
            )
            val r = register(u)
            uiState = uiState.copy(loading = false, error = r.exceptionOrNull()?.message)
            if (r.isSuccess) onDone()
        }
    }

    private fun sha256(s: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(s.toByteArray())
            .joinToString("") { "%02x".format(it) }
}



data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val age: String = "",
    val birthDate: String = "",
    val username: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
)