package com.lucasalfare.flfinances.server.model

import kotlinx.serialization.Serializable


/**
 * Represents a financial transaction entry in the system.
 *
 * @property id The ID of the entry.
 * @property amount The amount of the transaction.
 * @property date The date of the transaction in Unix timestamp format.
 * @property destination The destination or recipient of the transaction.
 * @property description A description or note for the transaction.
 * @property hasAttachments Indicates whether the entry has attachments associated with it.
 * @property relatedUserId The ID of the user related to the entry.
 */
@Serializable
data class Entry(
  val id: Int = -1,
  val amount: Double,
  val date: Long,
  val destination: String,
  val description: String,
  val hasAttachments: Boolean,
  val relatedUserId: Int = -1,
)