package com.example.linker.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RegisterRoute(onNavigateToLogin: () -> Unit, onRegistered: () -> Unit, vm: RegisterViewModel = hiltViewModel()) {
    RegisterScreen(
        state = vm.uiState,
        onFirstName = vm::onFirstNameChanged,
        onLastName = vm::onLastNameChanged,
        onAge = vm::onAgeChanged,
        onBirthDate = vm::onBirthDateChanged,
        onUsername = vm::onUsernameChanged,
        onPassword = vm::onPasswordChanged,
        onRegister = { vm.onRegister(onRegistered) },
        onNavigateToLogin = onNavigateToLogin
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
    onNavigateToLogin: () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(Modifier.fillMaxWidth()) {
                            Row(
                                Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(start = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.PersonAdd,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "ثبت‌نام",
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
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
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.10f),
                                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.08f),
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(8.dp))
                    Icon(
                        imageVector = Icons.Outlined.Groups,
                        contentDescription = "لوگوی اپ",
                        modifier = Modifier.size(72.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        "به ما بپیوندید",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        "اکانت بساز و از امکانات ثبت سفر هات لذت ببر",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    // کارت فرم
                    androidx.compose.material3.Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.extraLarge
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
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

                            var showPass by remember { mutableStateOf(false) }
                            OutlinedTextField(
                                value = state.password,
                                onValueChange = onPassword,
                                label = { Text("رمز عبور") },
                                placeholder = { Text("رمز عبور") },
                                isError = state.passwordError != null,
                                singleLine = true,
                                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { showPass = !showPass }) {
                                        Icon(
                                            imageVector = if (showPass) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                            contentDescription = null
                                        )
                                    }
                                },
                                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                modifier = Modifier.fillMaxWidth()
                            )
                            if (state.passwordError != null) {
                                Text(
                                    state.passwordError!!,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            if (state.error != null) {
                                Text(
                                    state.error,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Spacer(Modifier.height(4.dp))
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
                                Icon(Icons.Outlined.CheckCircle, contentDescription = null)
                                Spacer(Modifier.width(6.dp))
                                Text("ثبت ‌نام")
                            }
                        }
                    }

                    Text(
                        "از قبل حساب داری؟ وارد شو و ادامه بده",
                        style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.Underline),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onNavigateToLogin.invoke() }
                    )

                    Spacer(Modifier.height(8.dp))
                }

                Box(
                    Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
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

