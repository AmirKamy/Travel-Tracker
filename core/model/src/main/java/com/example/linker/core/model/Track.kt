package com.example.linker.core.model

data class Track(
    val id: Long = 0,
    val startedAt: Long,
    val endedAt: Long? = null
)