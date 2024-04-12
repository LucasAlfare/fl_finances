package com.lucasalfare.flfinances.server.model.error

/**
 * Enumeration representing various database-related errors that can occur in the application.
 */
enum class DatabaseError : AppError {
  /**
   * Represents an error indicating that the requested resource was not found in the database.
   */
  NotFound,

  /**
   * Represents an error indicating that an entity could not be created in the database.
   */
  NotCreated,

  EmptySearch,

  /**
   * Represents an error indicating that the password update operation failed in the database.
   */
  PasswordNotUpdated
}

fun DatabaseError.toErrorMessage() {

}
