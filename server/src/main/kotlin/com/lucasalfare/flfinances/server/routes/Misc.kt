package com.lucasalfare.flfinances.server.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

/**
 * Extracts the user ID from the JWT token present in the given [call].
 *
 * @return The user ID extracted from the JWT token, or null if the JWT token is invalid or doesn't contain a user ID.
 */
fun ApplicationCall.getUserIdFromJWT(): Int? {
  val principal = principal<JWTPrincipal>() ?: return null
  return principal["user-id"]?.toInt()
}

/**
 * Parses an [Exception] to a formatted string, containing either
 * its cause as its message, when present.
 */
fun Exception.toResponseString(): String {
  val e = this
  return buildString {
    if (e.cause != null) {
      append("Cause:\n\t")
      append(e.cause)
    }

    if (e.message != null) {
      append("\n")
      append("Message:\n\t")
      append(e.message)
    }

    if (this.isEmpty()) append(e.toString())
  }
}