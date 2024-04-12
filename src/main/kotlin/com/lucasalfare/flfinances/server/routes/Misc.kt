package com.lucasalfare.flfinances.server.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

/**
 * Extracts the user ID from the JWT token present in the given [call].
 *
 * @param call The ApplicationCall object representing the current HTTP call.
 * @return The user ID extracted from the JWT token, or null if the JWT token is invalid or doesn't contain a user ID.
 */
fun getUserIdFromJWT(call: ApplicationCall): Int? {
  val principal = call.principal<JWTPrincipal>() ?: return null
  return principal["user-id"]?.toInt()
}