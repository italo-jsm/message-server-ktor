package com.example.repository.messageuser

import kotlinx.serialization.Serializable

@Serializable
data class MessageUserDto(
    val username: String,
    val email: String,
    val publicKey: String
)