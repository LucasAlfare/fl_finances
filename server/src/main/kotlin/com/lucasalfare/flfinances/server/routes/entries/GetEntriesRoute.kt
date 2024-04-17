package com.lucasalfare.flfinances.server.routes.entries

import com.lucasalfare.flfinances.server.entriesHandler
import com.lucasalfare.flfinances.server.model.error.Failure
import com.lucasalfare.flfinances.server.model.error.Success
import com.lucasalfare.flfinances.server.routes.getUserIdFromJWT
import com.lucasalfare.flfinances.server.routes.toResponseString
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Defines routes for retrieving entries.
 * These routes are accessed via HTTP GET request.
 *
 * Endpoint URL patterns:
 * - "/flfinances/entries/" for retrieving all entries.
 * - "/flfinances/users/{id}/entries/" for retrieving entries by user ID.
 *
 * When a request is received:
 * 1. Retrieves all entries or entries by user ID using the corresponding method from EntriesHandler.
 * 2. Responds with success and the retrieved entry data if successful.
 * 3. Responds with Not Acceptable status code and the error message if retrieval fails due to unacceptable conditions.
 * 4. Responds with Not Found status code and the error message if the requested resource is not found.
 * 5. Responds with Bad Request status code and the exception message if an unexpected error occurs.
 *
 * @receiver The Route instance to which the routes are added.
 */
fun Route.getEntriesRoute() {
  get("/flfinances/entries/") {
    try {
      when (val result = entriesHandler.getAll()) {
        is Success -> return@get call.respond(HttpStatusCode.OK, result.data)
        is Failure -> return@get call.respond(HttpStatusCode.NotAcceptable, result.error)
      }
    } catch (e: Exception) {
      return@get call.respond(HttpStatusCode.InternalServerError, e.toString())
    }
  }

  get("/flfinances/users/entries/") {
    try {
      val id = call.getUserIdFromJWT() ?: run {
        return@get call.respond(HttpStatusCode.BadRequest, "Bad JWT.")
      }

      when (val result = entriesHandler.getEntriesByUserId(id)) {
        is Success -> return@get call.respond(HttpStatusCode.OK, result.data)
        is Failure -> return@get call.respond(HttpStatusCode.NotFound, result.error)
      }
    } catch (e: Exception) {
      return@get call.respond(HttpStatusCode.BadRequest, e.toResponseString())
    }
  }
}