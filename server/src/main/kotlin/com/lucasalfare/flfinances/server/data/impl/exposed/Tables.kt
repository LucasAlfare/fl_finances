package com.lucasalfare.flfinances.server.data.impl.exposed

import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Represents the database table for storing user information.
 */
object UsersTable : IntIdTable("Users") {

  /**
   * Represents the column storing user login information.
   */
  val login = text("login").uniqueIndex()

  /**
   * Represents the column storing the hashed password information with a default value of "1234".
   */
  val hashedPassword = text("hashed_password").default("1234")
}

/**
 * Represents the database table for storing entries or transactions.
 */
object EntriesTable : IntIdTable("Entries") {

  /**
   * Represents the column storing the amount of the entry with a default value of 0.0.
   */
  val amount = double("amount").default(0.0)

  /**
   * Represents the column storing the date of the entry with a default value of -1L.
   */
  val date = long("date").default(-1L)

  /**
   * Represents the column storing the destination or recipient of the entry with a default value of an empty string.
   */
  val destination = text("destination").default("")

  /**
   * Represents the column storing the description or note for the entry with a default value of an empty string.
   */
  val description = text("description").default("")

  /**
   * Represents whether the entry has attachments or not, with a default value of false.
   */
  val hasAttachments = bool("has_attachments").default(false)

  /**
   * Represents the column storing the ID of the user related to the entry, referencing the [UsersTable.id].
   */
  val relatedUserId = integer("related_user_id").references(UsersTable.id)
}

/**
 * Represents the database table for storing attachments related to entries.
 */
object AttachmentsTable : IntIdTable("Attachments") {

  /**
   * Represents the content of the attachment.
   */
  val content = largeText("content")

  /**
   * Represents the column storing the ID of the entry related to the attachment, referencing the [EntriesTable.id].
   */
  val relatedEntryId = integer("related_entry_id").references(EntriesTable.id)
}