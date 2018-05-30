package net.came20.spicytech.recipe

import net.came20.spicytech.item.IronBitsItem
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack

object CrusherRecipes: RecipesBase() {
    init {
        add(Blocks.IRON_ORE, ItemStack(IronBitsItem, 2))
    }
}