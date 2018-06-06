package net.came20.spicytech.recipe

import com.spicymemes.core.recipe.RecipesBase
import net.came20.spicytech.item.GoldBitsItem
import net.came20.spicytech.item.IronBitsItem
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack

object CrusherRecipes: RecipesBase() {
    init {
        add(Blocks.IRON_ORE, ItemStack(IronBitsItem, 2), 0f)
        add(Blocks.GOLD_ORE, ItemStack(GoldBitsItem, 2), 0f)
        add(Blocks.COBBLESTONE, ItemStack(Blocks.GRAVEL, 1), 0f)
    }
}