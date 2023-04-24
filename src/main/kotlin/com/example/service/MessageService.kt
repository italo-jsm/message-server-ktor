package com.example.service

import com.example.exception.SenderOrReceiverUnknownException
import com.example.repository.message.Message
import com.example.repository.message.MessageDto
import com.example.repository.message.MessageRepository
import com.example.repository.messageuser.UserRepository

interface MessageService {
    fun saveMessage(messageDto: MessageDto)
    fun getMessages(): List<Message>
    fun getMessagesByUserId(userId: String): List<Message>
    fun getUnreadMessagesByUserId(userId: String): List<Message>
}

class MessageServiceImpl(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository
) : MessageService{
    override fun saveMessage(messageDto: MessageDto) {
        if(userRepository.getUserById(messageDto.senderId) == null || userRepository.getUserById(messageDto.receiverId) == null){
            throw SenderOrReceiverUnknownException("Sender or receiver does not exists in database")
        }
        messageRepository.saveMessage(messageDto)
    }

    override fun getMessages(): List<Message> = messageRepository.getMessages()

    override fun getMessagesByUserId(userId: String): List<Message> {
        val messages = messageRepository.getMessagesByUserId(userId)
        messages.forEach{ if (!it.consumed){
            messageRepository.readMessage(it.id)
        } }
        return messages
    }

    override fun getUnreadMessagesByUserId(userId: String): List<Message> {
        val messages = messageRepository.getUnreadMessagesByUserId(userId)
        messages.forEach {
            messageRepository.readMessage(it.id)
        }
        return messages
    }


}