package com.lucasalfare.flfinances.server.routes.attachments

import com.lucasalfare.flfinances.server.attachmentsHandler
import com.lucasalfare.flfinances.server.model.error.Failure
import com.lucasalfare.flfinances.server.model.error.Success
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Defines routes for retrieving attachments associated with entries or users.
 * These routes are accessed via HTTP GET request.
 *
 * Endpoint URL patterns:
 * - "/flfinances/entries/{id}/attachments" for retrieving attachments by entry ID.
 * - "/flfinances/users/{id}/attachments" for retrieving attachments by user ID.
 *
 * When a request is received:
 * 1. Extracts the required ID from the URL path parameter.
 * 2. Retrieves attachments using the corresponding method from AttachmentsHandler.
 * 3. Responds with success and the retrieved attachment data if successful.
 * 4. Responds with Internal Server Error status code and the error message if an unexpected error occurs.
 *
 * @receiver The Route instance to which the routes are added.
 */
fun Route.getAttachmentsRoute() {
  get("/flfinances/entries/{id}/attachments") {
    try {
      val entryId = (call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "BAD URL")).toInt()
      when (val result = attachmentsHandler.getAttachmentsByEntryId(entryId)) {
        is Success -> return@get call.respond(HttpStatusCode.OK, result.data)
        is Failure -> return@get call.respond(HttpStatusCode.InternalServerError, result.error)
      }
    } catch (e: Exception) {
      return@get call.respond(HttpStatusCode.InternalServerError, e.toString())
    }
  }

  get("/flfinances/users/{id}/attachments") {
    try {
      val userId = (call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "BAD URL")).toInt()
      when (val result = attachmentsHandler.getAttachmentsByUserId(userId)) {
        is Success -> return@get call.respond(HttpStatusCode.OK, result.data)
        is Failure -> return@get call.respond(HttpStatusCode.InternalServerError, result.error)
      }
    } catch (e: Exception) {
      return@get call.respond(HttpStatusCode.InternalServerError, e.toString())
    }
  }
}