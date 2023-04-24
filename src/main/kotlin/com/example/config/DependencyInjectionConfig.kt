package com.example.config

import com.example.config.database.createDatasource
import com.example.config.database.postgresConnection
import com.example.repository.message.MessageRepository
import com.example.repository.message.MessageRepositoryImpl
import com.example.repository.messageuser.UserRepository
import com.example.repository.messageuser.UserRepositoryImpl
import com.example.service.MessageService
import com.example.service.MessageServiceImpl
import com.example.service.UserService
import com.example.service.UserServiceImpl
import org.koin.dsl.module

fun repositoryModule() = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<MessageRepository> {MessageRepositoryImpl(get())}
}

fun serviceModule() = module {
    single<UserService> { UserServiceImpl(get()) }
    single<MessageService> { MessageServiceImpl(get(), get())}
}

fun postgresModule() = module {
    single { postgresConnection(get()) }
}

fun hikariModule() = module {
    single { createDatasource() }
}



