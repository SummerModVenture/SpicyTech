package net.came20.spicytech.controller

import net.came20.spicytech.recipe.CrusherRecipes
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.MathHelper

class CrusherController(val powerDraw: Int): IController {
    companion object {
        const val CRUSH_TIME_FIELD_ID = 0
        const val ITEM_TOTAL_CRUSH_TIME_FIELD_ID = 1

        const val INPUT_SLOT = 0
        const val OUTPUT_SLOT = 2
    }
    private var crushTime = 0
    private var itemTotalCrushTime = 0

    fun getItemCrushTime(stack: ItemStack): Int {
        return 200
    }

    override fun getNumFields(): Int {
        return 2
    }

    override fun getField(id: Int): Int {
        return when (id) {
            CRUSH_TIME_FIELD_ID -> crushTime
            ITEM_TOTAL_CRUSH_TIME_FIELD_ID -> itemTotalCrushTime
            else -> 0
        }
    }

    override fun setField(id: Int, value: Int) {
        when (id) {
            CRUSH_TIME_FIELD_ID -> crushTime = value
            ITEM_TOTAL_CRUSH_TIME_FIELD_ID -> itemTotalCrushTime = value
        }
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        crushTime = compound.getInteger("crush_time")
        itemTotalCrushTime = compound.getInteger("item_total_crush_time")
    }

    override fun writeToNBT(compound: NBTTagCompound) {
        compound.setInteger("crush_time", crushTime)
        compound.setInteger("item_total_crush_time", itemTotalCrushTime)
    }

    fun getProgressPercentage(): Double {
        return if (itemTotalCrushTime != 0) {
            crushTime / itemTotalCrushTime.toDouble()
        } else {
            0.0
        }
    }

    private fun canRun(itemStacks: Array<ItemStack>): Boolean {
        val inputStack = itemStacks[INPUT_SLOT]
        val outputStack = itemStacks[OUTPUT_SLOT]
        if (inputStack.isEmpty) return false //we can't run if there is no input
        val resultStack = CrusherRecipes.getRecipe(inputStack) //Get the recipe from the recipes registry
        if (resultStack.isEmpty) return false //The recipe doesn't exist, so we can't run
        if (outputStack.isEmpty) return true //At this point if the output stack is empty we can run
        if (!outputStack.isItemEqual(resultStack)) return false //The result of this smelt doesn't match the items in the output
        if (outputStack.count + resultStack.count <= Math.min(64, outputStack.maxStackSize)) return true //The items are the same and will fit in the stack
        return false //The items will not fit
    }

    private fun crushItem(itemStacks: Array<ItemStack>) {
        val inputStack = itemStacks[INPUT_SLOT]
        val outputStack = itemStacks[OUTPUT_SLOT]
        val resultStack = CrusherRecipes.getRecipe(inputStack)
        if (outputStack.isEmpty) {
            itemStacks[OUTPUT_SLOT] = resultStack.copy()
        } else if (outputStack.isItemEqual(resultStack)) {
            outputStack.grow(resultStack.count)
        }
        inputStack.shrink(1)
    }

    fun update(powerSource: IPowerSource, itemStacks: Array<ItemStack>): Boolean {
        var changed = false
        if (powerSource.checkPower(powerDraw)) { //If the power source has enough power to run
            if (canRun(itemStacks)) {
                powerSource.drawPower(powerDraw) //Draw the power
                itemTotalCrushTime = getItemCrushTime(itemStacks[INPUT_SLOT]) //Get the current crush time
                crushTime++ //Increment our progress
                if (crushTime >= itemTotalCrushTime) { //We are done processing this item
                    crushTime = 0 //Reset the timer
                    crushItem(itemStacks)
                    changed = true
                }
            } else {
                crushTime = 0
            }
        } else { //The power source doesn't have enough, start decreasing the crush progress by 2x speed
            if (crushTime > 0) { //If there is any progress
                crushTime = MathHelper.clamp(crushTime - 2, 0, itemTotalCrushTime) //Decrease at 2x
            }
        }
        return changed
    }
}