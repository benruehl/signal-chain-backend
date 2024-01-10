package com.benruehl

import com.benruehl.infrastructure.persistence.tables.Devices
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun setupApplication(
    configureApplication: suspend () -> Unit = { },
    performTest: suspend ApplicationTestBuilder.() -> Unit
) = testApplication {
    application {
        module()
        runBlocking {
            clearDatabase()
            configureApplication()
        }
    }
    performTest()
}

private fun clearDatabase() {
    transaction {
        SchemaUtils.drop(Devices)
        SchemaUtils.create(Devices)
    }
}

suspend inline fun <reified T> HttpResponse.bodyDeserializedAs(): T =
    Json.decodeFromString<T>(this.bodyAsText())
