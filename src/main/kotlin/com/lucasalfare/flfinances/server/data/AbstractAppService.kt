package com.lucasalfare.flfinances.server.data

import com.lucasalfare.flfinances.server.model.Attachment
import com.lucasalfare.flfinances.server.model.Credentials
import com.lucasalfare.flfinances.server.model.Entry
import com.lucasalfare.flfinances.server.model.User
import com.lucasalfare.flfinances.server.model.dto.UpdatePasswordRequestDTO
import com.lucasalfare.flfinances.server.model.error.AppResult
import com.lucasalfare.flfinances.server.model.error.DatabaseError
import com.lucasalfare.flfinances.server.model.error.Failure

interface AppService {

  // USER service descriptions

  suspend fun createUser(credentials: Credentials): AppResult<Int, DatabaseError>

  suspend fun getUserById(id: Int): AppResult<User, DatabaseError>

  suspend fun updatePasswordById(id: Int, nextPassword: UpdatePasswordRequestDTO): AppResult<Unit, DatabaseError>

  suspend fun checkUserExistenceById(id: Int): AppResult<Unit, DatabaseError>

  suspend fun checkCredentials(credentials: Credentials): AppResult<Int, DatabaseError>

  // ENTRIES service descriptions

  suspend fun createEntry(entry: Entry, relatedUserId: Int): AppResult<Int, DatabaseError>

  suspend fun getAll(): AppResult<List<Entry>, DatabaseError>

  suspend fun getEntriesByUserId(userId: Int): AppResult<List<Entry>, DatabaseError>

  // ATTACHMENTS service descriptions

  suspend fun createAttachment(attachment: Attachment, relatedEntryId: Int): AppResult<Int, DatabaseError>

  suspend fun getAttachmentsByEntryId(entryId: Int): AppResult<List<Attachment>, DatabaseError>

  suspend fun getAttachmentsByUserId(userId: Int): AppResult<List<Attachment>, DatabaseError>
}

abstract class AppServiceAdapter : AppService {
  override suspend fun createUser(credentials: Credentials): AppResult<Int, DatabaseError> {
    return Failure(DatabaseError.NotFound)
  }

  override suspend fun getUserById(id: Int): AppResult<User, DatabaseError> {
    return Failure(DatabaseError.NotFound)
  }

  override suspend fun updatePasswordById(
    id: Int,
    nextPassword: UpdatePasswordRequestDTO
  ): AppResult<Unit, DatabaseError> {
    return Failure(DatabaseError.NotFound)
  }

  override suspend fun checkUserExistenceById(id: Int): AppResult<Unit, DatabaseError> {
    return Failure(DatabaseError.NotFound)
  }

  override suspend fun checkCredentials(credentials: Credentials): AppResult<Int, DatabaseError> {
    return Failure(DatabaseError.NotFound)
  }

  override suspend fun createEntry(entry: Entry, relatedUserId: Int): AppResult<Int, DatabaseError> {
    return Failure(DatabaseError.NotFound)
  }

  override suspend fun getAll(): AppResult<List<Entry>, DatabaseError> {
    return Failure(DatabaseError.NotFound)
  }

  override suspend fun getEntriesByUserId(userId: Int): AppResult<List<Entry>, DatabaseError> {
    return Failure(DatabaseError.NotFound)
  }

  override suspend fun createAttachment(attachment: Attachment, relatedEntryId: Int): AppResult<Int, DatabaseError> {
    return Failure(DatabaseError.NotFound)
  }

  override suspend fun getAttachmentsByEntryId(entryId: Int): AppResult<List<Attachment>, DatabaseError> {
    return Failure(DatabaseError.NotFound)
  }

  override suspend fun getAttachmentsByUserId(userId: Int): AppResult<List<Attachment>, DatabaseError> {
    return Failure(DatabaseError.NotFound)
  }
}