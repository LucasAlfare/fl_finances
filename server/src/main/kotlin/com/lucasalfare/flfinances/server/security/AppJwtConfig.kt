package com.lucasalfare.flfinances.server.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm

/**
 * Configuration object for JWT (JSON Web Token) authentication.
 * This object provides methods for generating and verifying JWT tokens.
 */
object AppJwtConfig {

  val realm = System.getenv("REALM")?.toString() ?: "LOCAL-TMP-REALM"
  private val audience = System.getenv("AUDIENCE")?.toString() ?: "LOCAL-TMP-AUDIENCE"
  private val issuer = System.getenv("ISSUER")?.toString() ?: "LOCAL-TMP-ISSUER"
  private val secret = System.getenv("SECRET")?.toString() ?: "LOCAL-TMP-SECRET"

  /**
   * Verifier for JWT tokens.
   * Lazily initialized JWTVerifier configured with the specified audience and issuer.
   */
  val verifier: JWTVerifier by lazy {
    JWT
      .require(Algorithm.HMAC256(secret))
      .withAudience(audience)
      .withIssuer(issuer)
      .build()
  }

  /**
   * Generates a JWT token for the specified user ID.
   * The token includes audience, issuer, and user ID claims.
   *
   * @param id The user ID for whom the token is generated.
   * @return The generated JWT token as a string.
   */
  fun generateJwt(id: Int): String = JWT
    .create()
    .withAudience(audience)
    .withIssuer(issuer)
    .withClaim("user-id", id.toString())
    .sign(Algorithm.HMAC256(secret))
}