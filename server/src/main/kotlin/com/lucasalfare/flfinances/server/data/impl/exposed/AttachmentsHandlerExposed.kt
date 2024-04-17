package com.lucasalfare.flfinances.server.data.impl.exposed

import com.lucasalfare.flfinances.server.data.AppDB
import com.lucasalfare.flfinances.server.data.AppServiceAdapter
import com.lucasalfare.flfinances.server.model.Attachment
import com.lucasalfare.flfinances.server.model.error.AppResult
import com.lucasalfare.flfinances.server.model.error.DatabaseError
import com.lucasalfare.flfinances.server.model.error.Failure
import com.lucasalfare.flfinances.server.model.error.Success
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll

/**
 * Handler for managing attachments in the application.
 * Provides methods for creating and retrieving attachments from the database.
 */
object AttachmentsHandlerExposed : AppServiceAdapter() {


  override suspend fun createAttachment(attachment: Attachment, relatedEntryId: Int): AppResult<Int, DatabaseError> {
    // TODO: check existence of the related entry ID

    return try {
      val id = AppDB.query {
        AttachmentsTable.insertAndGetId {
          it[content] = attachment.content
          it[AttachmentsTable.relatedEntryId] = relatedEntryId
        }
      }.value
      Success(id)
    } catch (e: Exception) {
      Failure(DatabaseError.NotFound)
    }
  }

  override suspend fun getAttachmentsByEntryId(entryId: Int): AppResult<List<Attachment>, DatabaseError> {
    // TODO: check existence of the related entry ID

    return try {
      val items = AppDB.query {
        AttachmentsTable.selectAll().where { AttachmentsTable.relatedEntryId eq entryId }.map {
          Attachment(
            id = it[AttachmentsTable.id].value,
            relatedEntryId = entryId,
            content = it[AttachmentsTable.content]
          )
        }
      }
      if (items.isNotEmpty()) Success(items)
      else Failure(DatabaseError.EmptySearch)
    } catch (e: Exception) {
      Failure(DatabaseError.NotFound)
    }
  }

  override suspend fun getAttachmentsByUserId(userId: Int): AppResult<List<Attachment>, DatabaseError> {
    // TODO: check existence of the related user ID

    return try {
      val items = AppDB.query {
        (UsersTable innerJoin EntriesTable innerJoin AttachmentsTable)
          .selectAll().where { UsersTable.id eq userId }
          .map {
            Attachment(
              id = it[AttachmentsTable.id].value,
              relatedEntryId = it[AttachmentsTable.relatedEntryId],
              content = it[AttachmentsTable.content]
            )
          }
      }
      if (items.isNotEmpty()) Success(items)
      else Failure(DatabaseError.EmptySearch)
    } catch (e: Exception) {
      Failure(DatabaseError.NotFound)
    }
  }
}