package com.benruehl

import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.test.*

class ApplicationTest {

    @Test
    fun testRoot() = setupApplication {
        client.get("/").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }
}
