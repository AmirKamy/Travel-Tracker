package com.example.linker.register

data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val age: String = "",
    val birthDate: String = "", // yyyy-MM-dd
    val username: String = "",
    val password: String = "",

    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val ageError: String? = null,
    val birthDateError: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null,

    val firstNameTouched: Boolean = false,
    val lastNameTouched: Boolean = false,
    val ageTouched: Boolean = false,
    val birthDateTouched: Boolean = false,
    val usernameTouched: Boolean = false,
    val passwordTouched: Boolean = false,

    val loading: Boolean = false,
    val error: String? = null, // خطای کلی
) {
    val isValid: Boolean =
        firstNameError == null && lastNameError == null &&
                ageError == null && birthDateError == null &&
                usernameError == null && passwordError == null &&
                firstName.isNotBlank() && lastName.isNotBlank() &&
                age.isNotBlank() && birthDate.isNotBlank() &&
                username.isNotBlank() && password.isNotBlank()
}
