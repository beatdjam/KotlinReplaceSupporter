package jp.co.youmeee.app

import java.io.File

fun main(args : Array<String>) {
    val path = File(".").absoluteFile.parent
    val extensions = Language.values().map { it.extension }
    val fileSeq = File(path).walkTopDown().filter { extensions.contains(it.extension) }
    val sourceList = Language.values().map { lang ->
        val list = fileSeq
            .filter { lang.extension == it.extension }
            .map { it }.toList()
        SourceList(lang, list)
    }
    log(sourceList)
}

fun log(result : List<SourceList>) {
    // ファイルリスト出力
    result.flatMap { it.list }.forEach { println(it) }
    // リプレース率出力
    val sizeAll = result.sumBy { it.list.size }.toDouble()
    result.forEach { sourceList ->
        println("---------${sourceList.language}---------")
        println("ファイル数：${sourceList.list.size}")
        if (sourceList.language.isReplaceTarget) {
            val rate = result.first { it.language.isReplaceTarget }
                .let { it.list.size / sizeAll * 100.0 }
            println("リプレース率：$rate %")
        }
    }
}

enum class Language(
    val extension : String,
    val isReplaceTarget : Boolean
) {
    JAVA("java", false),
    KOTLIN("kt", true);
    override fun toString() = this.name.toLowerCase().capitalize()
}

data class SourceList(
    val language : Language,
    val list : List<File>
)