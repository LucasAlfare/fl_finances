rootProject.name = "FL-Finances"

/**
 * Here we define the plugins "globally". This is cool because we can
 * abstract the versions in just one place, instead of applying each
 * one in the target module build file.
 */
pluginManagement {
  repositories {
    gradlePluginPortal()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
  }

  plugins {
    val kotlinVersion = extra["kotlin_version"] as String

    kotlin("jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.serialization") version kotlinVersion
  }
}

include(
  ":server",
  ":mobile"
)