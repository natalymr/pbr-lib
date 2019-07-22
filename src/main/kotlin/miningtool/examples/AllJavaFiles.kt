package miningtool.examples

//import kotlinx.serialization.*
//import kotlinx.serialization.json.*

import com.google.gson.GsonBuilder
import miningtool.parse.antlr.java.Java8Parser
import miningtool.parse.antlr.java.XmlParser
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter

//Retrieve paths from Java files, using a generated parser.
fun allJavaFiles() {
    val folder = "./testData/examples/blobs/"

    File(folder).walkTopDown().filter { it.path.endsWith(".java") }.forEach { file ->
        println("Next file ${file.name}")
        val tokens = Java8Parser().tokenizer(file.inputStream()) // ?: return@forEach
    }
}

class BlobsDiff(
        val commit: String,
        val filePath: String,
        val addedTokens: List<String>,
        val deletedTokens: List<String>,
        val message: String
)


fun littleFunctionForTesting() {
    val str = "5d63d6a0ca549aee04f6f4b513439efebe5aef06THIS_STRING_WILL_NEVER_APPEAR_IN_DATASET_AND_IT_WILL_BE_USED_AS_SEPARATORAlexey KudravtsevTHIS_STRING_WILL_NEVER_APPEAR_IN_DATASET_AND_IT_WILL_BE_USED_AS_SEPARATORMTHIS_STRING_WILL_NEVER_APPEAR_IN_DATASET_AND_IT_WILL_BE_USED_AS_SEPARATOREJB/testSource/com/intellij/j2ee/ejb/EjbFindUsagesTest.javaTHIS_STRING_WILL_NEVER_APPEAR_IN_DATASET_AND_IT_WILL_BE_USED_AS_SEPARATORceea14deed58308c157c0bfd12ee683dce12baa7THIS_STRING_WILL_NEVER_APPEAR_IN_DATASET_AND_IT_WILL_BE_USED_AS_SEPARATORb1e23ce637278095feb29eda9bba95340376a831THIS_STRING_WILL_NEVER_APPEAR_IN_DATASET_AND_IT_WILL_BE_USED_AS_SEPARATORintentions in injected langs brace matching in injected langsTHIS_STRING_WILL_NEVER_APPEAR_IN_DATASET_AND_IT_WILL_BE_USED_AS_SEPARATOR"
    val separator = "THIS_STRING_WILL_NEVER_APPEAR_IN_DATASET_AND_IT_WILL_BE_USED_AS_SEPARATOR"

    val list = str.split(separator)
    val commit = list[0]
    val filePath = list[3]
    val old = list[4]
    val new = list[5]
    val msg = list[6]
    val added = listOf("aa", "bb")
    val deleted = listOf("cc", "dd")

    val blobDiff = BlobsDiff(
            commit = commit,
            filePath = filePath,
            addedTokens = added,
            deletedTokens = deleted,
            message = msg)

    for (a in list) {
        println(a)
    }

    val folder = "./testData/examples/blobs/"
//    val folder = "/Users/natalia.murycheva/Documents/gitCommitMessageCollectorStorage/intellij_blobs"

    val blobOld = File(folder + File.separator + old)
    val blobNew = File(folder + File.separator + new)

    val tokensOld = Java8Parser().tokenizerForBlob(blobOld.inputStream()).toSet()
    val tokensNew = Java8Parser().tokenizerForBlob(blobNew.inputStream()).toSet()

    val a = mutableListOf("aa", "bb")
    val b = mutableListOf("aa", "aa", "bb")

//    val add = listOf("a", "a", "b", "c")
//    val removed = listOf("a", "b", "d")
//
//    val addedCounts = add.groupBy { it }.mapValues { (_, values) -> values.size }.toMutableMap()
//    val removedCounts = removed.groupBy { it }.mapValues { (_, values) -> values.size }
//
//    removedCounts.forEach { (key, value) ->
//        addedCounts.merge(key, value) { left, right -> left - right }
//    }
//
//    addedCounts.filterValues { it > 0 }.keys

    val new_ = listOf("a", "a", "b", "c")
    val old_ = listOf("a", "b", "d")

    val adddded = new_.toMutableList().apply {
        old_.forEach { remove(it) }
    }

    val remoooved = old_.toMutableList().apply {
        new_.forEach { remove(it) }
    }

    println(adddded)
    println(remoooved)

    val a_m_b = mutableListOf<String>().apply { addAll(a) }
    println("diff  $a_m_b")
    a_m_b.removeAll(b)
    println("diff  $a_m_b")

    val b_m_a = mutableListOf<String>().apply { addAll(b) }
    println("diff $b_m_a")
    b_m_a.removeAll(a)
    println("diff $b_m_a")

    println("${tokensNew.size}, ${tokensOld.size}")

    println(tokensNew - tokensOld)
    println(listOf(tokensNew - tokensOld))
    println(tokensOld - tokensNew)

//    for (a in tokensNew) {
//        println(a)
//    }

    File(folder).walkTopDown().filter { it.path.equals(new) || it.path.equals(old) }.forEach { file ->
        println("Next file ${file.name}")
        val tokens = Java8Parser().tokenizer(file.inputStream()) // ?: return@forEach
    }
}

