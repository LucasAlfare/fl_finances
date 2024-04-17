package com.lucasalfare.flfinances.server.service

import com.lucasalfare.flfinances.server.data.AppDB
import com.lucasalfare.flfinances.server.data.impl.exposed.AttachmentsTable
import com.lucasalfare.flfinances.server.data.impl.exposed.EntriesTable
import com.lucasalfare.flfinances.server.data.impl.exposed.UsersHandlerExposed
import com.lucasalfare.flfinances.server.data.impl.exposed.UsersTable
import com.lucasalfare.flfinances.server.model.Credentials
import com.lucasalfare.flfinances.server.model.dto.UpdatePasswordRequestDTO
import com.lucasalfare.flfinances.server.model.error.DatabaseError
import com.lucasalfare.flfinances.server.model.error.Failure
import com.lucasalfare.flfinances.server.model.error.Success
import com.lucasalfare.flfinances.server.security.Hashing
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class UsersHandlerExposedTest {

  init {
    AppDB.initialize(
      jdbcUrl = "jdbc:sqlite:./test_data.db",
      jdbcDriverClassName = "org.sqlite.JDBC",
      username = "",
      password = ""
    )
  }

  @BeforeTest
  fun setup() {
    transaction(AppDB.DB) {
      SchemaUtils.createMissingTablesAndColumns(
        UsersTable,
        EntriesTable,
        AttachmentsTable
      )
    }
  }

  @AfterTest
  fun dispose() {
    transaction(AppDB.DB) { SchemaUtils.drop(UsersTable, EntriesTable, AttachmentsTable) }
  }

  @Test
  fun `test createUser success`() = runBlocking {
    val credentials = Credentials("user", "123456")
    val result = UsersHandlerExposed.createUser(credentials)
    assertTrue(result is Success && result.data == 1)
  }

  @Test
  fun `test createUser failure`() = runBlocking {
    val credentials = Credentials("user", "123456")
    UsersHandlerExposed.createUser(credentials)

    val result = UsersHandlerExposed.createUser(credentials)
    assertTrue(result is Failure && result.error == DatabaseError.NotCreated)
  }

  @Test
  fun `test updatePassword success`() = runBlocking {
    val firstCredentials = Credentials("user", "123456")
    val creationResult = UsersHandlerExposed.createUser(firstCredentials)
    val createdUserId = (creationResult as Success).data

    val newPassword = UpdatePasswordRequestDTO("qwerty")
    val updateResult = UsersHandlerExposed.updatePasswordById(createdUserId, newPassword)
    assertTrue(updateResult is Success)

    val getResult = UsersHandlerExposed.getUserById(creationResult.data)
    assertTrue(Hashing.check(newPassword.password, (getResult as Success).data.password))
  }

  @Test
  fun `test updatePassword failure`() = runBlocking {
    val newPassword = UpdatePasswordRequestDTO("123456")
    val updateResult = UsersHandlerExposed.updatePasswordById(1, newPassword)
    assertTrue(updateResult is Failure)
  }

  @Test
  fun `test checkUserExistenceById success`() = runBlocking {
    val firstCredentials = Credentials("user", "123456")
    val creationResult = UsersHandlerExposed.createUser(firstCredentials)
    val createdUserId = (creationResult as Success).data

    val checkResult = UsersHandlerExposed.checkUserExistenceById(createdUserId)
    assertTrue(checkResult is Success)
  }

  @Test
  fun `test checkUserExistenceById failure`() = runBlocking {
    val checkResult = UsersHandlerExposed.checkUserExistenceById(1)
    assertTrue(checkResult is Failure)
  }

  @Test
  fun `test checkCredentials success`() = runBlocking {
    val firstCredentials = Credentials("user", "123456")
    UsersHandlerExposed.createUser(firstCredentials)

    val checkResult = UsersHandlerExposed.checkCredentials(firstCredentials)
    assertTrue(checkResult is Success)
  }

  @Test
  fun `test checkCredentials failure good login bad pass`() = runBlocking {
    val firstCredentials = Credentials("user", "123456")
    UsersHandlerExposed.createUser(firstCredentials)

    val checkResult = UsersHandlerExposed.checkCredentials(Credentials("user", "qwerty"))
    assertTrue(checkResult is Failure)
  }

  @Test
  fun `test checkCredentials failure bad login good pass`() = runBlocking {
    val firstCredentials = Credentials("user", "123456")
    UsersHandlerExposed.createUser(firstCredentials)

    val checkResult = UsersHandlerExposed.checkCredentials(Credentials("wrongLogin", "123456"))
    assertTrue(checkResult is Failure)
  }
}
