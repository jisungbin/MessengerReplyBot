/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/main/LICENSE
 */

plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("plugin.compose")
}

android {
  namespace = "land.sungbin.replybot"
  compileSdk = 34

  defaultConfig {
    minSdk = 23
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
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

composeCompiler {
  enableStrongSkippingMode = true
}

dependencies {
  implementation(libs.androidx.activity)

  implementation(libs.compose.material3)
  implementation(libs.compose.activity)

  implementation(libs.kotlin.coroutines)
  testImplementation(kotlin("test-junit5"))
  testImplementation(libs.test.kotlin.coroutines)
}
