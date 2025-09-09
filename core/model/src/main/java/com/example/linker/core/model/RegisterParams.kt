package com.example.linker.core.model

data class RegisterParams(
    val firstName: String,
    val lastName: String,
    val age: Int,
    val birthDate: String,
    val username: String,
    val passwordRaw: String
)