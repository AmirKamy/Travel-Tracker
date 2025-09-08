package com.example.core.data

import com.example.linker.core.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

//implement with a simple way for test small projects :

@Singleton
class SessionManager @Inject constructor() {
private val _user = MutableStateFlow<User?>(null)
val user: StateFlow<User?> = _user
fun setUser(u: User?) { _user.value = u }
}