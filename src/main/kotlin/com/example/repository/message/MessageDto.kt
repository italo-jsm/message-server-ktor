package com.example.repository.message

import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val payload: String,
    val senderId: String,
    val receiverId: String,
)


