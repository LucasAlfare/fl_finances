package com.lucasalfare.flfinances.server.model.error

enum class CredentialsError : AppError {
  BadCredentials,
  WrongCredentials
}