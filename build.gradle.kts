@file:Suppress("PropertyName")

val ktor_version: String by project

plugins {
  kotlin("jvm") version "1.9.10"
  id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
  application
}

group = "com.lucasalfare"
version = "1.0"

repositories {
  mavenCentral()
}

dependencies {
  // dependências do Ktor (core e motor de fundo)
  implementation("io.ktor:ktor-server-core:$ktor_version")
  implementation("io.ktor:ktor-server-netty:$ktor_version")

  // dependências para habilitar serialização
  implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
  implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

  // dependências para contentnegotiation para CLIENT
  implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")

  // dependências para gerenciamento de JWT
  implementation("io.ktor:ktor-server-auth:$ktor_version")
  implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")

  // por algum motivo JWT está requerindo essa dependência
  implementation("io.ktor:ktor-serialization-jackson:$ktor_version")

  // dependência para criptografar a senha
  implementation("org.mindrot:jbcrypt:0.4")

  // Dependencies for database manipulation
  implementation("org.jetbrains.exposed:exposed-core:0.48.0")
  implementation("org.jetbrains.exposed:exposed-jdbc:0.48.0")
  /*
  Database.connect("jdbc:sqlite:/data/data.db", "org.sqlite.JDBC")
  TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
   */
  implementation("org.xerial:sqlite-jdbc:3.45.2.0")
  implementation("com.zaxxer:HikariCP:5.1.0")

  // isso aqui serve apenas para gerar os logs da engine do servidor...
  implementation("ch.qos.logback:logback-classic:1.5.3")

  testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
  testImplementation("org.jetbrains.kotlin:kotlin-test")
}

//tasks.test {
//  useJUnitPlatform()
//}

kotlin {
  jvmToolchain(17)
}

application {
  // Define the main class for the application.
  mainClass.set("com.lucasalfare.flfinances.server.MainKt")
}

/*
This specifies a custom task when creating a ".jar" for this project.
The main thing is to define manifest and include all dependencies in the final `.jar`.
 */
tasks.withType<Jar> {
  manifest {
    attributes["Main-Class"] = "com.lucasalfare.flfinances.server.MainKt"
  }

  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
  from(configurations.compileClasspath.map { config -> config.map { if (it.isDirectory) it else zipTree(it) } })
}