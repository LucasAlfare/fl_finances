package com.lucasalfare.flfinances.server.model

/**
 * Represents a user in the system.
 *
 * @property id The ID of the user.
 * @property login The login name of the user.
 * @property password The password of the user.
 */
data class User(
  val id: Int,
  val login: String,
  val password: String
)