fun allBlobFilesDiff() {
    val separator = "THIS_STRING_WILL_NEVER_APPEAR_IN_DATASET_AND_IT_WILL_BE_USED_AS_SEPARATOR"
    val parentDirectory = "/Users/natalia.murycheva/Documents/gitCommitMessageCollectorStorage/"
    val gitDirectoryName = "aurora"
    val blobsDirectory = parentDirectory + File.separator + "${gitDirectoryName}_blobs"
    val fullLogFile = parentDirectory + File.separator + "gcm_${gitDirectoryName}_full_no_header.log"
    val outputFile = parentDirectory + File.separator + "${gitDirectoryName}_diff_blobs_identifiers.json"
    val separatorsToDelete = listOf("{", "}", "(", ")", "[", "]", ";", ".", ",", " ", "\n")
    val operatorsToDelete = listOf("+", "-", "*", "/", "%", "<", ">", "<=", ">=",
            "&&", "||", "!", "=", "+=", "-=", "*=", "/=", "++", "--", ">>", "<<", "^")
    val keyWordsToDelete = listOf("void", "throw", "super", "return", "package", "interface",
            "emplements", "float", "enum", "char", "break", "default", "try", "this", "structfp",
            "public", "new", "int", "if", "finally", "else", "continue", "catch", "boolean", "while",
            "transient", "synchronized", "static", "protected", "native", "instanceof", "goto", "final",
            "double", "const", "case", "assert", "volatile", "throws", "switch", "short", "private",
            "long", "import", "for", "extends", "do", "class", "byte", "abstact")



    /* only for aurora; get set with only needed for train commits */
    val neededCommitsFile = parentDirectory + File.separator + "${gitDirectoryName}_needed_commits.log"
    val neededCommit = mutableSetOf<String>()
    File(neededCommitsFile). forEachLine {
        neededCommit.add(it)
    }

    val allDiffs = mutableListOf<BlobsDiff>()
    var i = 0
    var j = 0
    var k = 0
    File(fullLogFile).forEachLine {
        i += 1
        if (i % 20 == 0) {
            println("Now at ${i}")
        }
        val list = it.split(separator)
        val commit = list[0]
        if (commit in neededCommit) { /* only for aurora */
            j += 1
            if (j % 20 == 0) {
                println("N0000W at ${j}")
            }
            val filePath = list[3]
            if (filePath.endsWith(".java")) {
                val oldBlob = list[4]
                val newBlob = list[5]
                val msg = list[6]

                val oldBlobFile = File(blobsDirectory + File.separator + oldBlob)
                val newBlobFile = File(blobsDirectory + File.separator + newBlob)

                try {
                    val tokensOldBlob = Java8Parser().tokenizerForBlob(oldBlobFile.inputStream())
                    val tokensNewBlob = Java8Parser().tokenizerForBlob(newBlobFile.inputStream())

                    val addedTokens = tokensNewBlob.toMutableList().apply {
                        tokensOldBlob.forEach { remove(it) }
                    }
                    addedTokens.removeAll(separatorsToDelete)
                    addedTokens.removeAll(operatorsToDelete)
                    addedTokens.removeAll(keyWordsToDelete)

                    val deletedTokens = tokensOldBlob.toMutableList().apply {
                        tokensNewBlob.forEach { remove(it) }
                    }
                    deletedTokens.removeAll(separatorsToDelete)
                    deletedTokens.removeAll(operatorsToDelete)
                    deletedTokens.removeAll(keyWordsToDelete)

                    allDiffs.add(
                            BlobsDiff(
                                    commit = commit,
                                    filePath = filePath,
                                    addedTokens = addedTokens,
                                    deletedTokens = deletedTokens,
                                    message = msg
                            )
                    )
                } catch (e: FileNotFoundException) {
                    println("Can't find $oldBlob or $newBlob")
                }
            } else if (filePath.endsWith(".xml")) {
                k += 1

                val oldBlob = list[4]
                val newBlob = list[5]
                val msg = list[6]

                val oldBlobFile = File(blobsDirectory + File.separator + oldBlob)
                val newBlobFile = File(blobsDirectory + File.separator + newBlob)

                try {
                    val tokensOldBlob = XmlParser().tokenizerForBlob(oldBlobFile.inputStream())
                    val tokensNewBlob = XmlParser().tokenizerForBlob(newBlobFile.inputStream())

                    val addedTokens = tokensNewBlob.toMutableList().apply {
                        tokensOldBlob.forEach { remove(it) }
                    }
                    addedTokens.removeAll(separatorsToDelete)
                    addedTokens.removeAll(operatorsToDelete)
                    addedTokens.removeAll(keyWordsToDelete)

                    val deletedTokens = tokensOldBlob.toMutableList().apply {
                        tokensNewBlob.forEach { remove(it) }
                    }
                    deletedTokens.removeAll(separatorsToDelete)
                    deletedTokens.removeAll(operatorsToDelete)
                    deletedTokens.removeAll(keyWordsToDelete)

                    allDiffs.add(
                            BlobsDiff(
                                    commit = commit,
                                    filePath = filePath,
                                    addedTokens = addedTokens,
                                    deletedTokens = deletedTokens,
                                    message = msg
                            )
                    )
                } catch (e: FileNotFoundException) {
                    println("Can't find $oldBlob or $newBlob")
                }
            }
        }
    }

    println("Number of xml files $k")

    val gson = GsonBuilder().setPrettyPrinting().create()
    FileWriter(outputFile).use {
        gson.toJson(allDiffs, it)
    }
}