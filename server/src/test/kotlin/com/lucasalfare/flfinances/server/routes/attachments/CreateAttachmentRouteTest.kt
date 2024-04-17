package com.lucasalfare.flfinances.server.routes.attachments

import com.lucasalfare.flfinances.server.*
import com.lucasalfare.flfinances.server.data.AppDB
import com.lucasalfare.flfinances.server.data.impl.exposed.AttachmentsTable
import com.lucasalfare.flfinances.server.data.impl.exposed.EntriesTable
import com.lucasalfare.flfinances.server.data.impl.exposed.UsersTable
import com.lucasalfare.flfinances.server.model.Credentials
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class CreateAttachmentRouteTest {

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
  fun `test createAttachmentRoute success`() = testApplication {
    createTestUserInDB()

    application {
      configureAuthentication()
      configureSerialization()
      configureRouting()
    }

    val testClient = createClient { install(plugin = ContentNegotiation) { json(Json { isLenient = false }) } }
    val testCredentials = Credentials(login = TEST_LOGIN, password = TEST_PASSWORD)

    val loginResponse = testClient.post(urlString = "/flfinances/login") {
      contentType(type = ContentType.Application.Json)
      setBody(body = testCredentials)
    }

    val responseJwt = loginResponse.bodyAsText()
    val randomEntry = randomEntry(hasAttachments = true)

    val createEntryResponse = testClient.post("/flfinances/users/entries/create") {
      header(key = HttpHeaders.Authorization, value = "Bearer $responseJwt")
      contentType(type = ContentType.Application.Json)
      setBody(body = randomEntry)
    }

    val randomEntryId = createEntryResponse.body<Int>()

    val createAttachmentResponse = testClient.post("/flfinances/entries/$randomEntryId/attachments/create") {
      header(key = HttpHeaders.Authorization, value = "Bearer $responseJwt")
      contentType(type = ContentType.Application.Json)
      setBody(body = randomAttachment())
    }

    assertEquals(expected = HttpStatusCode.Created, actual = createAttachmentResponse.status)
    assertTrue(actual = createAttachmentResponse.body<Int>() == 1)
  }
}