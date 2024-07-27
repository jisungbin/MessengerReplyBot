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
    targetSdk = 34
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
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

kotlin {
  compilerOptions {
    optIn.add("androidx.compose.material3.ExperimentalMaterial3Api")
  }
}

dependencies {
  implementation(libs.androidx.activity)
  implementation(libs.androidx.datastore.core)
  implementation(libs.androidx.datastore.okio)

  implementation(libs.compose.activity)
  implementation(libs.compose.material3)
  implementation(libs.compose.webview)

  implementation(libs.kotlin.coroutines)
  implementation(libs.kotlin.immutableCollections)

  implementation(libs.okio)

  implementation(libs.javet)
  implementation(libs.swc4j)

  implementation(projects.engine)
}
