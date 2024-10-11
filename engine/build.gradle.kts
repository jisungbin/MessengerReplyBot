// Copyright 2024 Ji Sungbin
// SPDX-License-Identifier: Apache-2.0
plugins {
  id("com.android.library")
  kotlin("android")
}

android {
  namespace = "land.sungbin.replybot.engine"
  compileSdk = libs.versions.targetSdk.get().toInt()

  defaultConfig {
    minSdk = 24
  }

  compileOptions {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.jdk.get().toInt())
    targetCompatibility = JavaVersion.toVersion(libs.versions.jdk.get().toInt())
  }

  sourceSets {
    getByName("main").java.srcDir("src/main/kotlin")
  }

  packaging {
    resources {
      excludes.add("**/*.kotlin_builtins")
    }
  }
}

kotlin {
  explicitApi()
}

dependencies {
  api(libs.okio)
  compileOnly(libs.javet) { because("Interception annotations") }
}
