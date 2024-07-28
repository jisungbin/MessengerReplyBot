/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

plugins {
  id("com.android.library")
  kotlin("android")
}

android {
  namespace = "land.sungbin.replybot.engine.katalk"
  compileSdk = 34

  defaultConfig {
    minSdk = 24
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

kotlin {
  explicitApi()
}

dependencies {
  implementation(libs.androidx.core)
  implementation(libs.kotlin.coroutines)

  api(projects.engine)
  api(libs.timber)
}
