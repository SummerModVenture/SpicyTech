package net.came20.spicytech.tile


import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing

class TestStorageTileEntity: SpicyTechMachineTileEntity(9) {
    companion object {
        const val FUEL_SLOT = 0
        const val INPUT_SLOT = 1
        const val OUTPUT_SLOT = 2

        val SLOTS_DOWN = intArrayOf(OUTPUT_SLOT)
        val SLOTS_SIDES = intArrayOf(FUEL_SLOT)
        val SLOTS_TOP = intArrayOf(INPUT_SLOT)
    }

    override fun getName(): String {
        return "container.test_storage.name"
    }

    override fun getSlotsForFace(side: EnumFacing): IntArray {
        return when (side) {
            EnumFacing.DOWN -> SLOTS_DOWN
            EnumFacing.UP -> SLOTS_TOP
            else -> SLOTS_SIDES
        }
    }

    override fun isItemValidForSlot(index: Int, stack: ItemStack): Boolean {
        if (stack.item == Items.APPLE) return false
        return true
    }
}