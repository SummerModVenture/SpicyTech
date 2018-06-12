package net.came20.spicytech.recipe

import com.spicymemes.core.recipe.RecipesBase
import net.minecraft.block.Block
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

object ComponentWorkbenchRecipes {
    private val recipes = ArrayList<Pair<List<ItemStack>, ItemStack>>()
    private fun add(output: ItemStack, vararg inputsIn: Any) {
        val inputs = arrayListOf<ItemStack>()
        inputsIn.forEach {
            when (it) {
                is ItemStack -> inputs.add(it)
                is Item -> inputs.add(ItemStack(it, 1, RecipesBase.IGNORE_META))
                is Block -> inputs.add(ItemStack(Item.getItemFromBlock(it), 1, RecipesBase.IGNORE_META))
            }
        }
        recipes.add(Pair(inputs, output))
    }


    fun init() {
        add(ItemStack(Items.IRON_INGOT, 2), Items.GOLD_INGOT)
        add(ItemStack(Items.DIAMOND_PICKAXE), ItemStack(Items.GOLD_INGOT, 2))
    }

    fun compare(input: ItemStack, recipe: ItemStack): Boolean {
        return input.item == recipe.item &&
                (recipe.metadata == RecipesBase.IGNORE_META || input.metadata == recipe.metadata)
    }

    fun stacksContainsStack(stacks: List<ItemStack>, stack: ItemStack): Boolean {
        stacks.forEach {
            if (compare(it, stack)) {
                return true
            }
        }
        return false
    }

    /**
     * Returns the number of the given recipe that can be created, or 0 if none can be created
     */
    private fun hasAllNecessary(inputsIn: Array<out ItemStack>, recipeIn: List<ItemStack>): Int {
        val inputs = inputsIn.filter { !it.isEmpty }
        recipeIn.forEach {
            if (!stacksContainsStack(inputs, it)) return 0 //Not all stacks match
        }
        return 1
    }

    /**
     * Gets all the possible recipes based on the given input stacks
     */
    fun getPossibleRecipes(vararg inputStacks: ItemStack): List<ItemStack> {
        if (inputStacks.all { it.isEmpty }) return listOf()
        val stacksOut = arrayListOf<ItemStack>()
        recipes.forEach {
            val inputs = it.first
            val output = it.second
            val possible = hasAllNecessary(inputStacks, inputs)
            if (possible > 0) {
                val stackOut = output.copy()
                val multiplier = stackOut.count
                stackOut.count = multiplier * possible
                stacksOut.add(stackOut)
            }
        }
        return stacksOut
    }

    /**
     * Gets the ingredients of a recipe by the output
     */
    fun reverseLookup(output: ItemStack): List<ItemStack> {
        recipes.forEach {
            val recipeInput = it.first
            val recipeOutput = it.second
            if (compare(output, recipeOutput)) {
                return recipeInput
            }
        }
        return listOf()
    }

    /**
     * Determines whether or not an item is a valid input for a recipe
     */
    fun isValidInput(stack: ItemStack): Boolean {
        recipes.forEach {
            val inputs = it.first
            inputs.forEach {
                if (compare(stack, it)) {
                    return true
                }
            }
        }
        return false
    }
}