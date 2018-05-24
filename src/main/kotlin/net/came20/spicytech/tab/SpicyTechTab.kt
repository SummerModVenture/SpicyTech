package net.came20.spicytech.tab

import net.came20.spicytech.block.TestStorageBlock
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack

object SpicyTechTab: CreativeTabs("spicytech_tab") {
    override fun getTabIconItem(): ItemStack {
        return ItemStack(TestStorageBlock, 1, 0)
    }
}