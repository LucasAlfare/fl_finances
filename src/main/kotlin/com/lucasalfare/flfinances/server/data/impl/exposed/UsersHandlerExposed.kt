package com.lucasalfare.flfinances.server.data.impl.exposed

import com.lucasalfare.flfinances.server.data.AppDB
import com.lucasalfare.flfinances.server.data.AppServiceAdapter
import com.lucasalfare.flfinances.server.model.Credentials
import com.lucasalfare.flfinances.server.model.User
import com.lucasalfare.flfinances.server.model.dto.UpdatePasswordRequestDTO
import com.lucasalfare.flfinances.server.model.error.AppResult
import com.lucasalfare.flfinances.server.model.error.DatabaseError
import com.lucasalfare.flfinances.server.model.error.Failure
import com.lucasalfare.flfinances.server.model.error.Success
import com.lucasalfare.flfinances.server.security.Hashing
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

/**
 * Object responsible for handling user-related operations in the database.
 */
object UsersHandlerExposed : AppServiceAdapter() {

  override suspend fun createUser(credentials: Credentials): AppResult<Int, DatabaseError> {
    try {
      val id = AppDB.query {
        UsersTable.insertAndGetId {
          it[login] = credentials.login
          it[hashedPassword] = Hashing.hashed(credentials.password)
        }
      }.value
      return Success(id)
    } catch (e: Exception) {
      return Failure(DatabaseError.NotCreated)
    }
  }

  override suspend fun getUserById(id: Int): AppResult<User, DatabaseError> {
    return try {
      val obj = AppDB.query {
        UsersTable.selectAll().where { UsersTable.id eq id }.singleOrNull()?.let {
          User(
            it[UsersTable.id].value,
            it[UsersTable.login],
            it[UsersTable.hashedPassword]
          )
        }
      }
      if (obj != null) Success(obj)
      else throw Exception("Item not found.")
    } catch (e: Exception) {
      Failure(DatabaseError.NotFound)
    }
  }

  override suspend fun updatePasswordById(
    id: Int,
    nextPassword: UpdatePasswordRequestDTO
  ): AppResult<Unit, DatabaseError> {
    return try {
      val numUpdated = AppDB.query {
        UsersTable.update(where = { UsersTable.id eq id }) {
          it[hashedPassword] = Hashing.hashed(nextPassword.password)
        }
      }
      if (numUpdated == 1) Success(Unit) else Failure(DatabaseError.PasswordNotUpdated)
    } catch (e: Exception) {
      Failure(DatabaseError.PasswordNotUpdated)
    }
  }


  override suspend fun checkUserExistenceById(id: Int): AppResult<Unit, DatabaseError> {
    val exists = AppDB.query {
      UsersTable.selectAll().where { UsersTable.id eq id }.singleOrNull()
    } != null

    return if (exists) Success(Unit) else Failure(DatabaseError.NotFound)
  }

  override suspend fun checkCredentials(credentials: Credentials): AppResult<Int, DatabaseError> {
    val loginSearch = AppDB.query {
      UsersTable.selectAll().where { UsersTable.login eq credentials.login }.singleOrNull()
    }

    if (loginSearch == null) return Failure(DatabaseError.NotFound)
    if (Hashing.check(credentials.password, loginSearch[UsersTable.hashedPassword])) {
      return Success(loginSearch[UsersTable.id].value)
    }

    return Failure(DatabaseError.NotFound)
  }
}