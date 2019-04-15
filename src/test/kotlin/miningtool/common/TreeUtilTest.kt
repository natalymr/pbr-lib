package miningtool.common

import miningtool.examples.preOrderedTraverseAST
import miningtool.examples.pyExample.Levenshtein1
import miningtool.examples.pyExample.Prescription
import miningtool.examples.sbt
import miningtool.paths.PathWorker
import org.junit.Assert
import org.junit.Test

class DummyNode(val data: String, val childrenList: List<DummyNode>) : Node {
    override fun setMetadata(key: String, value: Any) {

    }

    override fun getMetadata(key: String): Any? {
        return null
    }

    override fun isLeaf(): Boolean {
        return childrenList.isEmpty()
    }

    override fun getTypeLabel(): String {
        return data
    }

    override fun getChildren(): List<Node> {
        return childrenList
    }

    override fun getParent(): Node? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getToken(): String {
        return data
    }
}

fun createDummyTree(): DummyNode {
    val node4 = DummyNode("4", emptyList())
    val node5 = DummyNode("5", emptyList())
    val node6 = DummyNode("6", emptyList())
    val node7 = DummyNode("7", emptyList())
    val node8 = DummyNode("8", emptyList())

    val node2 = DummyNode("2", listOf(node4, node5, node6))
    val node3 = DummyNode("3", listOf(node7, node8))

    return DummyNode("1", listOf(node2, node3))
}

fun createDummyTreeForSbt(): DummyNode {
    val node4 = DummyNode("4", emptyList())
    val node5 = DummyNode("5", emptyList())
    val node6 = DummyNode("6", emptyList())

    val node2 = DummyNode("2", listOf(node4, node5, node6))
    val node3 = DummyNode("3", emptyList())

    return DummyNode("1", listOf(node2, node3))
}

fun createDummyTreeForSbtAnother(): DummyNode {
    val node4 = DummyNode("4", emptyList())
    val node5 = DummyNode("5", emptyList())
    val node6 = DummyNode("6", listOf(node4))
    val node7 = DummyNode("7", emptyList())
    val node8 = DummyNode("8", emptyList())

    val node2 = DummyNode("2", listOf(node5, node6))
    val node3 = DummyNode("3", listOf(node7, node8))

    return DummyNode("1", listOf(node2, node3))
}

class TreeUtilTest {
    @Test
    fun testPostOrder() {
        val root = createDummyTree()
        val dataList = root.postOrderIterator().asSequence().map { it.getTypeLabel() }

        Assert.assertArrayEquals(arrayOf("4", "5", "6", "2", "7", "8", "3", "1"), dataList.toList().toTypedArray())
    }

    @Test
    fun testPreOrder() {
        val root = createDummyTree()
        val dataList = root.preOrderIterator().asSequence().map { it.getTypeLabel() }

        Assert.assertArrayEquals(arrayOf("1", "2", "4", "5", "6", "3", "7", "8"), dataList.toList().toTypedArray())
    }

    @Test
    fun testSbt() {
        val root = createDummyTreeForSbt()
        Assert.assertEquals("", "_OPEN_ 1 " +
                "_OPEN_ 2 _OPEN_ 4 _CLOSE_ 4 _OPEN_ 5 _CLOSE_ 5 _OPEN_ 6 _CLOSE_ 6  _CLOSE_ 2" +
                " _OPEN_ 3 _CLOSE_ 3  _CLOSE_ 1", sbt(root))
    }

    @Test
    fun testSbtWithDummy() {
        val root = createDummyTree()
        Assert.assertEquals("", "_OPEN_ 1 " +
                "_OPEN_ 2 _OPEN_ 4 _CLOSE_ 4 _OPEN_ 5 _CLOSE_ 5 _OPEN_ 6 _CLOSE_ 6  _CLOSE_ 2" +
                " _OPEN_ 3 _OPEN_ 7 _CLOSE_ 7 _OPEN_ 8 _CLOSE_ 8  _CLOSE_ 3" +
                "  _CLOSE_ 1", sbt(root))
    }

    @Test
    fun testSbtWithAnotherDummy() {
        val root = createDummyTreeForSbtAnother()
        Assert.assertEquals("", "_OPEN_ 1 " +
                "_OPEN_ 2 _OPEN_ 5 _CLOSE_ 5 " +
                "_OPEN_ 6 _OPEN_ 4 _CLOSE_ 4  _CLOSE_ 6  _CLOSE_ 2" +
                " _OPEN_ 3 _OPEN_ 7 _CLOSE_ 7 _OPEN_ 8 _CLOSE_ 8  _CLOSE_ 3" +
                "  _CLOSE_ 1", sbt(root))
    }

    @Test
    fun testLevenshteinDist() {
        val ast1 = sbt(createDummyTreeForSbt())
        val ast2 = sbt(createDummyTree())

        print(Levenshtein1(ast1, ast2).route)
    }

    /**
     * SBT with  dummy NODES
     */
    private fun sbt_nodes_for_test(node: DummyNode) : MutableList<DummyNode> {
        val open = DummyNode("OPEN", emptyList())
        val close = DummyNode("CLOSE", emptyList())

        val result : MutableList<DummyNode> = ArrayList()

        if (node.isLeaf()) {
            result.add(open)
            result.add(node)
            result.add(close)
            result.add(node)
        }
        else {
            result.add(open)
            result.add(node)

            for (child in node.getChildren()) {
                result.addAll(sbt_nodes_for_test(child as DummyNode))
            }

            result.add(close)
            result.add(node)
        }

        return result
    }

