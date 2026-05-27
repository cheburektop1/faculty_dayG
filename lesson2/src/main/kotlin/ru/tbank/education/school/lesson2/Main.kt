package ru.tbank.education.school.lesson2
import ru.tbank.education.school.lesson2.fitnessclub.*

import java.math.BigDecimal

fun pickBestMachine(client: Client, machines: List<Equipment>): Equipment? {
    var best: Equipment? = null
    var minCost = Int.MAX_VALUE

    for (m in machines) {
        if (m.isBroken()) continue

        val time = client.goal / (m.calculateCaloriesBurn(1) ?: return null)
        if (time <= 0) continue

        val cost = m.calculateCost(time) ?: continue

        if (cost <= client.balance && cost < minCost) {
            minCost = cost
            best = m
        }
    }

    return best
}

fun useMachine(client: Client, machine: Equipment) {
    val time = client.goal / (machine.calculateCaloriesBurn(1) ?: 1)

    machine.applyDamage(time)

    println("${client.name} использовал ${machine::class.simpleName} $time минут.")
    println("Устройство теперь damage уменьшилось.")
}

fun main() {
    var Clients = mutableListOf<Client>(
        Client(170, "Petya", 15),
        Client(250, "Gregory", 53),
        Client(18, "Misha", 57),
        Client(10823, "Leonardo", 792),
        )
    val gymMachines = listOf(
        ExerciseBike(50, 5, 3, 40),
        ThreadMile(80, 7, 4, 50, false),
        Machine(100, 2, 1),
    )
    for (client in Clients) {
        val best = pickBestMachine(client, gymMachines)

        if (best == null) {
            println("${client.name} не может воспользоваться ни одним тренажёром")
            continue
        }

        println("${client.name} выбирает ${best::class.simpleName}")
        useMachine(client, best)
    }
}
