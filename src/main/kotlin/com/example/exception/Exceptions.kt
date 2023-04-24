package com.example.exception

import io.ktor.http.*

class MessageUserSaveException(message: String) : BaseException(message, HttpStatusCode.InternalServerError)
class MessageSaveException(message: String) : BaseException(message, HttpStatusCode.InternalServerError)
class UsernameExistsException(message: String) : BaseException(message, HttpStatusCode.BadRequest)
class UserEmailExistsException(message: String) : BaseException(message, HttpStatusCode.BadRequest)
class SenderOrReceiverUnknownException(message: String) : BaseException(message, HttpStatusCode.BadRequest)

abstract class BaseException(message: String, httpStatusCode: HttpStatusCode, cause: Throwable? = null) :
    RuntimeException(message, cause) {
    val statusCode = httpStatusCode
}