package com.example.service

import com.example.exception.UserEmailExistsException
import com.example.exception.UsernameExistsException
import com.example.repository.messageuser.MessageUser
import com.example.repository.messageuser.MessageUserDto
import com.example.repository.messageuser.UserRepository

interface UserService {
    fun saveUser(messageUser: MessageUserDto) : String
    fun getUsers(): List<MessageUser>
    fun getUserById(userId: String): MessageUser?
}

class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService{
    override fun saveUser(messageUser: MessageUserDto) :String {
        if(userRepository.getUserByEmail(messageUser.email) != null){
            throw UserEmailExistsException("Email already exists")
        }
        if(userRepository.getUserByUsername(messageUser.username) != null){
            throw UserEmailExistsException("Username already exists")
        }
        return userRepository.saveUser(messageUser)
    }

    override fun getUsers(): List<MessageUser> = userRepository.getUsers()
    override fun getUserById(userId: String): MessageUser? = userRepository.getUserById(userId)

}