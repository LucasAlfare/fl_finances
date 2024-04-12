package com.lucasalfare.flfinances.server.model.dto

import kotlinx.serialization.Serializable

/**
 * Serializable data class representing a request to update the password.
 *
 * @property password The new password to be updated.
 */
@Serializable
data class UpdatePasswordRequestDTO(
  val password: String
) {
  init {
    // Ensures that the password length is at least 6 characters.
    require(password.length >= 6) {
      "Password length must be at least 6 characters."
    }
  }
}