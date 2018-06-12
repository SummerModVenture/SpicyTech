package net.came20.spicytech.init

import net.came20.spicytech.block.TitaniumOreBlock
import net.came20.spicytech.item.GoldBitsItem
import net.came20.spicytech.item.IronBitsItem
import net.came20.spicytech.item.TitaniumBitsItem
import net.came20.spicytech.item.TitaniumIngotItem
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.GameRegistry

object ModSmeltingRecipes {
    fun init() {
        GameRegistry.addSmelting(IronBitsItem, ItemStack(Items.IRON_INGOT, 1), 0.1f)
        GameRegistry.addSmelting(GoldBitsItem, ItemStack(Items.GOLD_INGOT, 1), 0.1f)
        GameRegistry.addSmelting(TitaniumBitsItem, ItemStack(TitaniumIngotItem, 1), 0.1f)
        GameRegistry.addSmelting(TitaniumOreBlock, ItemStack(TitaniumIngotItem, 1), 0.1f)
    }
}