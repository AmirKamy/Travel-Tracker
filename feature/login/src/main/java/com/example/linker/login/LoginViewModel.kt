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

enum class LoginField { Username, Password }

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
        uiState = uiState.copy(username = value, usernameTouched = true)
            .validated(changed = LoginField.Username)
    }

    fun onPasswordChanged(value: String) {
        uiState = uiState.copy(password = value, passwordTouched = true)
            .validated(changed = LoginField.Password)
    }

    fun onLogin(onSuccess: () -> Unit) {
        val s0 = uiState.copy(usernameTouched = true, passwordTouched = true)
        val s = s0.validated(all = true)
        if (!s.isValid) { uiState = s; return }

        viewModelScope.launch {
            uiState = s.copy(loading = true, error = null)
            val r = login(s.username.trim(), s.password)
            uiState = if (r.isSuccess) s.copy(loading = false)
            else s.copy(loading = false, error = r.exceptionOrNull()?.message)
            if (r.isSuccess) onSuccess()
        }
    }

    private fun LoginUiState.validated(
        changed: LoginField? = null,
        all: Boolean = false
    ): LoginUiState {
        val userRegex = Regex("^[a-zA-Z0-9_.-]{4,}$")

        fun need(f: LoginField, touched: Boolean) = all || touched || changed == f

        val usernameErrCalc =
            if (username.trim().matches(userRegex)) null else "نام کاربری ≥۴، فقط حروف/عدد/._-"

        val passwordErrCalc =
            if (password.isNotBlank()) null else "رمز عبور را وارد کنید"

        return copy(
            usernameError = if (need(LoginField.Username, usernameTouched)) usernameErrCalc else usernameError,
            passwordError = if (need(LoginField.Password, passwordTouched)) passwordErrCalc else passwordError,
        )
    }
}