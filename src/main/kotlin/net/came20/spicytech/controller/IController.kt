package net.came20.spicytech.controller

import net.minecraft.nbt.NBTTagCompound

interface IController {
    fun readFromNBT(compound: NBTTagCompound)
    fun writeToNBT(compound: NBTTagCompound)

    fun getNumFields(): Int
    fun getField(id: Int): Int
    fun setField(id: Int, value: Int)
}