/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
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

  lint {
    disable += "ModifierParameter"
  }
}

composeCompiler {
  enableStrongSkippingMode = true
  enableNonSkippingGroupOptimization = true
}

dependencies {
  implementation(libs.androidx.activity)

  implementation(libs.compose.activity)
  implementation(libs.compose.material3)

  implementation(libs.kotlin.coroutines)
  implementation(libs.kotlin.immutableCollections)
}
