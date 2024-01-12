package com.benruehl

import com.benruehl.infrastructure.persistence.tables.Devices
import com.benruehl.infrastructure.persistence.tables.Links
import io.ktor.client.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun setupApplication(
    configureApplication: suspend () -> Unit = { },
    performTest: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit
) = testApplication {
    application {
        module()
        runBlocking {
            clearDatabase()
            configureApplication()
        }
    }

    // Configure client here and pass to tests.
    // Better would be to configure the build-in client of
    // ApplicationTestBuilder but this is currently not supported.
    // See https://youtrack.jetbrains.com/issue/KTOR-4005/testApplication-Having-shared-HTTP-client-for-each-test
    val client = createClient {
        install(ContentNegotiation) {
            json()
        }
    }
    performTest(client)
}

private fun clearDatabase() {
    transaction {
        SchemaUtils.drop(Links)
        SchemaUtils.drop(Devices)
        SchemaUtils.create(Devices)
        SchemaUtils.create(Links)
    }
}

suspend inline fun <reified T> HttpResponse.bodyDeserializedAs(): T =
    Json.decodeFromString<T>(this.bodyAsText())
