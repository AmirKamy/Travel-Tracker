package com.example.linker.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun LoginRoute(onLoggedIn: () -> Unit, vm: LoginViewModel = hiltViewModel()) {
    val user by vm.loggedIn.collectAsState()
    LaunchedEffect(user) { if (user != null) onLoggedIn() }
    LoginScreen(
        state = vm.uiState,
        onUsername = vm::onUsernameChanged,
        onPassword = vm::onPasswordChanged,
        onLogin = vm::onLogin
    )
}


@Composable
fun LoginScreen(state: LoginUiState, onUsername: (String)->Unit, onPassword: (String)->Unit, onLogin: ()->Unit) {
    Scaffold { p ->
        Column(Modifier.fillMaxSize().padding(p).padding(24.dp), verticalArrangement = Arrangement.Center) {
            Text("ورود", style = MaterialTheme.typography.headlineMedium)
            OutlinedTextField(value = state.username, onValueChange = onUsername, label = { Text("نام کاربری") })
            OutlinedTextField(value = state.password, onValueChange = onPassword, label = { Text("رمز عبور") }, visualTransformation = PasswordVisualTransformation())
            if (state.error != null) Text(state.error!!, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(12.dp))
            Button(onClick = onLogin, enabled = !state.loading, modifier = Modifier.fillMaxWidth()) { Text("ورود") }
        }
    }
}