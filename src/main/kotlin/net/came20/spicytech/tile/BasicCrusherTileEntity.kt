package net.came20.spicytech.tile

import net.came20.spicytech.block.BasicCrusherBlock
import net.came20.spicytech.block.SpicyTechBlockBasicMachine
import net.came20.spicytech.block.SpicyTechBlockDirectional
import net.came20.spicytech.block.SpicyTechBlockMachine
import net.came20.spicytech.controller.CrusherController
import net.came20.spicytech.controller.GeneratorController
import net.came20.spicytech.recipe.CrusherRecipes
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntityFurnace
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.MathHelper

class BasicCrusherTileEntity: SpicyTechMachineTileEntity(3), IBasicMachineRunningAccess {
    companion object {
        const val INPUT_SLOT = 0
        const val FUEL_SLOT = 1
        const val OUTPUT_SLOT = 2

        val SLOTS_DOWN = intArrayOf(OUTPUT_SLOT)
        val SLOTS_SIDES = intArrayOf(FUEL_SLOT)
        val SLOTS_TOP = intArrayOf(INPUT_SLOT)
    }

    val generator = GeneratorController(1, Short.MAX_VALUE.toInt())
    val crusher = CrusherController(1)

    init {
        fieldManager.register(generator)
        fieldManager.register(crusher)
    }

    override fun getName(): String {
        return "container.spicytech:basic_crusher.name"
    }

    override fun getSlotsForFace(side: EnumFacing): IntArray {
        return when (side) {
            EnumFacing.DOWN -> SLOTS_DOWN
            EnumFacing.UP -> SLOTS_TOP
            else -> SLOTS_SIDES
        }
    }

    private fun isItemValidFuel(stack: ItemStack): Boolean {
        return GeneratorController.isItemValidFuel(stack)
    }

    /**
     * Note that while in the Minecraft API this only handles the inputs from automation (hopper), our SlotSet API
     * returns Slot objects that perform the checks we define here.  Therefore, nothing else needs to be done.
     * The slots will honor the conditions we set here
     */
    override fun isItemValidForSlot(index: Int, stack: ItemStack): Boolean {
        return when (index) {
            FUEL_SLOT -> isItemValidFuel(stack) //If the item is valid fuel, allow the user to add it
            INPUT_SLOT -> CrusherRecipes.hasRecipe(stack) //If the item is a valid input, allow the user to add it
            OUTPUT_SLOT -> false //Do not allow user or hopper to insert items into the output slot
            else -> false //Default to not allowed
        }
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        generator.readFromNBT(compound)
        crusher.readFromNBT(compound)
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(compound)
        generator.writeToNBT(compound)
        crusher.writeToNBT(compound)
        return compound
    }

    override fun update() {
        if (!world.isRemote) {
            val fuelStack = itemStacks[FUEL_SLOT]
            var shouldMark = false
            if (generator.update(fuelStack)) {
                shouldMark = true
            }
            if (crusher.update(generator, itemStacks)) {
                shouldMark = true
            }
            if (shouldMark) {
                val state = world.getBlockState(pos)
                world.setBlockState(pos, BasicCrusherBlock.defaultState
                        .withProperty(SpicyTechBlockDirectional.FACING, state.getValue(SpicyTechBlockDirectional.FACING))
                        .withProperty(SpicyTechBlockMachine.ACTIVE, crusher.isRunning())
                        .withProperty(SpicyTechBlockBasicMachine.BURNING, generator.isRunning())
                )
                markDirty()
            }
        }
    }

    override fun isRunning(): Boolean {
        return crusher.isRunning()
    }

    override fun isBurning(): Boolean {
        return generator.isRunning()
    }
}