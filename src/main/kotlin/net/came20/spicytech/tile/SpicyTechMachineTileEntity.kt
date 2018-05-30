package net.came20.spicytech.tile

import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.ISidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World
import java.util.*
import kotlin.experimental.and

abstract class SpicyTechMachineTileEntity(numStacks: Int): TileEntity(), ISidedInventory, ITickable {
    protected val itemStacks = Array<ItemStack>(numStacks, {ItemStack.EMPTY})

    override fun openInventory(player: EntityPlayer?) {}
    override fun closeInventory(player: EntityPlayer?) {}
    override fun getField(id: Int) = 0
    override fun setField(id: Int, value: Int) {}
    override fun getFieldCount() = 0
    override fun update() {}
    override fun hasCustomName() = false
    override fun getDisplayName() = if (hasCustomName()) TextComponentString(name) else TextComponentTranslation(name)

    override fun clear() {
        Arrays.fill(itemStacks, ItemStack.EMPTY)
    }

    override fun getSizeInventory(): Int {
        return itemStacks.size
    }

    override fun isEmpty(): Boolean {
        return itemStacks.all { it.isEmpty }
    }

    override fun getStackInSlot(index: Int): ItemStack {
        return itemStacks[index]
    }

    override fun decrStackSize(index: Int, count: Int): ItemStack {
        val stack = getStackInSlot(index)
        if (stack.isEmpty) return ItemStack.EMPTY
        val removed: ItemStack
        if (stack.count <= count) {
            removed = stack
            setInventorySlotContents(index, ItemStack.EMPTY)
        } else {
            removed = stack.splitStack(count)
            if (stack.count == 0) {
                setInventorySlotContents(index, ItemStack.EMPTY)
            }
        }
        markDirty()
        return removed
    }

    override fun setInventorySlotContents(index: Int, stack: ItemStack) {
        val current = itemStacks[index]
        val flag = !stack.isEmpty && stack.isItemEqual(current) && ItemStack.areItemStackTagsEqual(stack, current)
        itemStacks[index] = stack
        if (stack.count > inventoryStackLimit) {
            stack.count = inventoryStackLimit
        }
        if (!flag) {
            onSlotChanged(index, stack)
            markDirty()
        }
    }

    /**
     * Can be overridden to add behaviour for when a slot is changed (by either a player or in code)
     */
    protected open fun onSlotChanged(index: Int, stack: ItemStack) {
    }

    override fun getInventoryStackLimit(): Int {
        return 64
    }

    override fun isUsableByPlayer(player: EntityPlayer): Boolean {
        if (world.getTileEntity(pos) != this) return false
        return player.getDistanceSq(pos.x + .5, pos.y + .5, pos.z + .5) < 64
    }

    override fun isItemValidForSlot(index: Int, stack: ItemStack): Boolean {
        return true
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(compound)
        val allSlots = NBTTagList()
        itemStacks.forEachIndexed {
            i, stack ->
            if (!stack.isEmpty) {
                val slotData = NBTTagCompound()
                slotData.setByte("slot", i.toByte())
                stack.writeToNBT(slotData)
                allSlots.appendTag(slotData)
            }
        }
        compound.setTag("slots", allSlots)
        return compound
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        val allSlots = compound.getTagList("slots", 10)
        clear()
        for (i in 0 until allSlots.tagCount()) {
            val slotData = allSlots.getCompoundTagAt(i)
            val slotIndex = (slotData.getByte("slot") and 255.toByte()).toInt()
            if (slotIndex in 0..(sizeInventory - 1)) {
                itemStacks[slotIndex] = ItemStack(slotData)
            }
        }
    }

    override fun removeStackFromSlot(index: Int): ItemStack {
        val stack = getStackInSlot(index)
        if (!stack.isEmpty) setInventorySlotContents(index, ItemStack.EMPTY)
        return stack
    }

    fun dropItems(world: World, pos: BlockPos) {
        for (i in 0 until sizeInventory) {
            val item = EntityItem(world, pos.x + .5, pos.y + .5, pos.z + .5, getStackInSlot(i))

            val multiplier = .1
            val motionX = world.rand.nextFloat() - .5
            val motionY = world.rand.nextFloat() - .5
            val motionZ = world.rand.nextFloat() - .5

            item.motionX = motionX * multiplier
            item.motionY = motionY * multiplier
            item.motionZ = motionZ * multiplier

            world.spawnEntity(item)
        }
    }

    override fun getSlotsForFace(side: EnumFacing): IntArray {
        return IntArray(itemStacks.size, {it})
    }

    override fun canExtractItem(index: Int, stack: ItemStack, direction: EnumFacing): Boolean {
        return true
    }

    override fun canInsertItem(index: Int, itemStackIn: ItemStack, direction: EnumFacing): Boolean {
        return true
    }
}