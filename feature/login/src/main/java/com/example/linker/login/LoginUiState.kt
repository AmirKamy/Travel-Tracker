package com.example.linker.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",

    val usernameError: String? = null,
    val passwordError: String? = null,

    val usernameTouched: Boolean = false,
    val passwordTouched: Boolean = false,

    val loading: Boolean = false,
    val error: String? = null,
) {
    val isValid: Boolean =
        username.isNotBlank() && password.isNotBlank() &&
        usernameError == null && passwordError == null
}