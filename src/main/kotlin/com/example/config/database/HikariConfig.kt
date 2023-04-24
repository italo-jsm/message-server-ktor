package com.example.config.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

fun createDatasource(): DataSource {
    val config = HikariConfig().also {
        it.jdbcUrl = "jdbc:postgresql://192.168.1.3:5432/postgres"
        it.username = "postgres"
        it.password = "secret"
        it.maximumPoolSize = 1
        it.isReadOnly = false
        it.poolName = "HikariWritePool"
    }
    return HikariDataSource(config)
}