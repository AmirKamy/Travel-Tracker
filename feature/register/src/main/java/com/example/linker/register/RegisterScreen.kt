package com.example.linker.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RegisterRoute(onRegistered: ()->Unit, vm: RegisterViewModel = hiltViewModel()) {
    RegisterScreen(
        state        = vm.uiState,
        onFirstName  = vm::onFirstNameChanged,
        onLastName   = vm::onLastNameChanged,
        onAge        = vm::onAgeChanged,
        onBirthDate  = vm::onBirthDateChanged,
        onUsername   = vm::onUsernameChanged,
        onPassword   = vm::onPasswordChanged,
        onRegister   = { vm.onRegister(onRegistered) }
    )
}


@Composable
fun RegisterScreen(
    state: RegisterUiState,
    onFirstName: (String)->Unit,
    onLastName: (String)->Unit,
    onAge: (String)->Unit,
    onBirthDate: (String)->Unit,
    onUsername: (String)->Unit,
    onPassword: (String)->Unit,
    onRegister: ()->Unit,
) {
    Scaffold { p ->
        Column(
            Modifier.fillMaxSize().padding(p)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text("ثبت‌نام", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(state.firstName, onFirstName, label = { Text("نام") })
            OutlinedTextField(state.lastName, onLastName, label = { Text("نام خانوادگی") })
            OutlinedTextField(state.age, onAge, label = { Text("سن") })
            OutlinedTextField(state.birthDate, onBirthDate, label = { Text("تاریخ تولد (yyyy-MM-dd)") })
            OutlinedTextField(state.username, onUsername, label = { Text("نام کاربری") })
            OutlinedTextField(
                state.password, onPassword,
                label = { Text("رمز عبور") },
                visualTransformation = PasswordVisualTransformation()
            )

            if (state.error != null)
                Text(state.error!!, color = MaterialTheme.colorScheme.error)

            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onRegister,
                enabled = !state.loading,
                modifier = Modifier.fillMaxWidth()
            ) { Text("ثبت‌نام") }
        }
    }
}
