package com.lucasalfare.flfinances.server.routes

import com.lucasalfare.flfinances.server.configureAuthentication
import com.lucasalfare.flfinances.server.configureRouting
import com.lucasalfare.flfinances.server.configureSerialization
import com.lucasalfare.flfinances.server.data.AppDB
import com.lucasalfare.flfinances.server.data.impl.exposed.AttachmentsTable
import com.lucasalfare.flfinances.server.data.impl.exposed.EntriesTable
import com.lucasalfare.flfinances.server.data.impl.exposed.UsersTable
import com.lucasalfare.flfinances.server.model.Credentials
import com.lucasalfare.flfinances.server.usersHandler
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LoginRouteTest {

  init {
    AppDB.initialize(
      jdbcUrl = "jdbc:sqlite:./test_data.db",
      jdbcDriverClassName = "org.sqlite.JDBC",
      username = "",
      password = ""
    )
  }

  @BeforeTest
  fun setup() {
    transaction(AppDB.DB) {
      SchemaUtils.createMissingTablesAndColumns(
        UsersTable,
        EntriesTable,
        AttachmentsTable
      )
    }
  }

  @AfterTest
  fun dispose() {
    transaction(AppDB.DB) { SchemaUtils.drop(UsersTable, EntriesTable, AttachmentsTable) }
  }

  @Test
  fun `test LoginRoute success`() = testApplication {
    application {
      configureAuthentication()
      configureSerialization()
      configureRouting()
    }
    val testClient = createClient { install(ContentNegotiation) { json(Json { isLenient = false }) } }
    val testCredentials = Credentials("user1", "123456")
    usersHandler.createUser(testCredentials)
    val response = testClient.post("/flfinances/login") {
      contentType(ContentType.Application.Json)
      setBody(testCredentials)
    }
    assertEquals(expected = HttpStatusCode.OK, actual = response.status)
  }

  @Test
  fun `test LoginRoute failure`() = testApplication {
    application {
      configureAuthentication()
      configureSerialization()
      configureRouting()
    }
    val testClient = createClient { install(ContentNegotiation) { json(Json { isLenient = false }) } }
    val testCredentials = Credentials("user1", "123456")
    usersHandler.createUser(testCredentials)
    val response = testClient.post("/flfinances/login") {
      contentType(ContentType.Application.Json)
      setBody(Credentials("user1", "qwerty"))
    }
    assertEquals(expected = HttpStatusCode.NotAcceptable, actual = response.status)
  }
}