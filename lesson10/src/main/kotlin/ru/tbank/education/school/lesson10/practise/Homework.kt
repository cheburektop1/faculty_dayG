package ru.tbank.education.school.lesson10.practise
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration

data class LogEntry(
    val time: LocalDateTime,
    val status: String,
    val id: String
)

data class PackageDone(
    val id: String,
    val time: Long
)

fun parseDate(time: String): LocalDateTime {
    val formatters = listOf(
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"), // A
        DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm"), // B
        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")  // C
    )

    for (f in formatters) {
        try {
            return LocalDateTime.parse(time, f)
        } catch (_: Exception) {

        }
    }

    throw IllegalArgumentException("Неизвестный формат даты: $time")
}


fun String.normalize(): LogEntry? {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    val normalized = this.lowercase().trim().replace("\\s+".toRegex(), " ")
    val patterns = listOf(
        // Format A
        Regex("""(?<time>\d{4}-\d{2}-\d{2} \d{2}:\d{2})\s*\|\s*ID:(?<id>\d+)\s*\|\s*STATUS:(?<status>\w+)""", RegexOption.IGNORE_CASE),

        // Format B
        Regex("""TS=(?<time>\d{2}/\d{2}/\d{4}-\d{2}:\d{2});\s*status=(?<status>\w+);\s*#(?<id>\d+)""", RegexOption.IGNORE_CASE),

        // Format C
        Regex("""\[(?<time>\d{2}\.\d{2}\.\d{4} \d{2}:\d{2})]\s*(?<status>\w+)\s*\(id:(?<id>\d+)""", RegexOption.IGNORE_CASE)
    )

    for (regex in patterns) {
        val match = regex.find(normalized)
        if (match != null) {
            val time = match.groups["time"]!!.value
            val status = match.groups["status"]!!.value
            val id = match.groups["id"]!!.value

            return LogEntry(time = parseDate(time), status = status, id = id)
        }
    }
    return null
}

/*fun main() {
    val logs = listOf<String>(
        "2026-01-22 09:14 | ID:042 | STATUS:sent",
        "TS=22/01/2026-09:27; status=delivered; #042",
        "2026-01-22 09:10 | ID:043 | STATUS:sent",
        "2026-01-22 09:18 | ID:043 | STATUS:delivered",
        "TS=22/01/2026-09:05; status=sent; #044",
        "[22.01.2026 09:40] delivered (id:044)",
        "2026-01-22 09:20 | ID:045 | STATUS:sent",
        "[22.01.2026 09:33] delivered (id:045)",
        "   ts=22/01/2026-09:50; STATUS=Sent; #046   ",
        " [22.01.2026 10:05]   DELIVERED   (ID:046) "
    )
    val normalized = mutableListOf<LogEntry>()
    for (i in logs.indices) {
        val norm = logs[i].normalize()
        if (norm != null) {
            normalized.add(LogEntry(time = norm.time, status = norm.status, id = norm.id))
        } else {
            println("Лог ${logs[i]} битый")
        }
    }

    val grouped: Map<String, List<LogEntry>> = normalized.groupBy { it.id }
    val all: MutableList<PackageDone> = mutableListOf()
    val bad: MutableList<PackageDone> = mutableListOf()
    for ((id, logEntries) in grouped) {
        if (logEntries.size == 1) {
            println("Для $id не хватает ещё одной записи")
            continue
        }

        val sorted = logEntries.sortedBy { it.time }

        val sendTime = sorted.first().time
        val deliverTime = sorted.last().time

        if (sendTime > deliverTime) {
            println("Для $id время приезда раньше времени отправки")
            continue
        }

        val diff = Duration.between(sendTime, deliverTime).toMinutes()
        if (diff > 20) {
            bad.add(PackageDone(id, diff))
        }
        all.add(PackageDone(id, diff))
    }

    all.sortByDescending { it.time }

    for (el in all) {
        println("ID: ${el.id}, время доставки: ${el.time}")
    }

    println("Самая долгая посылка: ${all[0].time}")

    for (el in bad) {
        println("Нарушитель: id: ${el.id}, время доставки: ${el.time}")
    }
}*/