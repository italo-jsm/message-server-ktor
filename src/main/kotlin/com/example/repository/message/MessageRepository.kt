package com.example.repository.message

import com.example.exception.MessageSaveException
import com.example.exception.MessageUserSaveException
import com.example.repository.messageuser.MessageUser
import com.example.repository.messageuser.UserRepositoryImpl
import org.jdbi.v3.core.Jdbi
import java.time.Instant
import java.util.*

interface MessageRepository {
    fun saveMessage(messageDto: MessageDto)
    fun getMessages(): List<Message>
    fun getMessagesByUserId(userId: String): List<Message>
    fun getUnreadMessagesByUserId(userId: String): List<Message>
    fun readMessage(messageId: String)
}

class MessageRepositoryImpl(private val jdbiConnection: Jdbi) : MessageRepository{
    override fun saveMessage(messageDto: MessageDto) {
        val message = Message(
            id = UUID.randomUUID().toString(),
            payload = messageDto.payload,
            senderId = messageDto.senderId,
            receiverId = messageDto.receiverId,
            moment = Instant.now(),
            consumed = false,
            consumedAt = null
        )
        runCatching {
            jdbiConnection.useHandle<MessageUserSaveException> {
                it.createUpdate(INSERT)
                    .bind(Message.ID, message.id)
                    .bind(Message.PAYLOAD, message.payload)
                    .bind(Message.SENDER_ID, message.senderId)
                    .bind(Message.RECEIVER_ID, message.receiverId)
                    .bind(Message.MOMENT, message.moment)
                    .bind(Message.CONSUMED, message.consumed)
                    .bind(Message.CONSUMED_AT, message.consumedAt)
                    .execute()
            }
        }.getOrElse { e ->  throw MessageSaveException(e.message!!)}
    }

    override fun getMessages(): List<Message> {
        var l: List<Message> = emptyList()
        jdbiConnection.useHandle<MessageUserSaveException> {
            l = it.select(SELECT).mapTo(Message::class.java)
                .list()
        }
        return l
    }

    override fun getMessagesByUserId(userId: String): List<Message> {
        var l: List<Message> = emptyList()
        jdbiConnection.useHandle<MessageUserSaveException> {
            l = it.select(SELECT_BY_USER)
                .bind(Message.RECEIVER_ID, userId)
                .mapTo(Message::class.java)
                .list()
        }
        return l
    }

    override fun getUnreadMessagesByUserId(userId: String): List<Message> {
        var l: List<Message> = emptyList()
        jdbiConnection.useHandle<MessageUserSaveException> {
            l = it.select(SELECT_UNREAD)
                .bind(Message.CONSUMED, false)
                .bind(Message.RECEIVER_ID, userId)
                .mapTo(Message::class.java)
                .list()
        }
        return l
    }

    override fun readMessage(messageId: String) {
        jdbiConnection.useHandle<MessageSaveException> {
            it.createUpdate(UPDATE_MESSAGE)
                .bind(Message.CONSUMED, true)
                .bind(Message.CONSUMED_AT, Instant.now())
                .bind(Message.ID, messageId)
                .execute()
        }
    }

    private companion object {
        const val INSERT = """
            INSERT INTO ${Message.ENTITY} (
                ${Message.ID},
                ${Message.PAYLOAD},
                ${Message.SENDER_ID},
                ${Message.RECEIVER_ID},
                ${Message.MOMENT},
                ${Message.CONSUMED},
                ${Message.CONSUMED_AT}
            )
            VALUES (
                :${Message.ID},
                :${Message.PAYLOAD},
                :${Message.SENDER_ID},
                :${Message.RECEIVER_ID},
                :${Message.MOMENT},
                :${Message.CONSUMED},
                :${Message.CONSUMED_AT}
            );
        """

        const val SELECT = """
            SELECT * FROM ${Message.ENTITY}
            """

        const val SELECT_BY_USER = """
            SELECT * FROM ${Message.ENTITY} WHERE ${Message.RECEIVER_ID} = :${Message.RECEIVER_ID}
            """

        const val SELECT_UNREAD = """
            SELECT * FROM ${Message.ENTITY} WHERE ${Message.CONSUMED} = :${Message.CONSUMED} AND ${Message.RECEIVER_ID} = :${Message.RECEIVER_ID}
            """

        const val UPDATE_MESSAGE = """
            UPDATE ${Message.ENTITY} SET ${Message.CONSUMED} = :${Message.CONSUMED}, ${Message.CONSUMED_AT} = :${Message.CONSUMED_AT}
            WHERE ${Message.ID} = :${Message.ID}
            """
    }
}