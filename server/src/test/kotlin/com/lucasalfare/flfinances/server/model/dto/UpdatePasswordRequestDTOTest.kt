package com.lucasalfare.flfinances.server.model.dto

import kotlin.test.Test
import kotlin.test.assertTrue

class UpdatePasswordRequestDTOTest {

  @Test
  fun `test UpdatePasswordRequestDTO init success`() {
    val password = "123456"

    runCatching {
      UpdatePasswordRequestDTO(password = password)
      assertTrue(true)
    }.onFailure {
      assertTrue(false)
    }
  }

  @Test
  fun `test UpdatePasswordRequestDTO init failure`() {
    val password = "1"

    runCatching {
      UpdatePasswordRequestDTO(password = password)
      assertTrue(false)
    }.onFailure {
      assertTrue(true)
    }
  }
}