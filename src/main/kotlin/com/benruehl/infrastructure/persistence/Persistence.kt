package com.benruehl.infrastructure.persistence

import com.benruehl.infrastructure.persistence.tables.Devices
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun configurePersistence() {
    val driverClassName = "org.h2.Driver"
    val jdbcURL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
    val database = Database.connect(jdbcURL, driverClassName)
    transaction(database) {
        SchemaUtils.create(Devices)
    }
}

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }
