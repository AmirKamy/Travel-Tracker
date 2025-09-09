package com.example.linker.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linker.core.model.RegisterParams
import com.linker.core.domain.authusecase.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class Field { FirstName, LastName, Age, BirthDate, Username, Password }

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val register: RegisterUserUseCase
) : ViewModel() {

    var uiState by mutableStateOf(RegisterUiState())
        private set

    // هندلرهای تغییر (فقط همان فیلد را ولیدیت می‌کنیم و فلگ touched را می‌زنیم)
    fun onFirstNameChanged(v: String) {
        uiState = uiState.copy(firstName = v, firstNameTouched = true).validated(changed = Field.FirstName)
    }
    fun onLastNameChanged(v: String) {
        uiState = uiState.copy(lastName = v, lastNameTouched = true).validated(changed = Field.LastName)
    }
    fun onAgeChanged(v: String) {
        uiState = uiState.copy(age = v, ageTouched = true).validated(changed = Field.Age)
    }
    fun onBirthDateChanged(v: String) {
        uiState = uiState.copy(birthDate = v, birthDateTouched = true).validated(changed = Field.BirthDate)
    }
    fun onUsernameChanged(v: String) {
        uiState = uiState.copy(username = v, usernameTouched = true).validated(changed = Field.Username)
    }
    fun onPasswordChanged(v: String) {
        uiState = uiState.copy(password = v, passwordTouched = true).validated(changed = Field.Password)
    }

    // ثبت: همه را touched می‌کنیم و ولیدیشن کامل می‌گیریم
    fun onRegister(onDone: () -> Unit) {
        val s0 = uiState.copy(
            firstNameTouched = true, lastNameTouched = true, ageTouched = true,
            birthDateTouched = true, usernameTouched = true, passwordTouched = true
        )
        val s = s0.validated(all = true)
        if (!s.isValid) { uiState = s; return }

        viewModelScope.launch {
            uiState = s.copy(loading = true, error = null)
            val params = RegisterParams(
                firstName = s.firstName.trim(),
                lastName = s.lastName.trim(),
                age = s.age.toInt(),
                birthDate = s.birthDate.trim(),
                username = s.username.trim(),
                passwordRaw = s.password
            )
            val r = register(params)
            uiState = if (r.isSuccess) s.copy(loading = false)
            else s.copy(loading = false, error = r.exceptionOrNull()?.message)
            if (r.isSuccess) onDone()
        }
    }

    // ولیدیشن: فقط فیلدهای touched (یا فیلد changed) یا حالت all=true
    private fun RegisterUiState.validated(
        changed: Field? = null,
        all: Boolean = false
    ): RegisterUiState {
        val nameRegex = Regex("^[\\p{L} .'-]{2,}$")
        val userRegex = Regex("^[a-zA-Z0-9_.-]{4,}$")
        val passLenOk = password.length >= 8
        val passMixOk = password.any(Char::isDigit) && password.any { it.isLetter() }

        fun need(f: Field, touched: Boolean) = all || touched || changed == f

        val ageInt = age.toIntOrNull()
        val ageErrCalc = when {
            ageInt == null -> "سن معتبر نیست"
            ageInt !in 5..120 -> "سن باید بین 5 تا 120 باشد"
            else -> null
        }
        val birthErrCalc = try {
            val fmt = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            fmt.isLenient = false
            fmt.parse(birthDate.trim()); null
        } catch (_: Exception) { "تاریخ تولد به شکل yyyy-MM-dd" }

        return copy(
            firstNameError = if (need(Field.FirstName, firstNameTouched)) {
                if (firstName.trim().matches(nameRegex)) null else "نام حداقل ۲ کاراکتر"
            } else firstNameError,

            lastNameError = if (need(Field.LastName, lastNameTouched)) {
                if (lastName.trim().matches(nameRegex)) null else "نام خانوادگی حداقل ۲ کاراکتر"
            } else lastNameError,

            ageError = if (need(Field.Age, ageTouched)) ageErrCalc else ageError,

            birthDateError = if (need(Field.BirthDate, birthDateTouched)) birthErrCalc else birthDateError,

            usernameError = if (need(Field.Username, usernameTouched)) {
                if (username.trim().matches(userRegex)) null else "نام کاربری ≥۴، فقط حروف/عدد/._-"
            } else usernameError,

            passwordError = if (need(Field.Password, passwordTouched)) {
                when {
                    !passLenOk -> "رمز ≥ ۸ کاراکتر"
                    !passMixOk -> "رمز شامل حرف و عدد باشد"
                    else -> null
                }
            } else passwordError
        )
    }
}