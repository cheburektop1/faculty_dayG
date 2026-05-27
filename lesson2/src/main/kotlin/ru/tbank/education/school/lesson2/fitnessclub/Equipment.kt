package ru.tbank.education.school.lesson2.fitnessclub

import ru.tbank.education.school.lesson2.fitnessclub.ExerciseBike

abstract class Equipment(
    protected var damage: Int,
    protected val cost: Int,
    protected val burn: Int
) {

    open fun applyDamage(time: Int) {
        damage -= time
    }

    fun isBroken(): Boolean = damage <= 0

    abstract fun calculateCost(time: Int) : Int?
    abstract fun useTime(money: Int) : Int?
    abstract fun canUse(money: Int, time : Int) : Boolean
    abstract fun calculateCaloriesBurn(time:Int) : Int?
}

open class Machine(
    damage: Int,
    cost: Int,
    burn: Int
) : Equipment(damage, cost, burn) {
    override fun calculateCaloriesBurn(time:Int) : Int? =
        if (time > 0) time * burn else null
    override fun calculateCost(time: Int) : Int? =
        if (time > 0) time * cost else null
    override fun useTime(money: Int) : Int =
        if (money <= 0) 0 else money / cost
    override fun canUse(money: Int, time : Int) : Boolean =
        !isBroken() && money / cost >= time
}

class ExerciseBike(
    damage: Int,
    cost: Int,
    burn: Int
) : Machine(damage, cost, burn) {
    var maxSpeed: Int = 0
    constructor(
        damage: Int,
        cost: Int,
        burn: Int,
        maxSpeed: Int
    ) : this(damage, cost, burn) {
        this.maxSpeed = maxSpeed
    }
    fun calculateCaloriesBurn(time: Int, speed: Int): Int? {
        return time * speed * burn
    }
}

class ThreadMile(
    damage: Int,
    cost: Int,
    burn: Int
) : Machine(damage, cost, burn) {
    private var maxSpeed = 0
    private var extraMode: Boolean = false
    constructor(
        damage: Int,
        cost: Int,
        burn: Int,
        maxSpeed: Int,
        extraMode: Boolean
    ) : this(damage, cost, burn) {
        this.maxSpeed = maxSpeed
        this.extraMode = extraMode
    }
    fun enableClimbMode() {
        extraMode = true
    }
    fun disableClimbMode() {
        extraMode = false
    }
    override fun calculateCaloriesBurn(time: Int): Int? {
        if (time <= 0) return null
        return if (extraMode) time * burn * 2 else time * burn
    }
    override fun calculateCost(time: Int): Int? {
        if (time <= 0) return null
        return if (extraMode) time * cost * 2 else time * cost
    }
    override fun useTime(money: Int): Int {
        if (money <= 0) return 0
        return if (extraMode) money / (cost * 2) else money / cost
    }
    override fun applyDamage(time: Int) {
        damage -= if (extraMode) time * 2 else time
    }
}
