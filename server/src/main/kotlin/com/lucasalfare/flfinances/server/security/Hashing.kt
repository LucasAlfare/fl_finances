package com.lucasalfare.flfinances.server.security

import org.mindrot.jbcrypt.BCrypt

/**
 * Object responsible for providing functions for password hashing using BCrypt.
 */
object Hashing {

  /**
   * Generates a secure hash for the original password using the BCrypt algorithm.
   *
   * @param original The original password to be hashed.
   * @return The generated hash for the original password.
   */
  fun hashed(original: String): String =
    BCrypt.hashpw(original, BCrypt.gensalt())

  /**
   * Checks if the original password matches the provided hash.
   *
   * @param original The original password to be checked.
   * @param hashed The hash of the original password.
   * @return true if the original password matches the hash, false otherwise.
   */
  fun check(original: String, hashed: String) =
    BCrypt.checkpw(original, hashed)
}