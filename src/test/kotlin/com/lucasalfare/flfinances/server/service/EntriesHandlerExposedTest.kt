package com.lucasalfare.flfinances.server.service

import com.lucasalfare.flfinances.server.createTestUserInDB
import com.lucasalfare.flfinances.server.data.AppDB
import com.lucasalfare.flfinances.server.data.impl.exposed.AttachmentsTable
import com.lucasalfare.flfinances.server.data.impl.exposed.EntriesHandlerExposed
import com.lucasalfare.flfinances.server.data.impl.exposed.EntriesTable
import com.lucasalfare.flfinances.server.data.impl.exposed.UsersTable
import com.lucasalfare.flfinances.server.model.error.DatabaseError
import com.lucasalfare.flfinances.server.model.error.Failure
import com.lucasalfare.flfinances.server.model.error.Success
import com.lucasalfare.flfinances.server.randomEntry
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class EntriesHandlerExposedTest {

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
  fun `test createEntry success`() = runBlocking {
    val userResult = createTestUserInDB()
    val entry = randomEntry()

    val createEntryResult = EntriesHandlerExposed.createEntry(entry, (userResult as Success).data)
    assertTrue(createEntryResult is Success)
    assertTrue(createEntryResult.data == 1)
  }

  @Test
  fun `test createEntry failure`() = runBlocking {
    val entry = randomEntry()
    val createEntryResult = EntriesHandlerExposed.createEntry(entry, 2)
    assertTrue(createEntryResult is Failure)
    assertEquals(createEntryResult.error, DatabaseError.NotFound)
  }

  @Test
  fun `test getAll success`() = runBlocking {
    val getAllResult = EntriesHandlerExposed.getAll()
    assertTrue(getAllResult is Success)
    assertTrue(getAllResult.data.isEmpty())
  }

  @Test
  fun `test getEntriesByUserId success`() = runBlocking {
    val userResult = createTestUserInDB()
    val entry = randomEntry()
    EntriesHandlerExposed.createEntry(entry, (userResult as Success).data)
    val getEntriesResult = EntriesHandlerExposed.getEntriesByUserId(userResult.data)
    assertTrue(getEntriesResult is Success)
    assertTrue(getEntriesResult.data.size == 1)
  }


}