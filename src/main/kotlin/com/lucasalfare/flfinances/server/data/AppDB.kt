package com.lucasalfare.flfinances.server.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.util.IsolationLevel
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Object responsible for managing the database connection and operations.
 */
object AppDB {

  /**
   * Late-initialized HikariDataSource variable to hold the database connection pool.
   */
  private lateinit var hikariDataSource: HikariDataSource

  /**
   * Lazily initialized Database instance using the HikariDataSource.
   */
  val DB by lazy { Database.connect(hikariDataSource) }

  /**
   * Initializes the database connection.
   *
   * @param jdbcUrl JDBC URL for the database.
   * @param jdbcDriverClassName JDBC driver class name.
   * @param username Username for the database connection.
   * @param password Password for the database connection.
   */
  fun initialize(
    jdbcUrl: String,
    jdbcDriverClassName: String,
    username: String,
    password: String,
    onFirstTransactionCallback: () -> Unit = {}
  ) {
    // Creating HikariDataSource using provided parameters.
    hikariDataSource = createHikariDataSource(
      jdbcUrl = jdbcUrl,
      jdbcDriverClassName = jdbcDriverClassName,
      username = username,
      password = password
    )

    // Opening a transaction to ensure the database connection is valid.
    transaction(DB) {
      onFirstTransactionCallback()
    }
  }

  /**
   * Shuts down the database connection.
   */
  fun shutdown() {
    hikariDataSource.close()
  }

  /**
   * Executes a suspended transaction with the provided code block.
   *
   * @param queryCodeBlock The code block to be executed within the transaction.
   * @return The result of the transaction.
   */
  suspend fun <T> query(queryCodeBlock: suspend () -> T): T =
    newSuspendedTransaction(context = Dispatchers.IO, db = DB) {
      queryCodeBlock()
    }

  /**
   * Creates a HikariDataSource with the provided parameters.
   *
   * @param jdbcUrl JDBC URL for the database.
   * @param jdbcDriverClassName JDBC driver class name.
   * @param username Username for the database connection.
   * @param password Password for the database connection.
   * @return The created HikariDataSource.
   */
  private fun createHikariDataSource(
    jdbcUrl: String,
    jdbcDriverClassName: String,
    username: String,
    password: String
  ): HikariDataSource {
    // Configuring HikariCP with provided parameters.
    val hikariConfig = HikariConfig().apply {
      this.jdbcUrl = jdbcUrl
      this.driverClassName = jdbcDriverClassName
      this.username = username
      this.password = password
      this.maximumPoolSize = 20
      this.isAutoCommit = true
      this.transactionIsolation = IsolationLevel.TRANSACTION_SERIALIZABLE.name // switch based on datasource?
      this.validate()
    }

    // Creating and returning HikariDataSource.
    return HikariDataSource(hikariConfig)
  }
}