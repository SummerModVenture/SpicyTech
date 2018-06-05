package net.came20.spicytech.controller

interface IPowerSource {
    fun getAvailablePower(): Int
    fun getPowerPercentage(): Double

    fun drawPower(desired: Int): Int
    fun addPower(powerIn: Int)

    fun checkPower(powerIn: Int): Boolean
    fun isPowerFull(): Boolean
}