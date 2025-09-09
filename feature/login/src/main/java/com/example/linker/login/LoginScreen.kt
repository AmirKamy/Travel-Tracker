package com.example.linker.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
fun LoginRoute(onNavigateToRegister: () -> Unit, onLoggedIn: () -> Unit, vm: LoginViewModel = hiltViewModel()) {
    val user by vm.loggedIn.collectAsState()
    LaunchedEffect(user) { if (user != null) onLoggedIn() }
    LoginScreen(
        onNavigateToRegister = onNavigateToRegister,
        state = vm.uiState,
        onUsername = vm::onUsernameChanged,
        onPassword = vm::onPasswordChanged,
        onLogin = vm::onLogin
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    state: LoginUiState,
    onUsername: (String) -> Unit,
    onPassword: (String) -> Unit,
    onLogin: () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = {
                        Box(Modifier.fillMaxWidth()) {
                            Text(
                                "ورود",
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(start = 12.dp),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                )
            },
        ) { p ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(p)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.08f),
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // هِرو: لوگو + تیتر
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // اگر آیکون اختصاصی داری جایگزینش کن با Image(painterResource(...))
                        Icon(
                            imageVector = Icons.Outlined.Link,
                            contentDescription = "لوگوی اپ",
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "خوش اومدی!",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            "برای ادامه وارد حساب کاربری‌ت شو",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }

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
                            LoginField(
                                label = "نام کاربری",
                                value = state.username,
                                onValueChange = onUsername,
                                error = state.usernameError,
                                keyboard = KeyboardType.Text,
                                textDirection = TextDirection.Ltr
                            )

                            LoginField(
                                label = "رمز عبور",
                                value = state.password,
                                onValueChange = onPassword,
                                error = state.passwordError,
                                isPassword = true,
                                keyboard = KeyboardType.Password,
                                textDirection = TextDirection.Ltr
                            )

                            if (state.error != null) {
                                Text(
                                    state.error,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Spacer(Modifier.height(4.dp))
                            Button(
                                onClick = onLogin,
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
                                Text("ورود")
                            }
                        }
                    }

                    Text(
                        "هنوز حساب نداری؟ همین الان ثبت ‌نام کن",
                        style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.Underline),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onNavigateToRegister.invoke() }
                    )
                }
            }
        }
    }
}


@Composable
private fun LoginField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    isPassword: Boolean = false,
    keyboard: KeyboardType = KeyboardType.Text,
    textDirection: TextDirection = TextDirection.ContentOrRtl
) {
    val visual = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    val style = TextStyle(
        textAlign = TextAlign.Start,
        textDirection = textDirection
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = error != null,
        visualTransformation = visual,
        singleLine = true,
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = keyboard),
        textStyle = style,
        modifier = Modifier.fillMaxWidth()
    )
    if (error != null) {
        Text(
            error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}