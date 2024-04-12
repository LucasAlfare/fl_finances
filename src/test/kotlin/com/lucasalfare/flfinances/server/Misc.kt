package com.lucasalfare.flfinances.server

import com.lucasalfare.flfinances.server.model.Attachment
import com.lucasalfare.flfinances.server.model.Credentials
import com.lucasalfare.flfinances.server.model.Entry
import com.lucasalfare.flfinances.server.model.error.AppResult
import com.lucasalfare.flfinances.server.model.error.DatabaseError
import kotlin.random.Random

const val TEST_LOGIN = "test-user"
const val TEST_PASSWORD = "123456"

suspend fun createTestUserInDB(): AppResult<Int, DatabaseError> {
  val credentials = Credentials(TEST_LOGIN, TEST_PASSWORD)
  return usersHandler.createUser(credentials)
}

fun randomEntry(
  amount: Double = Random.nextDouble(10.0, 1000.0),
  date: Long = System.currentTimeMillis(),
  destination: String = "--",
  description: String = "payment",
  hasAttachments: Boolean = false
) = Entry(
  amount = amount,
  date = date,
  destination = destination,
  description = description,
  hasAttachments = hasAttachments
)

fun randomAttachment(): Attachment {
  val content = buildString {
    repeat(5) {
      append("X".repeat(100))
      append("|")
      append(".XYZ")
      append(";")
    }
  }

  return Attachment(content = content)
}