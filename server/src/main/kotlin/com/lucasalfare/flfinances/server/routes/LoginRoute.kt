package com.lucasalfare.flfinances.server.routes

import com.lucasalfare.flfinances.server.model.Credentials
import com.lucasalfare.flfinances.server.model.error.Failure
import com.lucasalfare.flfinances.server.model.error.Success
import com.lucasalfare.flfinances.server.security.AppJwtConfig
import com.lucasalfare.flfinances.server.usersHandler
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Defines a route for user login.
 * This route is accessed via HTTP POST request.
 *
 * Endpoint URL pattern:
 * - "/flfinances/login" for user login.
 *
 * When a request is received:
 * 1. Receives credentials data from the request body.
 * 2. Checks the credentials using the UsersHandler.
 * 3. Responds with success and a generated JWT token if the credentials are valid.
 * 4. Responds with Not Acceptable status code and the error message if the credentials are not valid.
 * 5. Responds with Bad Request status code and the exception message if an unexpected error occurs.
 *
 * @receiver The Route instance to which the route is added.
 */
fun Route.loginRoute() {
  post("/flfinances/login") {
    try {
      val credentials = call.receive<Credentials>()
      when (val result = usersHandler.checkCredentials(credentials)) {
        is Success -> return@post call.respond(HttpStatusCode.OK, AppJwtConfig.generateJwt(result.data))
        is Failure -> return@post call.respond(HttpStatusCode.NotAcceptable, result.error)
      }
    } catch (e: Exception) {
      return@post call.respond(HttpStatusCode.BadRequest, e.toResponseString())
    }
  }
}