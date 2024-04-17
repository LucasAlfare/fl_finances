package com.lucasalfare.flfinances.server

import com.lucasalfare.flfinances.server.data.AppDB
import com.lucasalfare.flfinances.server.data.impl.exposed.*
import com.lucasalfare.flfinances.server.model.Credentials
import com.lucasalfare.flfinances.server.model.error.Failure
import com.lucasalfare.flfinances.server.model.error.Success
import com.lucasalfare.flfinances.server.routes.attachments.createAttachmentRoute
import com.lucasalfare.flfinances.server.routes.attachments.getAttachmentsRoute
import com.lucasalfare.flfinances.server.routes.entries.createEntryRoute
import com.lucasalfare.flfinances.server.routes.entries.getEntriesRoute
import com.lucasalfare.flfinances.server.routes.loginRoute
import com.lucasalfare.flfinances.server.routes.users.updatePasswordRoute
import com.lucasalfare.flfinances.server.security.AppJwtConfig
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SchemaUtils

/**
 * We swap the values of these fields to the desired
 * Service implementation.
 * E.g.: [UsersHandlerExposed], [UsersHandlerMongo], etc.
 *
 * The tests should to use these fields, not set their own.
 */
var usersHandler = UsersHandlerExposed
var entriesHandler = EntriesHandlerExposed
var attachmentsHandler = AttachmentsHandlerExposed

suspend fun main() {
  // Initialize the database and create missing tables and columns
  // TODO: consider abstract this function to an supertype
  AppDB.initialize(
    jdbcUrl = "jdbc:sqlite:./data.db",
    jdbcDriverClassName = "org.sqlite.JDBC",
    username = System.getenv("DATABASE_USERNAME") ?: "admin",
    password = System.getenv("DATABASE_PASSWORD") ?: "admin"
  ) {
    SchemaUtils.createMissingTablesAndColumns(
      UsersTable,
      EntriesTable,
      AttachmentsTable
    )
  }

  runCatching {
    usersHandler.createUser(Credentials("usuario1", "docker123"))
  }

  // Start the embedded server
  embeddedServer(
    factory = Netty,
    port = 9999
  ) {
    configureSerialization()
    configureAuthentication()
    configureRouting()
  }.start(true)
}

/**
 * Configures authentication for the application.
 */
fun Application.configureAuthentication() {
  install(Authentication) {
    jwt("app-jwt-auth") {
      realm = AppJwtConfig.realm

      verifier(AppJwtConfig.verifier)

      // Validate the JWT token
      validate { token ->
        return@validate try {
          val id = token.payload.getClaim("user-id").asString().toInt()
          when (UsersHandlerExposed.checkUserExistenceById(id)) {
            is Success -> JWTPrincipal(token.payload)
            is Failure -> null
            else -> null
          }
        } catch (e: Exception) {
          null
        }
      }

      // Challenge unauthorized requests
      challenge { _, _ ->
        call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired.")
      }
    }
  }
}

/**
 * Configures content serialization for the application.
 */
fun Application.configureSerialization() {
  install(ContentNegotiation) { json(Json { isLenient = false }) }
}

/**
 * Configures routing for the application.
 */
fun Application.configureRouting() {
  routing {
    loginRoute()
    authenticate("app-jwt-auth") {
      updatePasswordRoute()
      createEntryRoute()
      getEntriesRoute()
      createAttachmentRoute()
      getAttachmentsRoute()
    }
  }
}