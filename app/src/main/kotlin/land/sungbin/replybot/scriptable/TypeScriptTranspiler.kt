package land.sungbin.replybot.scriptable

import androidx.annotation.WorkerThread
import com.caoccao.javet.swc4j.Swc4j
import com.caoccao.javet.swc4j.enums.Swc4jMediaType
import com.caoccao.javet.swc4j.enums.Swc4jSourceMapOption
import com.caoccao.javet.swc4j.options.Swc4jTranspileOptions
import java.net.URL
import land.sungbin.replybot.components.EditorTab

private val transpileEngine by lazy { Swc4j() }

private val mainTranspilerOptions by lazy {
  Swc4jTranspileOptions()
    .setSpecifier(URL("file:///${EditorTab.Main.filename}"))
    .setMediaType(Swc4jMediaType.TypeScript)
    .setSourceMap(Swc4jSourceMapOption.None)
}

@WorkerThread fun ts2js(source: String, type: EditorTab): String =
  transpileEngine.transpile(
    source,
    if (type == EditorTab.Main) mainTranspilerOptions else TODO("Transpiler options for $type"),
  )
    .code
