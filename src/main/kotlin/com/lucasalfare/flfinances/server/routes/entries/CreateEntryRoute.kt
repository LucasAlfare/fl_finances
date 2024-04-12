package com.lucasalfare.flfinances.server.routes.entries

import com.lucasalfare.flfinances.server.entriesHandler
import com.lucasalfare.flfinances.server.model.Entry
import com.lucasalfare.flfinances.server.model.error.Failure
import com.lucasalfare.flfinances.server.model.error.Success
import com.lucasalfare.flfinances.server.routes.getUserIdFromJWT
import com.lucasalfare.flfinances.server.routes.toResponseString
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Defines a route for creating entries associated with users.
 * This route is accessed via HTTP POST request.
 *
 * The endpoint URL pattern is "/flfinances/users/{id}/entries/create".
 *
 * When a request is received:
 * 1. Extracts the user ID from the URL path parameter.
 * 2. Receives the entry data from the request body.
 * 3. Creates the entry using the EntriesHandler.
 * 4. Responds with success and the created entry data if creation is successful.
 * 5. Responds with Not Acceptable status code and the error message if creation fails.
 * 6. Responds with Internal Server Error status code and the exception message if an unexpected error occurs.
 *
 * @receiver The Route instance to which the route is added.
 */
fun Route.createEntryRoute() {
  post("/flfinances/users/entries/create") {
    try {
      val id = call.getUserIdFromJWT() ?: run {
        return@post call.respond(HttpStatusCode.BadRequest, "Bad JWT.")
      }

      val entry = call.receive<Entry>()
      when (val result = entriesHandler.createEntry(entry, id)) {
        is Success -> return@post call.respond(HttpStatusCode.OK, result.data)
        is Failure -> return@post call.respond(HttpStatusCode.NotAcceptable, result.error)
      }
    } catch (e: Exception) {
      return@post call.respond(HttpStatusCode.InternalServerError, e.toResponseString())
    }
  }
}