package miningtool.examples

import miningtool.common.Node
import miningtool.common.toPathContext
import miningtool.parse.antlr.SimpleNode
import miningtool.parse.antlr.java.Java8Parser
import miningtool.paths.PathMiner
import miningtool.paths.PathRetrievalSettings
import miningtool.paths.storage.VocabularyPathStorage
import java.io.File

//Retrieve paths from Java files, using a generated parser.
fun allJavaFiles() {
    val folder = "./testData/examples/"

    val miner = PathMiner(PathRetrievalSettings(5, 5))
    val storage = VocabularyPathStorage()

    File(folder).walkTopDown().filter { it.path.endsWith(".java") }.forEach { file ->
        val node = Java8Parser().parse(file.inputStream()) ?: return@forEach
        if (file.name == "4.5.java") {
            node.prettyPrint()
        }

        val paths = miner.retrievePaths(node)

        storage.store(paths.map { toPathContext(it) }, entityId = file.path)
    }

    storage.save("out_examples/allJavaFilesAntlr")
}

fun allJavaFilesToAstSbt() {
    //val folder = "/home/q1/Documents/Java/git3/spbau_java_hw/Git/src/main/java/ru/itmo/git"
    val folder = "./testData/examples/"

    val fileToSave = "../data/ast.csv"
    //val fileToSave = "../data/ast_with_types.csv"

    val results = mutableListOf<String>()

    File(folder).walkTopDown().filter { it.path.endsWith(".java") }.forEach { file ->
        val node = Java8Parser().parse(file.inputStream()) ?: return@forEach

        node.prettyPrint()

        val fileContent: String = sbt(node)
        results.add(file.name + "^" + fileContent)
    }

    val builder = StringBuilder()
    builder.append("file Name^content\n")
    for (file in results) {
        println("2")
        builder.append(file)
        builder.append("\n")
    }
    File(fileToSave).writeText(builder.toString())
}

fun sbt(node: Node) : String {
    val open = "_OPEN_"
    val close = "_CLOSE_"

    if (node.isLeaf()) {
        //return "$open ${node.getTypeLabel()} ${node.getToken()} $close ${node.getTypeLabel()} ${node.getToken()}"
        return "$open ${node.getToken()} $close ${node.getToken()}"
    } else {
        val builder = StringBuilder()
        for (child: Node in node.getChildren()) {
            builder.append(sbt(child))
            builder.append(" ")
        }
        val childrenInfo = builder.toString()

        //return "$open ${node.getTypeLabel()} ${node.getToken()} $childrenInfo $close ${node.getTypeLabel()} ${node.getToken()}"
        return "$open ${node.getToken()} $childrenInfo $close ${node.getToken()}"
    }
}

fun sbt_nodes(node: Node) : MutableList<Node> {
    val open = SimpleNode("parentheses", null, "OPEN")
    val close = SimpleNode("parentheses", null, "CLOSE")

    var result : MutableList<Node> = ArrayList()

    if (node.isLeaf()) {
        result.add(open)
        result.add(node)
        result.add(close)
        result.add(node)
    }
    else {
        result.add(open)
        result.add(node)

        for (child: Node in node.getChildren()) {
            result.addAll(sbt_nodes(child))
        }

        result.add(close)
        result.add(node)
    }

    return result
}