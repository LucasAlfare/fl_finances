package com.lucasalfare.flfinances.server.service

import com.lucasalfare.flfinances.server.createTestUserInDB
import com.lucasalfare.flfinances.server.data.AppDB
import com.lucasalfare.flfinances.server.data.impl.exposed.*
import com.lucasalfare.flfinances.server.model.error.DatabaseError
import com.lucasalfare.flfinances.server.model.error.Failure
import com.lucasalfare.flfinances.server.model.error.Success
import com.lucasalfare.flfinances.server.randomAttachment
import com.lucasalfare.flfinances.server.randomEntry
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class AttachmentsHandlerExposedTest {

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
  fun `test createAttachment success`() = runBlocking {
    val userResult = createTestUserInDB()
    val entry = randomEntry()
    val entryResult = EntriesHandlerExposed.createEntry(entry, (userResult as Success).data)
    val attachment = randomAttachment()
    val attachmentCreateResult = AttachmentsHandlerExposed.createAttachment(attachment, (entryResult as Success).data)
    assertTrue(attachmentCreateResult is Success)
    assertTrue(attachmentCreateResult.data == 1)
  }

  @Test
  fun `test getAttachmentsByEntryId success`() = runBlocking {
    val userResult = createTestUserInDB()
    val entry = randomEntry()
    val entryResult = EntriesHandlerExposed.createEntry(entry, (userResult as Success).data)
    val attachment = randomAttachment()
    AttachmentsHandlerExposed.createAttachment(attachment, (entryResult as Success).data)
    val getResult = AttachmentsHandlerExposed.getAttachmentsByEntryId(entryResult.data)
    assertTrue(getResult is Success)
    assertTrue(getResult.data.size == 1)
  }

  @Test
  fun `test getAttachmentsByEntryId failure`() = runBlocking {
    val getResult = AttachmentsHandlerExposed.getAttachmentsByEntryId(1)
    assertTrue(getResult is Failure)
    assertEquals(expected = getResult.error, actual = DatabaseError.EmptySearch)
  }

  @Test
  fun `test getAttachmentsByUserId success`() = runBlocking {
    val userResult = createTestUserInDB()
    val entry = randomEntry()
    val entryResult = EntriesHandlerExposed.createEntry(entry, (userResult as Success).data)
    val attachment = randomAttachment()
    AttachmentsHandlerExposed.createAttachment(attachment, (entryResult as Success).data)
    val getResult = AttachmentsHandlerExposed.getAttachmentsByUserId(userResult.data)
    assertTrue(getResult is Success)
    assertTrue(getResult.data.size == 1)
  }

  @Test
  fun `test getAttachmentsByUserId failure`() = runBlocking {
    val getResult = AttachmentsHandlerExposed.getAttachmentsByUserId(1)
    assertTrue(getResult is Failure)
    assertEquals(expected = getResult.error, actual = DatabaseError.EmptySearch)
  }
}