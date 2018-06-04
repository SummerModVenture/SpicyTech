package net.came20.spicytech.controller

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntityFurnace

public class GeneratorController(val powerPerTick: Int, val maxPower: Int): IController, IPowerSource {
    companion object {
        const val BURN_TIME_FIELD_ID = 0
        const val FUEL_TOTAL_BURN_TIME_FIELD_ID = 1
        const val POWER_FIELD_ID = 2

        fun isItemValidFuel(stack: ItemStack): Boolean {
            return TileEntityFurnace.isItemFuel(stack)
        }

        fun getItemBurnTime(stack: ItemStack): Int {
            return TileEntityFurnace.getItemBurnTime(stack)
        }
    }
    private var burnTime = 0
    private var fuelTotalBurnTime = 0
    private var power = 0

    override fun getNumFields(): Int {
        return 3
    }

    override fun getField(id: Int): Int {
        return when (id) {
            BURN_TIME_FIELD_ID -> burnTime
            FUEL_TOTAL_BURN_TIME_FIELD_ID -> fuelTotalBurnTime
            POWER_FIELD_ID -> power
            else -> 0
        }
    }

    override fun setField(id: Int, value: Int) {
        when (id) {
            BURN_TIME_FIELD_ID -> burnTime = value
            FUEL_TOTAL_BURN_TIME_FIELD_ID -> fuelTotalBurnTime = value
            POWER_FIELD_ID -> power = value
        }
    }

    override fun writeToNBT(compound: NBTTagCompound) {
        compound.setInteger("burn_time", burnTime)
        compound.setInteger("fuel_total_burn_time", fuelTotalBurnTime)
        compound.setInteger("power", power)
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        burnTime = compound.getInteger("burn_time")
        fuelTotalBurnTime = compound.getInteger("fuel_total_burn_time")
        power = compound.getInteger("power")
    }

    fun isRunning(): Boolean {
        return burnTime > 0
    }

    fun getBurnTimePercentage(): Double {
        return if (fuelTotalBurnTime != 0) {
            burnTime / fuelTotalBurnTime.toDouble()
        } else {
            0.0
        }
    }

    override fun getAvailablePower(): Int {
        return power
    }

    override fun getPowerPercentage(): Double {
        return power / maxPower.toDouble()
    }

    override fun drawPower(desired: Int): Int {
        var actualDraw = desired
        if (actualDraw > power) actualDraw = power
        power -= actualDraw
        return actualDraw
    }

    override fun addPower(powerIn: Int) {
        if (power + powerIn >= maxPower) {
            power = maxPower
        } else {
            power += powerIn
        }
    }

    override fun checkPower(powerIn: Int): Boolean {
        return power >= powerIn
    }

    fun update(fuelStack: ItemStack): Boolean {
        var changed = false
        if (!isRunning()) { //If we're not running, check if we can
            if (!fuelStack.isEmpty && isItemValidFuel(fuelStack)) { //If we can start running
                fuelTotalBurnTime = getItemBurnTime(fuelStack) //Get the total burn time
                burnTime = fuelTotalBurnTime //Set the current burn time to the total (loading new fuel)
                fuelStack.shrink(1) //Remove one fuel from the stack
                changed = true
            }
        }
        if (isRunning()) { //If we were running before or if we've started now
            burnTime-- //Decrement burn time
            addPower(powerPerTick) //Add the power per tick to our power
        }
        return changed
    }
}