    @Test
    fun testSbtNode() {
        val node4 = DummyNode("4", emptyList())
        val node5 = DummyNode("5", emptyList())
        val node6 = DummyNode("6", emptyList())

        val node2 = DummyNode("2", listOf(node4, node5, node6))
        val node3 = DummyNode("3", emptyList())

        val node1 = DummyNode("1", listOf(node2, node3))

        val open = DummyNode("OPEN", emptyList())
        val close = DummyNode("CLOSE", emptyList())

        val expected = listOf(open, node1,
                open, node2,
                open, node4, close, node4, open, node5, close, node5, open, node6, close, node6,
                close, node2,
                open, node3, close, node3,
                close, node1)

        val actual = sbt_nodes_for_test(node1)

        Assert.assertEquals("", expected.size, actual.size)
    }

    @Test
    fun testSbtNodeDummy() {
        val node4 = DummyNode("4", emptyList())
        val node5 = DummyNode("5", emptyList())
        val node6 = DummyNode("6", emptyList())
        val node7 = DummyNode("7", emptyList())
        val node8 = DummyNode("8", emptyList())

        val node2 = DummyNode("2", listOf(node4, node5, node6))
        val node3 = DummyNode("3", listOf(node7, node8))

        val node1 = DummyNode("1", listOf(node2, node3))

        val open = DummyNode("OPEN", emptyList())
        val close = DummyNode("CLOSE", emptyList())

        val expected = listOf(open, node1,
                open, node2,
                open, node4, close, node4, open, node5, close, node5, open, node6, close, node6,
                close, node2,
                open, node3,
                open, node7, close, node7, open, node8, close, node8,
                close, node3,
                close, node1)

        val actual = sbt_nodes_for_test(node1)

        Assert.assertEquals("", expected.size, actual.size)

    }

    /**
     * Levenshtein for dummy NODES
     */
    private fun Levenshtein_for_nodes(s1: List<DummyNode>, s2: List<DummyNode>): Prescription {

        println("s1")
        for (each in s1) {
            print(each.data)
        }
        println()
        println("s2")
        for (each in s2) {
            print(each.data)
        }
        println()

        val m = s1.size
        val n = s2.size

        val D = Array(m + 1) { IntArray(n + 1) }
        val P = Array(m + 1) { CharArray(n + 1) }

        // Базовые значения
        for (i in 0..m) {
            D[i][0] = i
            P[i][0] = 'D'
        }
        for (i in 0..n) {
            D[0][i] = i
            P[0][i] = 'I'
        }


        for (i in 1..m)
            for (j in 1..n) {
//                if (s1[i - 1].data != s2[j - 1].data) {
//                    print("str1 = ${s1[i - 1].data} str2 = ${s2[j - 1].data}\n")
//                }
//                var cost: Int
//                if (s1[i - 1].data != s2[j - 1].data &&
//                        (s1[i - 1].data != "OPEN" || s1[i - 1].data != "CLOSE") &&
//                        (s2[j - 1].data != "OPEN" || s2[j - 1].data != "CLOSE")) {
//                    cost = 1
//                } else {
//                    cost = 0
//                }
                val cost = if (s1[i - 1].data != s2[j - 1].data) 1 else 0
                if (D[i][j - 1] < D[i - 1][j] && D[i][j - 1] < D[i - 1][j - 1] + cost) {
                    //Вставка
                    D[i][j] = D[i][j - 1] + 1
                    P[i][j] = 'I'
                } else if (D[i - 1][j] < D[i - 1][j - 1] + cost) {
                    //Удаление
                    D[i][j] = D[i - 1][j] + 1
                    P[i][j] = 'D'
                } else {
                    //Замена или отсутствие операции
                    D[i][j] = D[i - 1][j - 1] + cost
                    P[i][j] = if (cost == 1) 'R' else 'M'
                }
            }

        //Восстановление предписания
        val route = StringBuilder("")
        var i = m
        var j = n
        do {
            val c = P[i][j]
            route.append(c)
            if (c == 'R' || c == 'M') {
                i--
                j--
            } else if (c == 'D') {
                i--
            } else {
                j--
            }
        } while (i != 0 || j != 0)
        return Prescription(D[m][n], route.reverse().toString())
    }


    @Test
    fun LevenshteinForNodes() {
        val root1 = createDummyTree()
        val sbt1: List<DummyNode> = sbt_nodes_for_test(root1)

        val root2 = createDummyTreeForSbt()
        val sbt2: List<DummyNode> = sbt_nodes_for_test(root2)

        println(Levenshtein_for_nodes(sbt2, sbt1).route)
    }

    /*
    * PreOrder AST
    *
    * */
    @Test
    fun preOrderedTraverseASTTest() {
        val root = createDummyTreeForSbtAnother()
        print(preOrderedTraverseAST(root))
    }

    @Test
    fun preOrderedTraverseASTTest2() {
        val root = createDummyTreeForSbt()
        print(preOrderedTraverseAST(root))
    }

    @Test
    fun pathCountDummyTree() {
        val tree = createDummyTreeForSbt()
        val nLeaves = tree.postOrder().count { it.isLeaf() }

        val allPaths = PathWorker().retrievePaths(tree)
        val expectedCount = (nLeaves * (nLeaves - 1)) / 2

        Assert.assertEquals("A tree with $nLeaves leaves contains $expectedCount paths, " +
                "one per distinct ordered pair of leaves. Worker returned ${allPaths.size}",
                expectedCount, allPaths.size)

    }
}
