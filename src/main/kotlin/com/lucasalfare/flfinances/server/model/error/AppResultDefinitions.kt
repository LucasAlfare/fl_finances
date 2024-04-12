package com.lucasalfare.flfinances.server.model.error

/**
 * Represents an error in the application.
 */
interface AppError

/**
 * Represents the result of an operation in the application.
 *
 * @param D The type of data contained in the result.
 * @param E The type of error that can occur.
 */
interface AppResult<out D, out E : AppError>

/**
 * Represents a successful result of an operation in the application.
 *
 * @param data The data contained in the successful result.
 * @param E The type of error that can occur, constrained by [AppError].
 */
data class Success<D, E : AppError>(val data: D) : AppResult<D, E>

/**
 * Represents a failure result of an operation in the application.
 *
 * @param error The error that occurred during the operation.
 * @param E The type of error that can occur, constrained by [AppError].
 */
data class Failure<D, E : AppError>(val error: E) : AppResult<D, E>
