package com.lucasalfare.flfinances.server.model

import kotlinx.serialization.Serializable

/**
 * Represents the credentials required for authentication.
 *
 * @property login The login identifier.
 * @property password The password associated with the login.
 * @throws IllegalArgumentException if the login is empty or consists only of whitespace, or if the password length is less than 6 characters.
 */
@Serializable
data class Credentials(
  val login: String,
  val password: String
) {
  init {
    require(login.isNotEmpty() || login.isNotBlank()) { "Login must not be empty or blank." }
    require(password.length >= 6) { "Password length must be at least 6 characters." }
  }
}