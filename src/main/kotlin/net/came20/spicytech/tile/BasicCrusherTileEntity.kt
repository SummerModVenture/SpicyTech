package net.came20.spicytech.tile

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

        const val FIELD_RUN_TIME = 0
        const val FIELD_CURRENT_ITEM_RUN_TIME = 1
        const val FIELD_PROGRESS = 2
        const val FIELD_TOTAL_PROGRESS = 3
        const val NUM_FIELDS = 4
    }

    private var crusherRunTime = 0 //Number of ticks the crusher will run for
    private var currentItemRunTime = 0 //The number of ticks the current item will keep the crusher running for
    private var progress = 0
    private var totalProgress = 0

    val generator = GeneratorController(1, Short.MAX_VALUE.toInt())

    init {
        fieldManager.register(generator)
    }

    override fun getName(): String {
        return "container.basic_crusher.name"
    }

    /**
     * Returns the number of ticks it will take to crush an item.
     * Currently returns 200 (furnace), but can be configured in
     * the future to return custom values based on the input item
     */
    private fun getCrushTime(stack: ItemStack): Int {
        return 200
    }

    override fun getSlotsForFace(side: EnumFacing): IntArray {
        return when (side) {
            EnumFacing.DOWN -> SLOTS_DOWN
            EnumFacing.UP -> SLOTS_TOP
            else -> SLOTS_SIDES
        }
    }

    private fun isItemValidFuel(stack: ItemStack): Boolean {
        return TileEntityFurnace.isItemFuel(stack)
    }

    private fun getItemRunTime(stack: ItemStack): Int {
        return TileEntityFurnace.getItemBurnTime(stack)
    }

    /**
     * Returns whether or not the crusher can run with the current input and output stacks
     * Does not look at fuel
     */
    private fun canRun(): Boolean {
        val inputStack = itemStacks[INPUT_SLOT]
        val outputStack = itemStacks[OUTPUT_SLOT]
        if (inputStack.isEmpty) return false //we can't run if there is no input
        val resultStack = CrusherRecipes.getRecipe(inputStack) //Get the recipe from the recipes registry
        if (resultStack.isEmpty) return false //The recipe doesn't exist, so we can't run
        if (outputStack.isEmpty) return true //At this point if the output stack is empty we can run
        if (!outputStack.isItemEqual(resultStack)) return false //The result of this smelt doesn't match the items in the output
        if (outputStack.count + resultStack.count <= Math.min(inventoryStackLimit, outputStack.maxStackSize)) return true //The items are the same and will fit in the stack
        return false //The items will not fit
    }

    /**
     * Takes one item from the input and crushes it
     */
    private fun crushItem() {
        val inputStack = itemStacks[INPUT_SLOT]
        val outputStack = itemStacks[OUTPUT_SLOT]
        val resultStack = CrusherRecipes.getRecipe(inputStack) //Get the recipe from the recipes registry
        if (outputStack.isEmpty) {
            itemStacks[OUTPUT_SLOT] = resultStack.copy() //Put a new stack of the result into the output
        } else if (outputStack.isItemEqual(resultStack)) {
            outputStack.grow(resultStack.count) //Increase the number of items in the output by the count in the result
        }

        inputStack.shrink(1) //Remove an item from the input
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

    override fun onSlotChanged(index: Int, stack: ItemStack) {
        totalProgress = getCrushTime(stack)
        progress = 0
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        generator.readFromNBT(compound)
        /*
        crusherRunTime = compound.getInteger("run_time")
        progress = compound.getInteger("crush_time")
        totalProgress = compound.getInteger("total_crush_time")
        currentItemRunTime = getItemRunTime(itemStacks[FUEL_SLOT])
        */
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(compound)
        generator.writeToNBT(compound)
        /*
        compound.setInteger("run_time", crusherRunTime)
        compound.setInteger("crush_time", progress)
        compound.setInteger("total_crush_time", totalProgress)
        return compound
        */
        return compound
    }

    fun isRunning() = crusherRunTime > 0

    override fun update() {
        if (!world.isRemote) {
            val fuelStack = itemStacks[FUEL_SLOT]
            generator.update(fuelStack)
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

    /**
     * Apparently this is needed to synchronize between the client and the server in the GUI,
     * although I don't understand why NBT can't just be used for this...
     */
    /*
    override fun getField(id: Int): Int {
        return when (id) {
            FIELD_RUN_TIME -> crusherRunTime
            FIELD_CURRENT_ITEM_RUN_TIME -> currentItemRunTime
            FIELD_PROGRESS -> progress
            FIELD_TOTAL_PROGRESS -> totalProgress
            else -> return 0
        }
    }

    override fun setField(id: Int, value: Int) {
        when (id) {
            FIELD_RUN_TIME -> {
                crusherRunTime = value
            }
            FIELD_CURRENT_ITEM_RUN_TIME -> {
                currentItemRunTime = value
            }
            FIELD_PROGRESS -> {
                progress = value
            }
            FIELD_TOTAL_PROGRESS -> {
                totalProgress = value
            }
        }
    }

    override fun getFieldCount(): Int {
        return NUM_FIELDS
    }
    */
}