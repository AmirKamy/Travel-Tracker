package com.example.linker.core.model

data class User(
    val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val birthDate: String, // ISO-8601 یا yyyy-MM-dd
    val username: String,
    val passwordHash: String
)