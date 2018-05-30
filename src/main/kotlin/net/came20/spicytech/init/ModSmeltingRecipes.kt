package net.came20.spicytech.init

import net.came20.spicytech.item.IronBitsItem
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.GameRegistry

object ModSmeltingRecipes {
    fun init() {
        GameRegistry.addSmelting(IronBitsItem, ItemStack(Items.IRON_INGOT, 1), 0.1f)
    }
}