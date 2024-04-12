package com.lucasalfare.flfinances.server.routes.users

import com.lucasalfare.flfinances.server.model.dto.UpdatePasswordRequestDTO
import com.lucasalfare.flfinances.server.model.error.Failure
import com.lucasalfare.flfinances.server.model.error.Success
import com.lucasalfare.flfinances.server.routes.getUserIdFromJWT
import com.lucasalfare.flfinances.server.routes.toResponseString
import com.lucasalfare.flfinances.server.usersHandler
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Defines a route for updating user password.
 * This route is accessed via HTTP PATCH request.
 *
 * Endpoint URL pattern:
 * - "/flfinances/users/{id}/update_password" for updating user password.
 *
 * @receiver The Route instance to which the route is added.
 */
fun Route.updatePasswordRoute() {
  patch("/flfinances/users/update_password") {
    try {
      val id = call.getUserIdFromJWT() ?: run {
        return@patch call.respond(HttpStatusCode.BadRequest, "Bad JWT.")
      }

      try {
        val nextPassword = call.receive<UpdatePasswordRequestDTO>()
        when (val result = usersHandler.updatePasswordById(id, nextPassword)) {
          is Success -> return@patch call.respond(HttpStatusCode.OK, "Password successfully updated.")
          is Failure -> return@patch call.respond(HttpStatusCode.BadRequest, result.error)
        }
      } catch (e: Exception) {
        return@patch call.respond(HttpStatusCode.BadRequest, e.toResponseString())
      }
    } catch (e: Exception) {
      return@patch call.respond(HttpStatusCode.BadRequest, e.toResponseString())
    }
  }
}