package land.sungbin.replybot.engine

import okio.Source
import okio.buffer

public data class Profile(
  public val name: String,
  public val picture: Source,
) {
  public fun getPictureBase64(): String =
    picture.buffer().use { it.readByteString() }.base64()

  public fun getPictureBytes(): ByteArray =
    picture.buffer().use { it.readByteArray() }
}
