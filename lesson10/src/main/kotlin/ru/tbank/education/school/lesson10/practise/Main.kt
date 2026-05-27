package ru.tbank.education.school.lesson10.practise
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter

fun main() {
    task1()
    println()
    task2()
    println()
    task3()
    println()
    task4()
    println()
    task5()
    println()
    task7()
}

/*
1) Строки + регулярные выражения
["Name: Ivan, score=17", ...]
Извлечь имя и score, собрать пары, вывести победителя.
*/
fun task1() {
    val lines = listOf(
        "Name: Ivan, score=17",
        "Name: Olga, score=23",
        "Name: Max, score=5"
    )

    val re = Regex("""^Name:\s*([A-Za-z]+)\s*,\s*score=(\d+)\s*$""")

    val pairs: List<Pair<String, Int>> = lines.mapNotNull { s ->
        val m = re.find(s) ?: return@mapNotNull null
        val name = m.groupValues[1]
        val score = m.groupValues[2].toInt()
        name to score
    }

    println("Task 1 pairs: $pairs")

    val best = pairs.maxByOrNull { it.second }
    if (best != null) {
        println("Task 1 best: ${best.first} (${best.second})")
    } else {
        println("Task 1: no valid lines")
    }
}

/*
2) Даты + коллекции
["2026-01-22", ...]
Преобразовать в даты, отсортировать, посчитать сколько в январе 2026.
*/
fun task2() {
    val dateStrings = listOf(
        "2026-01-22",
        "2026-02-01",
        "2025-12-31",
        "2026-01-05"
    )

    val fmt = DateTimeFormatter.ISO_LOCAL_DATE

    val dates = dateStrings.map { LocalDate.parse(it, fmt) }.sorted()

    println("Task 2 sorted dates: ${dates.joinToString { it.format(fmt) }}")

    val countJan2026 = dates.count { it.year == 2026 && it.month == Month.JANUARY }
    println("Task 2 count in Jan 2026: $countJan2026")
}

/*
3) Коллекции + строки
"apple orange apple banana orange apple"
Частоты слов, вывести слова с частотой > 1 по алфавиту.
*/
fun task3() {
    val text = "apple orange apple banana orange apple"

    val words = text.trim().split(Regex("""\s+""")).filter { it.isNotEmpty() }

    val freq = mutableMapOf<String, Int>()
    for (w in words) {
        freq[w] = (freq[w] ?: 0) + 1
    }

    println("Task 3 freq: $freq")

    val repeated = freq
        .filter { (_, c) -> c > 1 }
        .keys
        .sorted()

    println("Task 3 repeated words: ${repeated.joinToString(", ")}")
}

fun task4() {
    val words = listOf<String>("A-123", "B-7", "AA-12", "C-001", "D-99x")
    var newwords = mutableListOf<String>()
    for (w in words) {
        if (w.length > 5) continue
        var check3 = true
        val s = w.length - 1
        for (i in 2..s) {
            if (w[i] !in '0'..'9') {
                check3 = false
                break
            }
        }
        if (w[0] in 'A'..'Z' && w[1] == '-' && check3) {
            newwords.add(w)
        }
    }
    for (w in newwords) {
        println("Task 4: $w")
    }
}

fun task5() {
    val s = listOf("  Hello   world  ", "A   B    C", "   one")
    for (word in s) {
        val splitted = word.split(" ")
        print("Task 5: ")
        for (i in 0..<splitted.size-1) {
            if (splitted[i].isEmpty()) continue
            print(splitted[i] + " ")
        }
        println(splitted[splitted.size - 1])
    }
}

fun task7() {
    val s = listOf("math:Ivan", "bio:Olga", "math:Max", "bio:Ivan", "cs:Olga")
    val result = mutableMapOf<String, MutableList<String>>()
    for (w in s) {
        val splitted = w.split(":")
        if (result.get(splitted[0]) == null) {
            result[splitted[0]] = mutableListOf(splitted[1])
        } else {
            result[splitted[0]]?.add(splitted[1])
        }
    }
    for (k in result.keys) {
        print("Task 7: $k -> ")
        for (v in result[k]!!) {
            print("$v ")
        }
        println()
    }
}

fun task6() {
    val s = listOf<Pair<String, String>>(Pair("2026-01-01", "2026-01-10"), Pair("2025-12-31","2026-01-01"), Pair("2026-02-01","2026-01-22"))

}