package net.came20.spicytech.tab

import net.came20.spicytech.block.BasicCrusherBlock
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack

object SpicyTechTab: CreativeTabs("spicytech_tab") {
    override fun getTabIconItem(): ItemStack {
        return ItemStack(BasicCrusherBlock, 1, 0)
    }
}