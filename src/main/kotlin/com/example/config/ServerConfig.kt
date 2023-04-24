package com.example.config

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun ktorServer() = embeddedServer(Netty, port = 8080){
    installDependencyInjection()
    configureSerialization()
    configureRouting()
}
fun Application.installDependencyInjection() = install(Koin){
    slf4jLogger()
    modules(
        repositoryModule(),
        serviceModule(),
        postgresModule(),
        hikariModule()
    )
}
