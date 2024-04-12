package com.lucasalfare.flfinances.server.data

import com.lucasalfare.flfinances.server.model.Attachment
import com.lucasalfare.flfinances.server.model.Credentials
import com.lucasalfare.flfinances.server.model.Entry
import com.lucasalfare.flfinances.server.model.User
import com.lucasalfare.flfinances.server.model.dto.UpdatePasswordRequestDTO
import com.lucasalfare.flfinances.server.model.error.AppResult
import com.lucasalfare.flfinances.server.model.error.CredentialsError
import com.lucasalfare.flfinances.server.model.error.DatabaseError
import com.lucasalfare.flfinances.server.model.error.Failure

interface AppService {

  // USER service descriptions

  /**
   * Creates a new user with the provided credentials.
   *
   * @param credentials The credentials containing login and password information for the new user.
   * @return An [AppResult] indicating the success or failure of the user creation operation.
   */
  suspend fun createUser(credentials: Credentials): AppResult<Int, DatabaseError>

  /**
   * Retrieves a user by their ID.
   *
   * @param id The ID of the user to retrieve.
   * @return An [AppResult] containing the user if found, or indicating failure otherwise.
   */
  suspend fun getUserById(id: Int): AppResult<User, DatabaseError>

  /**
   * Updates the password of a user by their ID.
   *
   * @param id The ID of the user whose password needs to be updated.
   * @param nextPassword The new password to be set for the user.
   * @return An [AppResult] indicating the success or failure of the password update operation.
   */
  suspend fun updatePasswordById(id: Int, nextPassword: UpdatePasswordRequestDTO): AppResult<Unit, DatabaseError>

  /**
   * Checks the existence of a user by their ID.
   *
   * @param id The ID of the user to be checked for existence.
   * @return An [AppResult] indicating the success or failure of the user existence check operation.
   */
  suspend fun checkUserExistenceById(id: Int): AppResult<Unit, DatabaseError>

  /**
   * Checks the credentials of a user.
   *
   * @param credentials The credentials to be checked, containing login and password information.
   * @return An [AppResult] indicating the success or failure of the credential validation operation.
   */
  suspend fun checkCredentials(credentials: Credentials): AppResult<Int, CredentialsError>

  // ENTRIES service descriptions

  /**
   * Creates a new entry in the database.
   *
   * Before inserting, this function checks the existence of the related user by the received ID.
   * If the user does not exist, it returns a failure with a NotFound database error.
   *
   * @param entry The entry to be created.
   * @param relatedUserId The ID of the user related to the entry.
   * @return An [AppResult] indicating the success or failure of the entry creation operation.
   */
  suspend fun createEntry(entry: Entry, relatedUserId: Int): AppResult<Int, DatabaseError>

  /**
   * Retrieves all entries from the database.
   *
   * @return An [AppResult] containing a list of entries if successful, or indicating failure otherwise.
   */
  suspend fun getAll(): AppResult<List<Entry>, DatabaseError>

  /**
   * Retrieves entries associated with a specific user ID from the database.
   *
   * @param userId The ID of the user for whom entries are to be retrieved.
   * @return An [AppResult] containing a list of entries if successful, or indicating failure otherwise.
   */
  suspend fun getEntriesByUserId(userId: Int): AppResult<List<Entry>, DatabaseError>

  // ATTACHMENTS service descriptions

  /**
   * Creates a new attachment associated with the specified entry ID.
   *
   * @param attachment The attachment data to be created.
   * @param relatedEntryId The ID of the entry to which the attachment is related.
   * @return An AppResult representing the success or failure of the operation.
   */
  suspend fun createAttachment(attachment: Attachment, relatedEntryId: Int): AppResult<Int, DatabaseError>

  /**
   * Retrieves attachments associated with the specified entry ID.
   *
   * @param entryId The ID of the entry for which attachments are retrieved.
   * @return An AppResult containing a list of attachments if successful, or an error otherwise.
   */
  suspend fun getAttachmentsByEntryId(entryId: Int): AppResult<List<Attachment>, DatabaseError>

  /**
   * Retrieves attachments associated with the specified user ID.
   *
   * @param userId The ID of the user for which attachments are retrieved.
   * @return An AppResult containing a list of attachments if successful, or an error otherwise.
   */
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

  override suspend fun checkCredentials(credentials: Credentials): AppResult<Int, CredentialsError> {
    return Failure(CredentialsError.BadCredentials)
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