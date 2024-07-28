/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.configuration

import android.content.Context
import androidx.annotation.NonUiContext
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.Closeable
import land.sungbin.replybot.engine.EngineFactory
import land.sungbin.replybot.engine.katalk.KakaoTalkEngine
import land.sungbin.replybot.utils.readOrDefault
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

object AppConfiguration : Closeable {
  // The properties with @PublishedApi should be judged as private.

  @PublishedApi internal lateinit var fs: FileSystem
  @PublishedApi internal lateinit var path: Path

  @PublishedApi internal val moshi = Moshi.Builder().build()
  @PublishedApi internal val configurationType =
    Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)

  val engines: List<EngineFactory>
    field = mutableListOf()

  fun start(@NonUiContext context: Context, fs: FileSystem, directory: Path) {
    check(!::fs.isInitialized || !::path.isInitialized) { "AppConfiguration is already initialized" }
    this.fs = fs
    path = directory.resolve("app_configuration.json".toPath())
    fs.createDirectories(path.parent!!)

    // TODO consider how to add engines dynamically. Perhaps a ServiceLoader is ideal.
    val totalEngines = listOf(KakaoTalkEngine(context))
    val enableEngines = read(AppConfigurationKey.Engines)
    engines += totalEngines.filter { engine -> engine.identifier in enableEngines }
  }

  inline fun <reified T : Any> read(key: AppConfigurationKey<T>): T =
    JsonReader.of(fs.readOrDefault(path) { "{}" }).use { reader ->
      reader.beginObject()
      while (reader.hasNext()) {
        if (reader.nextName() == key.label) {
          val value = reader.readJsonValue()
          return@use moshi.adapter(T::class.java).fromJsonValue(value) ?: key.defaultValue()
        } else {
          reader.skipValue()
        }
      }
      reader.endObject()
      return@use key.defaultValue()
    }

  inline fun <reified T : Any> write(key: AppConfigurationKey<T>, writer: T?.() -> T) {
    val json = fs.readOrDefault(path) { "{}" }
    val adapter = moshi.adapter<Map<String, Any>>(configurationType)
    val map = adapter.fromJson(json).orEmpty().toMutableMap()
    map[key.label] = (map[key.label] as? T).writer()
    fs.write(path) { moshi.adapter<Map<String, Any>>(configurationType).toJson(this, map) }
  }

  override fun close() {
    engines.clear()
  }
}
