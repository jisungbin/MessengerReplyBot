/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.engine

import okio.Source
import okio.buffer

public data class Profile(
  public val name: String,
  public val id: String?,
  public val picture: Source,
) {
  public fun getPictureBase64(): String =
    picture.buffer().use { it.readByteString() }.base64()

  public fun getPictureBytes(): ByteArray =
    picture.buffer().use { it.readByteArray() }
}
