package net.came20.spicytech.tile

interface IPoweredMachineAccess {
    fun getPower(): Int
    fun setPower(powerIn: Int)
}