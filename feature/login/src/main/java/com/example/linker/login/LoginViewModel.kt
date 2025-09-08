package com.example.linker.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linker.core.model.User
import com.linker.core.domain.authusecase.CurrentUserUseCase
import com.linker.core.domain.authusecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val login: LoginUseCase,
    private val currentUser: CurrentUserUseCase,
) : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set

    val loggedIn: StateFlow<User?> =
        currentUser().stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun onUsernameChanged(value: String) {
        uiState = uiState.copy(username = value)
    }

    fun onPasswordChanged(value: String) {
        uiState = uiState.copy(password = value)
    }

    fun onLogin() {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true, error = null)
            val r = login(uiState.username, uiState.password)
            uiState = uiState.copy(loading = false, error = r.exceptionOrNull()?.message)
        }
    }
}


data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
)