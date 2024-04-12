package com.lucasalfare.flfinances.server.model

import kotlin.test.Test
import kotlin.test.assertTrue

class AttachmentCreationTest {

  @Test
  fun `test Attachment init success`() {
    // TODO: test content pattern
    val content = "bn2f48f10847f1840fhg10-4fh1" //just a random string for now

    runCatching {
      Attachment(content = content)
      assertTrue(true)
    }.onFailure {
      assertTrue(false)
    }
  }

  @Test
  fun `test Attachment init failure`() {
    // TODO: test content pattern
    var content = "" //just a random string

    runCatching {
      Attachment(content = content)
      assertTrue(false)
    }.onFailure {
      assertTrue(true)
    }

    content = "   " //just a random string

    runCatching {
      Attachment(content = content)
      assertTrue(false)
    }.onFailure {
      assertTrue(true)
    }
  }
}