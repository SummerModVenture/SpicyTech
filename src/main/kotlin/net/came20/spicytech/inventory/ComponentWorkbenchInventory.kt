package net.came20.spicytech.inventory

import assets.spicytech.clientOnly
import assets.spicytech.hasStackIgnoreDurability
import assets.spicytech.serverOnly
import net.came20.spicytech.container.SpicyTechContainer
import net.came20.spicytech.recipe.ComponentWorkbenchRecipes
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.asm.transformers.ItemStackTransformer
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class ComponentWorkbenchInventory: SpicyTechInventory(NUM_SLOTS) {
    companion object {
        const val ITEM_GRID_FIRST_INDEX = 0
        const val ITEM_GRID_NUM_SLOTS = 2 * 2
        const val OUTPUT_GRID_FIRST_INDEX = ITEM_GRID_FIRST_INDEX + ITEM_GRID_NUM_SLOTS
        const val OUTPUT_GRID_NUM_SLOTS = 5 * 6
        const val NUM_SLOTS = ITEM_GRID_NUM_SLOTS + OUTPUT_GRID_NUM_SLOTS
    }

    override fun getName(): String {
        return "container.spicytech:component_workbench.name"
    }

    fun updateRecipes() {
        val outputStacks = ComponentWorkbenchRecipes.getPossibleRecipes(*itemStacks.slice(ITEM_GRID_FIRST_INDEX until OUTPUT_GRID_FIRST_INDEX).toTypedArray())
        clearRange(OUTPUT_GRID_FIRST_INDEX, NUM_SLOTS)
        outputStacks.forEachIndexed { index, itemStack ->
            setInventorySlotContents(OUTPUT_GRID_FIRST_INDEX + index, itemStack)
        }
    }

    /**
     * For handling removing output items
     */
    override fun decrStackSize(index: Int, count: Int): ItemStack {
        if (index in OUTPUT_GRID_FIRST_INDEX until NUM_SLOTS) {
            val stack = itemStacks[index].copy() //Since the stack will be overwritten
            val inputStacks = itemStacks.slice(ITEM_GRID_FIRST_INDEX until OUTPUT_GRID_FIRST_INDEX)
            val recipeInputs = ComponentWorkbenchRecipes.reverseLookup(stack)
            recipeInputs.forEach {
                rInput ->
                val amountToTake = rInput.count
                inputStacks.first { ComponentWorkbenchRecipes.compare(it, rInput) }.shrink(amountToTake)
            }
            updateRecipes()
            return stack
        } else {
            return super.decrStackSize(index, count)
        }
    }

    /**
     * Used for the method below
     */
    interface ShrinkContext {
        val stack: ItemStack
        fun shrink()
    }

    /**
     * Handles the "shift-click" operation to remove all of the items for a given recipe,
     * and returns a shrink context which will be used by the container
     */
    fun handleRemoveAll(index: Int): ShrinkContext {
        val stack = itemStacks[index - SpicyTechContainer.MOD_FIRST_SLOT_INDEX].copy() //Get the output in the slot, copy because we will be making changes to it
        val inputStacks = itemStacks.slice(ITEM_GRID_FIRST_INDEX until OUTPUT_GRID_FIRST_INDEX).filter { !it.isEmpty } //Get the input stacks
        val recipeInputs = ComponentWorkbenchRecipes.reverseLookup(stack) //Get the recipe inputs
        var possible = Int.MAX_VALUE //Allows Math.min to work properly
        recipeInputs.forEach {
            rInput ->
            if (ComponentWorkbenchRecipes.stacksContainsStack(inputStacks, rInput)) {
                possible = Math.min(inputStacks.first { ComponentWorkbenchRecipes.compare(it, rInput)}.count / rInput.count, possible)
            }
        }
        if (possible == Int.MAX_VALUE) possible = 0 //In this case none were found, make it 0
        //The game will split a stack with a size greater than 64 for us
        //Now that we have the number of possible times we can repeat the recipe, we do some operations on the stack
        stack.count *= possible //Increase the output stack count to the number we will be creating
        return object : ShrinkContext { //create a shrink context which can be used by the container to decide whether or not to shrink
            override val stack = stack
            override fun shrink() {
                recipeInputs.forEach {
                    rInput ->
                    val amountToTake = possible * rInput.count
                    inputStacks.first { ComponentWorkbenchRecipes.compare(it, rInput) }.shrink(amountToTake) //Take the items
                }
            }
        }
    }

    override fun isItemValidForSlot(index: Int, stack: ItemStack): Boolean {
        return when (index) {
            in ITEM_GRID_FIRST_INDEX until OUTPUT_GRID_FIRST_INDEX -> ComponentWorkbenchRecipes.isValidInput(stack)
            else -> false
        }
    }
}