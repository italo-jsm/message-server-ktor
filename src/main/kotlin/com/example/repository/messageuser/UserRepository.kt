package com.example.repository.messageuser

import com.example.exception.MessageUserSaveException
import org.jdbi.v3.core.Jdbi
import java.util.*

interface UserRepository {
    fun saveUser(user: MessageUserDto) : String
    fun getUsers(): List<MessageUser>
    fun getUserById(userId: String) : MessageUser?
    fun getUserByEmail(userEmail: String) : MessageUser?
    fun getUserByUsername(username: String) : MessageUser?
}

class UserRepositoryImpl(private val jdbiConnection: Jdbi) : UserRepository {
    override fun saveUser(userDto: MessageUserDto): String {
        val user = MessageUser(
            id = UUID.randomUUID().toString(),
            username = userDto.username,
            email = userDto.email,
            publicKey = userDto.publicKey
        )
        runCatching {
            jdbiConnection.useHandle<MessageUserSaveException> {
                val t = it.createUpdate(INSERT)
                    .bind(MessageUser.ID, user.id)
                    .bind(MessageUser.USERNAME, user.username)
                    .bind(MessageUser.EMAIL, user.email)
                    .bind(MessageUser.PUBLIC_KEY, user.publicKey)
                t.execute()
            }
        }.getOrElse { e -> throw MessageUserSaveException(e.message!!) }
        return user.id
    }


    override fun getUsers(): List<MessageUser> {
        var l: List<MessageUser> = emptyList()
        jdbiConnection.useHandle<MessageUserSaveException> {
            l = it.select(SELECT).mapTo(MessageUser::class.java)
                .list()
        }
        return l
    }

    override fun getUserById(userId: String): MessageUser? =
        jdbiConnection.withHandle<MessageUser, MessageUserSaveException> {
            it.select(SELECT_USER_BY_ID)
                .bind(MessageUser.ID, userId)
                .mapTo(MessageUser::class.java)
                .findFirst()
                .orElse(null)
        }

    override fun getUserByEmail(userEmail: String): MessageUser? =
        jdbiConnection.withHandle<MessageUser, MessageUserSaveException> {
            it.select(SELECT_USER_BY_EMAIL)
                .bind(MessageUser.EMAIL, userEmail)
                .mapTo(MessageUser::class.java)
                .findFirst()
                .orElse(null)
        }

    override fun getUserByUsername(username: String): MessageUser? =
        jdbiConnection.withHandle<MessageUser, MessageUserSaveException> {
            it.select(SELECT_USER_BY_USERNAME)
                .bind(MessageUser.USERNAME, username)
                .mapTo(MessageUser::class.java)
                .findFirst()
                .orElse(null)
        }

    private companion object {
        const val INSERT = """
            INSERT INTO ${MessageUser.ENTITY} (
                ${MessageUser.ID},
                ${MessageUser.USERNAME},
                ${MessageUser.EMAIL},
                ${MessageUser.PUBLIC_KEY}
            )
            VALUES (
                :${MessageUser.ID},
                :${MessageUser.USERNAME},
                :${MessageUser.EMAIL},
                :${MessageUser.PUBLIC_KEY}
            );
        """

        const val SELECT = """
            SELECT * FROM ${MessageUser.ENTITY}
            """
        const val SELECT_USER_BY_ID = """
            SELECT * FROM ${MessageUser.ENTITY} WHERE ${MessageUser.ID} = :${MessageUser.ID}
            """

        const val SELECT_USER_BY_USERNAME = """
            SELECT * FROM ${MessageUser.ENTITY} WHERE ${MessageUser.USERNAME} = :${MessageUser.USERNAME}
            """

        const val SELECT_USER_BY_EMAIL = """
            SELECT * FROM ${MessageUser.ENTITY} WHERE ${MessageUser.EMAIL} = :${MessageUser.EMAIL}
            """
    }

}
