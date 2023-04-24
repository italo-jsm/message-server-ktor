package com.example.repository.messageuser

import com.example.repository.messageuser.MessageUser.Companion.EMAIL
import com.example.repository.messageuser.MessageUser.Companion.ID
import com.example.repository.messageuser.MessageUser.Companion.PUBLIC_KEY
import com.example.repository.messageuser.MessageUser.Companion.USERNAME
import kotlinx.serialization.Serializable
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

@Serializable
data class MessageUser (
    val id: String,
    val username: String,
    val email: String,
    val publicKey: String
){
    companion object {
        const val ENTITY = "MESSAGE_USER"
        const val ID = "ID"
        const val USERNAME = "USERNAME"
        const val EMAIL = "EMAIL"
        const val PUBLIC_KEY = "PUBLIC_KEY"
    }
}
class MessageUserMapper : RowMapper<MessageUser> {
    override fun map(rs: ResultSet, ctx: StatementContext?) =
        MessageUser(
            id = rs.getString(ID),
            username = rs.getString(USERNAME),
            email = rs.getString(EMAIL),
            publicKey = rs.getString(PUBLIC_KEY)
        )
}