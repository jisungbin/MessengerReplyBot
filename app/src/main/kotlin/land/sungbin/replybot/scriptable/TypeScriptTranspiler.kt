/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/MessengerReplyBot/blob/trunk/LICENSE
 */

package land.sungbin.replybot.scriptable

import androidx.annotation.WorkerThread
import com.caoccao.javet.swc4j.Swc4j
import com.caoccao.javet.swc4j.enums.Swc4jMediaType
import com.caoccao.javet.swc4j.enums.Swc4jSourceMapOption
import com.caoccao.javet.swc4j.options.Swc4jTranspileOptions
import java.net.URL
import land.sungbin.replybot.components.EditorType

private val transpileEngine by lazy { Swc4j() }

private val mainTranspilerOptions by lazy {
  Swc4jTranspileOptions()
    .setSpecifier(URL("file:///${EditorType.Main.filename}"))
    .setMediaType(Swc4jMediaType.TypeScript)
    .setSourceMap(Swc4jSourceMapOption.None)
}

@WorkerThread fun ts2js(source: String, type: EditorType): String =
  transpileEngine.transpile(
    source,
    if (type == EditorType.Main) mainTranspilerOptions else TODO("Transpiler options for $type"),
  )
    .code
