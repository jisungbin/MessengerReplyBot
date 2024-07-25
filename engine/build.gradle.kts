plugins {
  kotlin("jvm")
}

kotlin {
  explicitApi()
}

dependencies {
  api(libs.okio)
}
