package net.came20.spicytech.slotset

import net.came20.spicytech.SpicyTech
import net.came20.spicytech.container.DefaultValidityChecker
import net.came20.spicytech.container.IValidityChecker
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack

abstract class SlotSetBase(val name: String,
                       val inventory: IInventory,
                       val numRows: Int,
                       val numCols: Int,
                       val startXPos: Int,
                       val startYPos: Int,
                       val xSpacing: Int = 18,
                       val ySpacing: Int = 18,
                       val validityChecker: IValidityChecker = DefaultValidityChecker) {
    val numSlots = numRows * numCols

    fun buildSlots(firstSlot: Int): Array<Slot> {
        val slots = arrayOfNulls<Slot>(numSlots)
        var i = 0
        var slotNumber: Int
        for (y in 0 until numRows) {
            for (x in 0 until numCols) {
                slotNumber = i + firstSlot
                val xPos = startXPos + x * xSpacing
                val yPos = startYPos + y * ySpacing
                SpicyTech.logger.info("Creating slot $slotNumber, index $i for SlotSetBase $name at ($xPos, $yPos)")
                slots[i] = object : Slot(inventory, slotNumber, xPos, yPos) {
                    override fun isItemValid(stack: ItemStack): Boolean {
                        return validityChecker.checkValidity(stack)
                    }
                }
                i++
            }
        }
        return slots as Array<Slot>
    }
}