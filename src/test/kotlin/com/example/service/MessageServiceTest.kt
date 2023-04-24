package com.example.service

import com.example.exception.SenderOrReceiverUnknownException
import com.example.repository.message.MessageDto
import com.example.repository.message.MessageRepository
import com.example.repository.messageuser.MessageUser
import com.example.repository.messageuser.UserRepository
import io.kotest.core.spec.style.StringSpec
import io.ktor.http.*
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class MessageServiceTest :StringSpec({
    val messageRepository: MessageRepository = mockk()
    val userRepository: UserRepository = mockk()
    val messageServiceImpl = MessageServiceImpl(messageRepository, userRepository)

    "should save message when Sender and Receiver Exists" {
        every { userRepository.getUserById(any()) } returns MESSAGE_USER
        every { messageRepository.saveMessage(any())} returns Unit
        messageServiceImpl.saveMessage(MESSAGE_DTO)
    }

    "should throw exception when Sender or Receiver not Exists" {
        every { userRepository.getUserById(any()) } returns null
        every { messageRepository.saveMessage(any())} returns Unit
        val exception = assertThrows<SenderOrReceiverUnknownException> { messageServiceImpl.saveMessage(MESSAGE_DTO) }
        assertEquals(exception.statusCode, HttpStatusCode.BadRequest)
    }
}){
    companion object{
        val MESSAGE_DTO = MessageDto(
            senderId = "senderID",
            receiverId = "receiverID",
            payload = "payload"
        )

        val MESSAGE_USER = MessageUser(
            id = "userId",
            username = "username",
            email = "email",
            publicKey = "publicKey"
        )

    }
}