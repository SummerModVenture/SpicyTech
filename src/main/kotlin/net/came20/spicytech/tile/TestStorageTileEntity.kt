package net.came20.spicytech.tile

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import java.util.*
import kotlin.experimental.and

class TestStorageTileEntity: TileEntity(), IInventory {
    private val itemStacks = Array<ItemStack>(9, {ItemStack.EMPTY})

    override fun clear() {
        Arrays.fill(itemStacks, ItemStack.EMPTY)
    }

    override fun getSizeInventory(): Int {
        return itemStacks.size
    }

    override fun isEmpty(): Boolean {
        return itemStacks.all {isEmpty}
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
        itemStacks[index] = stack
        if (stack.isEmpty && stack.count > inventoryStackLimit) {
            stack.count = inventoryStackLimit
        }
        markDirty()
    }

    override fun getInventoryStackLimit(): Int {
        return 64
    }

    override fun isUsableByPlayer(player: EntityPlayer): Boolean {
        if (world.getTileEntity(pos) != this) return false
        return player.getDistanceSq(pos.x + .5, pos.y + .5, pos.z + .5) < 64
    }

    override fun isItemValidForSlot(index: Int, stack: ItemStack?): Boolean {
        return true
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
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
        return super.writeToNBT(compound)
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        val allSlots = compound.getTagList("slots", 10)
        clear()
        for (i in 0 until allSlots.tagCount()) {
            val slotData = allSlots.getCompoundTagAt(i)
            val slotIndex = (slotData.getByte("slot") and 255.toByte()).toInt()
            if (slotIndex in 0..(sizeInventory - 1)) {
                itemStacks[slotIndex] = ItemStack(slotData)
            }
        }
        super.readFromNBT(compound)
    }

    override fun getName(): String {
        return "container.teststorage.name"
    }

    override fun hasCustomName(): Boolean {
        return false
    }

    override fun getDisplayName(): ITextComponent? {
        return if (hasCustomName()) {
            TextComponentString(name)
        } else {
            TextComponentTranslation(name)
        }
    }

    override fun removeStackFromSlot(index: Int): ItemStack {
        val stack = getStackInSlot(index)
        if (!stack.isEmpty) setInventorySlotContents(index, ItemStack.EMPTY)
        return stack
    }

    override fun openInventory(player: EntityPlayer?) {

    }

    override fun closeInventory(player: EntityPlayer?) {

    }

    override fun getField(id: Int): Int {
        return 0
    }

    override fun setField(id: Int, value: Int) {

    }

    override fun getFieldCount(): Int {
        return 0
    }
}