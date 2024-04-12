package com.lucasalfare.flfinances.server.model

import kotlinx.serialization.Serializable

/**
 * Represents an attachment with its associated content and related entry ID.
 * Attachments are defined with the following format:
 * - "BASE_64_STRING_OF_DATA|FILE_EXTENSION;BASE_64_STRING_OF_DATA|FILE_EXTENSION;BASE_64_STRING_OF_DATA|FILE_EXTENSION;[...]"
 * Where:
 * - "BASE_64_STRING_OF_DATA" is the content of the attachment encoded in Base64.
 * - "FILE_EXTENSION" is the file extension of the encoded content.
 * Each attachment entry is separated by a semicolon.
 * Each Base64 content and its respective extension are separated by a pipe ("|").
 * The server solely stores attachments; it does not provide functions for decoding them.
 * Presentation and decoding functionalities should be implemented by clients.
 *
 * @property id The ID of the attachment.
 * @property relatedEntryId The ID of the entry related to the attachment.
 * @property content The Base64-encoded content of the attachment with associated file extensions.
 */
@Serializable
data class Attachment(
  val id: Int = -1,
  val relatedEntryId: Int = -1,
  val content: String
) {

  init {
    // Ensures that the content is not empty or blank.
    require(content.isNotEmpty() || content.isNotBlank())
    // TODO: Validate content pattern ("CONTENT|EXTENSION;[...]")
  }
}