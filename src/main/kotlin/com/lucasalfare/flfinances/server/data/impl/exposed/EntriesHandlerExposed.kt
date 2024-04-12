package com.lucasalfare.flfinances.server.data.impl.exposed

import com.lucasalfare.flfinances.server.data.AppDB
import com.lucasalfare.flfinances.server.data.AppServiceAdapter
import com.lucasalfare.flfinances.server.model.Entry
import com.lucasalfare.flfinances.server.model.error.AppResult
import com.lucasalfare.flfinances.server.model.error.DatabaseError
import com.lucasalfare.flfinances.server.model.error.Failure
import com.lucasalfare.flfinances.server.model.error.Success
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll

/**
 * Object responsible for handling entry-related operations in the database.
 */
object EntriesHandlerExposed : AppServiceAdapter() {

  override suspend fun createEntry(entry: Entry, relatedUserId: Int): AppResult<Int, DatabaseError> {
    // Before insert, we check user existence by the received ID
    // TODO: refactor this to a side Validator?
    if (UsersHandlerExposed.checkUserExistenceById(relatedUserId) is Failure)
      return Failure(DatabaseError.NotFound)

    try {
      val id = AppDB.query {
        EntriesTable.insertAndGetId {
          it[amount] = entry.amount
          it[date] = entry.date
          it[destination] = entry.destination
          it[description] = entry.description
          it[hasAttachments] = entry.hasAttachments

          /**
           * using the ID from URL instead of the present in the OBJ: verified!
           */
          it[EntriesTable.relatedUserId] = relatedUserId
        }
      }.value
      return Success(id)
    } catch (e: Exception) {
      return Failure(DatabaseError.NotCreated)
    }
  }

  override suspend fun getAll(): AppResult<List<Entry>, DatabaseError> {
    try {
      val items = AppDB.query { EntriesTable.selectAll().map { it.toEntry() } }
      return Success(items.ifEmpty { emptyList() })
    } catch (e: Exception) {
      return Failure(DatabaseError.NotFound)
    }
  }

  override suspend fun getEntriesByUserId(userId: Int): AppResult<List<Entry>, DatabaseError> {
    try {
      val items = AppDB.query {
        EntriesTable
          .selectAll()
          .where { EntriesTable.relatedUserId eq userId }
          .map { it.toEntry() }
      }
      return Success(items)
    } catch (e: Exception) {
      return Failure(DatabaseError.NotFound)
    }
  }
}

/**
 * Extension function to convert a ResultRow to an Entry object.
 */
private fun ResultRow.toEntry() = Entry(
  id = this[EntriesTable.id].value,
  amount = this[EntriesTable.amount],
  date = this[EntriesTable.date],
  destination = this[EntriesTable.destination],
  description = this[EntriesTable.description],
  hasAttachments = this[EntriesTable.hasAttachments],
  relatedUserId = this[EntriesTable.relatedUserId]
)