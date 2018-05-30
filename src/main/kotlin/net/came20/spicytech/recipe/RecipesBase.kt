package net.came20.spicytech.recipe

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

abstract class RecipesBase {
    companion object {
        const val IGNORE_META = 32767
    }

    private val recipes = hashMapOf<ItemStack, ItemStack>()
    private val expReward = hashMapOf<ItemStack, Float>()

    protected fun add(input: ItemStack, output: ItemStack, exp: Float = 0.0f) {
        recipes.put(input, output)
        expReward.put(input, exp)
    }

    protected fun add(input: Item, output: ItemStack, exp: Float = 0.0f) {
        add(ItemStack(input, 1, IGNORE_META), output, exp)
    }

    protected fun add(input: Block, output: ItemStack, exp: Float = 0.0f) {
        add(Item.getItemFromBlock(input), output, exp)
    }

    private fun compare(input: ItemStack, recipe: ItemStack): Boolean {
        return input.item == recipe.item && (recipe.metadata == IGNORE_META || input.metadata == recipe.metadata)
    }

    /**
     * Gets a recipe's result by it's input stack.  Ignores stack count.
     * If there is no recipe for the given input, returns ItemStack.EMPTY
     */
    fun getRecipe(input: ItemStack): ItemStack {
        var stack = ItemStack.EMPTY
        recipes.forEach {
            recipeInput, recipeOutput ->
            if (compare(input, recipeInput)) {
                stack = recipeOutput
                return@forEach
            }
        }
        return stack
    }

    /**
     * Gets a recipe's experience reward by it's input stack.  Ignores stack count.
     * If there is no recipe for the given input, return 0.0f
     */
    fun getRecipeExp(input: ItemStack): Float {
        var exp = 0.0F
        expReward.forEach {
            recipeInput, expReward ->
            if (compare(input, recipeInput)) {
                exp = expReward
                return@forEach
            }
        }
        return exp
    }

    fun hasRecipe(input: ItemStack): Boolean {
        return !getRecipe(input).isEmpty
    }
}