package land.sungbin.replybot.engine.v8

import com.caoccao.javet.annotations.V8Allow
import com.caoccao.javet.annotations.V8Convert
import com.caoccao.javet.annotations.V8Function
import com.caoccao.javet.enums.V8ConversionMode
import com.caoccao.javet.enums.V8ProxyMode
import okio.Source
import okio.buffer

@V8Convert(mode = V8ConversionMode.AllowOnly, proxyMode = V8ProxyMode.Class)
public class V8Source(internal val source: Source) {
  @[V8Allow V8Function]
  public fun readUtf8(): String = source.buffer().use { it.readUtf8() }

  @[V8Allow V8Function]
  public fun readBase64(): String = source.buffer().use { it.readByteString().base64() }

  @[V8Allow V8Function]
  public fun readBytes(): ByteArray = source.buffer().use { it.readByteArray() }
}
