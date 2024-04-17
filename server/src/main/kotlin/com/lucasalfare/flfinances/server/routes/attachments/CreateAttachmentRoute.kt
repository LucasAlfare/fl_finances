package com.lucasalfare.flfinances.server.routes.attachments

import com.lucasalfare.flfinances.server.attachmentsHandler
import com.lucasalfare.flfinances.server.model.Attachment
import com.lucasalfare.flfinances.server.model.error.Failure
import com.lucasalfare.flfinances.server.model.error.Success
import com.lucasalfare.flfinances.server.routes.toResponseString
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Defines a route for creating attachments associated with entries.
 * This route is accessed via HTTP POST request.
 *
 * The endpoint URL pattern is "/flfinances/entries/{entry_id}/attachments/create".
 *
 * When a request is received:
 * 1. Extracts the entry ID from the URL path parameter.
 * 2. Receives the attachment data from the request body.
 * 3. Creates the attachment using the AttachmentsHandler.
 * 4. Responds with success and the created attachment data if creation is successful.
 * 5. Responds with Not Acceptable status code and the error message if creation fails.
 * 6. Responds with Internal Server Error status code and the exception message if an unexpected error occurs.
 *
 * @receiver The Route instance to which the route is added.
 */
fun Route.createAttachmentRoute() {
  post("/flfinances/entries/{entry_id}/attachments/create") {
    try {
      val entryId =
        (call.parameters["entry_id"] ?: return@post call.respond(HttpStatusCode.BadRequest, "BAD URL")).toInt()
      val attachment = call.receive<Attachment>()
      when (val result = attachmentsHandler.createAttachment(attachment, entryId)) {
        is Success -> return@post call.respond(HttpStatusCode.Created, result.data)
        is Failure -> return@post call.respond(HttpStatusCode.NotAcceptable, result.error)
      }
    } catch (e: Exception) {
      return@post call.respond(HttpStatusCode.InternalServerError, e.toResponseString())
    }
  }
}