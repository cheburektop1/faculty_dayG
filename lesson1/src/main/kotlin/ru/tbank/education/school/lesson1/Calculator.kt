package ru.tbank.education.school.lesson1
import kotlin.math.*
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Функция преобразует математическое выражение в постфиксную запись (обратная польская нотация)
 * @sample express математическое выражение, которое надо преобразовать
 * @return Это же выражение в постфиксной записи
 */
fun expressionToPostfix(express:String): String {

    return ""
}


fun BigDecimal.floor(): BigDecimal {
    return this.setScale(0, RoundingMode.FLOOR)
}

fun sin(x: BigDecimal): BigDecimal {
    val y = (x / (2 * PI).toBigDecimal()).floor()
    val dx = x - ((2 * PI).toBigDecimal() * y);
    return sin(dx)
}

/**
 * Функция убирает последний элемент из стэка и возвращает его
 * @return удалённый элемент стэка
 */
fun MutableList<BigDecimal>.pop(): BigDecimal {
    val a = this[this.size - 1]
    this.pop()
    return a
}

fun MutableList<BigDecimal>.applyOp(block: (BigDecimal, BigDecimal) -> BigDecimal) {
    val a = this.pop()
    val b = this.pop()
    add(block(b, a))
}

fun MutableList<BigDecimal>.applyOp(block: (BigDecimal) -> BigDecimal) {
    val a = this.pop()
    add(block(a))
}

/**
 * Вычисляет выражение, записанное в постфиксной записи
 * @sample express математическое выражение, которое надо вычислить
 * @return Результат вычислений
 */
fun calculatePostfixExpression(express: String):String? {
    var itemStack: MutableList<BigDecimal> = mutableListOf()
    for (item in express.split(" ")) {
        when (item) {
            "+" -> itemStack.applyOp { x, y -> x + y }
            "-" -> itemStack.applyOp { x, y -> x - y }
            "*" -> itemStack.applyOp { x, y -> x * y }
            "/" -> itemStack.applyOp { x, y -> x / y }
            "%" -> itemStack.applyOp { x, y -> x % y }
            "sin" -> itemStack.applyOp { x -> sin(x) }
            else -> itemStack.add(item.toBigDecimalOrNull() ?: return null)
        }
    }

    return null
}

/**
 * Функция вычисления выражения, представленного строкой
 * @return результат вычисления строки или null, если вычисление невозможно
 * @sample "5 * 2".calculate()
 */
@Suppress("ReturnCount")
fun String.calculate(): Double? {

    return 0.0
}