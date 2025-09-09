package com.example.linker.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RegisterRoute(onRegistered: () -> Unit, vm: RegisterViewModel = hiltViewModel()) {
    RegisterScreen(
        state = vm.uiState,
        onFirstName = vm::onFirstNameChanged,
        onLastName = vm::onLastNameChanged,
        onAge = vm::onAgeChanged,
        onBirthDate = vm::onBirthDateChanged,
        onUsername = vm::onUsernameChanged,
        onPassword = vm::onPasswordChanged,
        onRegister = { vm.onRegister(onRegistered) }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    state: RegisterUiState,
    onFirstName: (String) -> Unit,
    onLastName: (String) -> Unit,
    onAge: (String) -> Unit,
    onBirthDate: (String) -> Unit,
    onUsername: (String) -> Unit,
    onPassword: (String) -> Unit,
    onRegister: () -> Unit,
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(Modifier.fillMaxWidth()) {
                            Text(
                                "ثبت ‌نام",
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(start = 12.dp),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { p ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(p)
            ) {
                // فرم اسکرول‌شونده
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 88.dp), // جا برای دکمه پایین
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    LabeledField(
                        label = "نام",
                        value = state.firstName,
                        onValueChange = onFirstName,
                        error = state.firstNameError
                    )
                    LabeledField(
                        label = "نام خانوادگی",
                        value = state.lastName,
                        onValueChange = onLastName,
                        error = state.lastNameError
                    )
                    LabeledField(
                        label = "سن",
                        value = state.age,
                        onValueChange = onAge,
                        error = state.ageError,
                        keyboard = KeyboardType.Number
                    )
                    LabeledField(
                        label = "yyyy-MM-dd",
                        value = state.birthDate,
                        onValueChange = onBirthDate,
                        error = state.birthDateError
                    )
                    LabeledField(
                        label = "نام کاربری",
                        value = state.username,
                        onValueChange = onUsername,
                        error = state.usernameError
                    )
                    LabeledField(
                        label = "رمز عبور",
                        value = state.password,
                        onValueChange = onPassword,
                        error = state.passwordError,
                        isPassword = true
                    )

                    if (state.error != null) {
                        Text(
                            state.error,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(Modifier.height(4.dp))
                }

                Box(
                    Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    Button(
                        onClick = onRegister,
                        enabled = state.isValid && !state.loading,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) {
                        if (state.loading) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                        }
                        Text("ثبت ‌نام")
                    }
                }

            }
        }
    }
}

@Composable
private fun LabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    isPassword: Boolean = false,
    keyboard: KeyboardType = KeyboardType.Text
) {
    val visual = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None

    // جهت نوشتار: عددی → LTR ، متنی → ContentOrRtl
    val style = LocalTextStyle.current.merge(
        TextStyle(
            textAlign = TextAlign.Start,
            textDirection =
                TextDirection.ContentOrRtl
        )
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(label) },
        isError = error != null,
        visualTransformation = visual,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboard),
        textStyle = style,
        modifier = Modifier.fillMaxWidth()
    )
    if (error != null) {
        Text(
            error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

