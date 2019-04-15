package miningtool.examples

import miningtool.common.Node
import miningtool.common.postOrder
import miningtool.parse.antlr.cpp.CppParser
import miningtool.paths.PathWorker
import org.junit.Assert
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

fun cppTokenizerTest() {
    val folder = "./testData/examples/"


    File(folder).walkTopDown().filter { it.path.endsWith(".cpp") }.forEach { file ->
        val tokens = CppParser().tokenizer(file.inputStream()) ?: return@forEach

        for (t in tokens) {
            print("token ${t.text} ")
        }
        print("\n")
    }
}

fun cppTokenizerAllDataset() {
    val rootFolder = "/home/nicolay/IdeaProjects/antlr/pbr-lib/dataset/ProgramData/"
    val fileToSaveFolder = "/home/nicolay/IdeaProjects/antlr/pbr-lib/dataset/PD_tokens/"

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
            val tokens = CppParser().tokenizer(file.inputStream()) ?: return@forEach

            // concat all file's tokens in one string
            val builder = StringBuilder()
            for (token in tokens) {
                builder.append(token.text)
                builder.append(" ")
            }
            val fileContent: String = builder.toString()
            results.add(file.name + " ` " + fileContent)
        }

        val builder = StringBuilder()
        builder.append("file.txt ` content\n")
        for (file in results) {
            builder.append(file)
            builder.append("\n")
        }
        File(fileToSave).writeText(builder.toString())
    }
}

fun preOrderedTraverseAST(node: Node): String {

    return if (node.isLeaf()) {
        " ${node.getToken()} "
    } else {
        val nodesStringBuilder = StringBuilder()
        nodesStringBuilder.append(" ${node.getToken()} ")
        for (child in node.getChildren()) {
            nodesStringBuilder.append(preOrderedTraverseAST(child))
            nodesStringBuilder.append(" ")
        }

        nodesStringBuilder.toString()
    }
}

fun cppPreOrderedTraverseAllDataset() {
    val rootFolder = "/home/nicolay/IdeaProjects/antlr/pbr-lib/dataset/ProgramData/"
    val fileToSaveFolder = "/home/nicolay/IdeaProjects/antlr/pbr-lib/dataset/PD_pre_ast/"

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
            println("FILE: $i")
            val node = CppParser().parse(file.inputStream()) ?: return@forEach

            val fileContent: String = preOrderedTraverseAST(node)
            results.add(file.name + " ` " + fileContent)
        }

        val builder = StringBuilder()
        builder.append("file.txt ` content\n")
        for (file in results) {
            builder.append(file)
            builder.append("\n")
        }
        File(fileToSave).writeText(builder.toString())
    }
}

fun pathCountCpp() {
    val folder = "./testData/examples/"

    File(folder).walkTopDown().filter { it.path.endsWith("1.cpp") }.forEach { file ->
        val node = CppParser().parse(file.inputStream()) ?: return@forEach

        val nLeaves = node.postOrder().count { it.isLeaf() }

        val allPaths = PathWorker().retrievePaths(node, 10, 4)
        val expectedCount = (nLeaves * (nLeaves - 1)) / 2

        for (path in allPaths) {
            for (each in path.upwardNodes) {
                print(" ${each.getToken()} ")
            }
            print(" | ")
            for (each in path.downwardNodes) {
                print(" ${each.getToken()} ")
            }
            println()
        }


        println("nLeaves = $nLeaves expected = $expectedCount actual = ${allPaths.size}")
    }






}