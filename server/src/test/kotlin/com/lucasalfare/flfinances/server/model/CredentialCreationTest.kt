package com.lucasalfare.flfinances.server.model

import kotlin.test.Test
import kotlin.test.assertTrue

class CredentialCreationTest {

  @Test
  fun `test Credentials init success`() {
    val login = "my-name"
    val password = "my-password"

    runCatching {
      Credentials(login = login, password = password)
      assertTrue(true)
    }
  }

  @Test
  fun `test Credentials init failure`() {
    val login = "my-name"
    val password = "123"

    runCatching {
      Credentials(login = login, password = password)
      assertTrue(false)
    }.onFailure {
      assertTrue(true)
    }
  }
}