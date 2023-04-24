package com.example.repository.message

import com.example.common.InstantSerializer
import kotlinx.serialization.Serializable
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.time.Instant

@Serializable
data class Message(
    val id: String,
    val payload: String,
    val senderId: String,
    val receiverId: String,
    @Serializable(with = InstantSerializer::class)
    val moment: Instant = Instant.now(),
    val consumed: Boolean,
    @Serializable(with = InstantSerializer::class)
    val consumedAt: Instant?
){
    companion object {
        const val ENTITY = "MESSAGE"
        const val ID = "ID"
        const val PAYLOAD = "PAYLOAD"
        const val SENDER_ID = "SENDER_ID"
        const val RECEIVER_ID = "RECEIVER_ID"
        const val MOMENT = "MOMENT"
        const val CONSUMED = "CONSUMED"
        const val CONSUMED_AT = "CONSUMED_AT"
    }
}

class MessageMapper : RowMapper<Message> {
    override fun map(rs: ResultSet, ctx: StatementContext?) =
        Message(
            id = rs.getString(Message.ID),
            payload = rs.getString(Message.PAYLOAD),
            senderId = rs.getString(Message.SENDER_ID),
            receiverId = rs.getString(Message.RECEIVER_ID),
            moment = rs.getTimestamp(Message.MOMENT).toInstant(),
            consumed = rs.getBoolean(Message.CONSUMED),
            consumedAt = rs.getTimestamp(Message.CONSUMED_AT)?.toInstant() ?: null
        )
}