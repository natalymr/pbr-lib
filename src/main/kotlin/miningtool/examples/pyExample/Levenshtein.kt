package miningtool.examples.pyExample


data class Prescription(var distance: Int, var route: String)

internal fun Levenshtein1(s1: String, s2: String): Prescription {
    val str1 = s1.split(" ").toMutableList()
    val str2 = s2.split(" ").toMutableList()
    while (str1.contains("")) {
        str1.remove("")
    }
    while (str2.contains("")) {
        str2.remove("")
    }
    val m = str1.size
    val n = str2.size
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
            if (str1[i - 1] != str2[j - 1]) {
                print("str1 = ${str1[i - 1]} str2 = ${str2[j - 1]}\n")
            }
            val cost = if (str1[i - 1] != str2[j - 1]) 1 else 0
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



