package miningtool.examples

import miningtool.parse.antlr.cpp.CppParser
import java.io.File

fun allCppFilesToAst() {
    val folder = "./testData/examples/"

    val fileToSave = "../data/cpp_ast.csv"

    val results = mutableListOf<String>()

    File(folder).walkTopDown().filter { it.path.endsWith("1.cpp") }.forEach { file ->
        val node = CppParser().parse(file.inputStream()) ?: return@forEach
        node.prettyPrint()

        val fileContent: String = sbt(node)
        results.add(file.name + "^" + fileContent)
    }

    val builder = StringBuilder()
    builder.append("file Name^content\n")
    for (file in results) {
        builder.append(file)
        builder.append("\n")
    }
    File(fileToSave).writeText(builder.toString())
}

fun allCppFilesToAstTaskClassification() {
    val rootFolder = "../task_classification/PD/"
    val fileToSaveFolder = "../data/task_classification/"

    File(rootFolder).listFiles().forEach { folder ->

        val fileToSave = fileToSaveFolder + folder.name + ".csv"
        println("FOLDER = " + folder.name)

        if (!File(fileToSave).exists()) {
            File(fileToSave).createNewFile();
        }

        val results = mutableListOf<String>()

        var i = 0
        File(rootFolder + folder.name).walkTopDown().filter { it.path.endsWith(".txt") }.forEach { file ->
            i += 1
            println("FILE: " + i)
            val node = CppParser().parse(file.inputStream()) ?: return@forEach

            val fileContent: String = sbt(node)
            results.add(file.name + "^" + fileContent)
        }

        val builder = StringBuilder()
        builder.append("file Name^content\n")
        for (file in results) {
            builder.append(file)
            builder.append("\n")
        }
        File(fileToSave).writeText(builder.toString())
    }
}