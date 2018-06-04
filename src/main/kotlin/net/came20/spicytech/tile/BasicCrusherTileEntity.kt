package net.came20.spicytech.tile

import net.came20.spicytech.controller.CrusherController
import net.came20.spicytech.controller.GeneratorController
import net.came20.spicytech.recipe.CrusherRecipes
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntityFurnace
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.MathHelper

class BasicCrusherTileEntity: SpicyTechMachineTileEntity(3) {
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
        return "container.basic_crusher.name"
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
            generator.update(fuelStack)
            crusher.update(generator, itemStacks)
        }
        /*
        val runningAtStart = isRunning()
        var changed = false

        if (isRunning()) {
            crusherRunTime--
        }

        if (!world.isRemote) {
            val fuelStack = itemStacks[FUEL_SLOT]
            val inputStack = itemStacks[INPUT_SLOT]
            //if the furnace is running or (the fuel stack is not empty and the input stack is not empty
            if (isRunning() || !fuelStack.isEmpty && !inputStack.isEmpty) {
                //If we aren't running and we can run
                if (!isRunning() && canRun()) {
                    crusherRunTime = getItemRunTime(fuelStack)
                    currentItemRunTime = crusherRunTime

                    //If we're running now (would be false if the fuel input was somehow invalid, should be prevented by isItemValidForSlot)
                    if (isRunning()) {
                        changed = true //We changed state

                        //If we have fuel (which should have been handled by the above check)
                        if (!fuelStack.isEmpty) {
                            val fuel = fuelStack.item
                            fuelStack.shrink(1) //Use 1 fuel

                            //If we used the last fuel
                            if (fuelStack.isEmpty) {
                                val fuelContainer = fuel.getContainerItem(fuelStack)
                                itemStacks[FUEL_SLOT] = fuelContainer //Set the fuel stack to the container, which handles the lava bucket case and gives them their bucket back
                            }
                        }
                    }
                }

                //If we are running and we can run
                if (isRunning() && canRun()) {
                    progress++ //Tick up the crush timer
                    if (progress == totalProgress) {
                        progress = 0 //Reset the progress
                        totalProgress = getCrushTime(inputStack) //Get the next total progress
                        crushItem()
                        changed = true
                    }
                } else {
                    progress = 0 //Reset our progress (out of fuel, removed input, etc.)
                }
            } else if (!isRunning() && progress > 0) { //We can't run and we have some progress
                //Start ticking down the progress at twice the rate
                if (progress > 0) {
                    progress = MathHelper.clamp(progress - 2, 0, totalProgress)
                }
            }

            //If we weren't running and now we are and vice versa
            if (runningAtStart != isRunning()) {
                changed = true
                //TODO do something with blockstate here, to change texture perhaps
            }
        }

        if (changed) markDirty() //Tell mc we changed
        */
    }
}