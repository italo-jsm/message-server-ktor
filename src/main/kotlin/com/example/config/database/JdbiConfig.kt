package com.example.config.database

import com.example.repository.message.MessageMapper
import com.example.repository.messageuser.MessageUserMapper
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.postgres.PostgresPlugin
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import javax.sql.DataSource

fun postgresConnection(dataSource: DataSource): Jdbi =
    Jdbi.create(dataSource)
        .installPlugin(PostgresPlugin())
        .installPlugin(SqlObjectPlugin())
        .registerRowMapper(MessageUserMapper())
        .registerRowMapper(MessageMapper())
