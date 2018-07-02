package net.came20.spicytech.recipe

import com.spicymemes.core.recipe.RecipesBase
import net.came20.spicytech.item.CrusherComponentItem
import net.came20.spicytech.item.SawmillComponentItem
import net.minecraft.block.Block
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

object ComponentWorkbenchRecipes {
    private val recipes = ArrayList<Pair<List<ItemStack>, ItemStack>>()
    private fun add(outputIn: Any, vararg inputsIn: Any) {
        val output: ItemStack
        val inputs = arrayListOf<ItemStack>()

        when (outputIn) {
            is ItemStack -> output = outputIn
            is Item -> output = ItemStack(outputIn, 1)
            is Block -> output = ItemStack(Item.getItemFromBlock(outputIn), 1)
            else -> throw RuntimeException("Invalid ouptut type ${outputIn.javaClass.name}")
        }

        inputsIn.forEach {
            when (it) {
                is ItemStack -> inputs.add(it)
                is Item -> inputs.add(ItemStack(it, 1, RecipesBase.IGNORE_META))
                is Block -> inputs.add(ItemStack(Item.getItemFromBlock(it), 1, RecipesBase.IGNORE_META))
                else -> throw RuntimeException("Invalid input type ${it.javaClass.name}")
            }
        }
        recipes.add(Pair(inputs, output))
    }


    fun init() {
        //add(ouptut, inputs...)
        add(CrusherComponentItem, ItemStack(Items.IRON_INGOT, 2, RecipesBase.IGNORE_META))
        add(SawmillComponentItem, ItemStack(Items.IRON_INGOT, 2, RecipesBase.IGNORE_META))
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
            rInput ->
            if (stacksContainsStack(inputs, rInput)) { //If the recipe contains the input stack
                if (inputs.first { compare(it, rInput) }.count < rInput.count) {
                    return 0
                }
            }
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