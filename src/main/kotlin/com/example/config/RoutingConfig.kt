package com.example.config

import com.example.exception.BaseException
import com.example.repository.message.MessageDto
import com.example.repository.messageuser.MessageUserDto
import com.example.service.MessageService
import com.example.service.UserService
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject
import java.lang.RuntimeException

fun Application.configureRouting() {
    val userService by inject<UserService>()
    val messageService by inject<MessageService>()

    routing {
        post("/users"){
            val user = call.receive<MessageUserDto>()
            try{
                call.respondText( userService.saveUser(user), ContentType.Any, HttpStatusCode.Created)
            }catch (e: BaseException){
                call.respondText(e.message!!, ContentType.Any, e.statusCode)
            }
        }
        get("/users") {
            if (call.request.queryParameters["userId"] != null) {
                val userId = call.request.queryParameters["userId"]
                userId.let {
                    if (it != null) {
                        call.respond(userService.getUserById(it)!!)
                    }
                }
            }
        }
        post("/messages"){
            val message = call.receive<MessageDto>()
            try{
                messageService.saveMessage(message)
                call.respond(HttpStatusCode.Created)
            }catch (e: BaseException){
                call.respondText(e.message!!, ContentType.Any, e.statusCode)
            }
        }
        get("/messages") {
            call.respond(messageService.getMessages())
        }
        get("/messages/{userId}") {
            if(!call.parameters["userId"].isNullOrEmpty()){
                call.parameters["userId"]?.let { it1 -> messageService.getMessagesByUserId(it1) }
                    ?.let { it2 -> call.respond(it2) }
            }
        }
        get("/messages/unread/{userId}") {
            if(!call.parameters["userId"].isNullOrEmpty()){
                call.parameters["userId"]?.let { it1 -> messageService.getUnreadMessagesByUserId(it1) }
                    ?.let { it2 -> call.respond(it2) }
            }
        }
    }
}
