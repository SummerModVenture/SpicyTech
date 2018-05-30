package net.came20.spicytech.container

import net.came20.spicytech.tile.SpicyTechMachineTileEntity
import net.came20.spicytech.etc.IVanillaSlotSet
import net.came20.spicytech.etc.SlotSetBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.thread.SidedThreadGroups.CLIENT
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly



abstract class SpicyTechContainer(private val invPlayer: InventoryPlayer, private val tile: SpicyTechMachineTileEntity, vararg val slotSets: SlotSetBase): Container() {
    companion object {
        const val HOTBAR_SLOT_COUNT = 9
        const val PLAYER_INVENTORY_ROW_COUNT = 3
        const val PLAYER_INVENTORY_COLUMN_COUNT = 9
        const val PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT
        const val VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT
        const val VANILLA_FIRST_SLOT_INDEX = 0
        const val PLAYER_INVENTORY_FIRST_SLOT_INDEX = 9
        const val MOD_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT

        fun slotIndex(offset: Int) = MOD_FIRST_SLOT_INDEX + offset

        fun isPlayerInventory(slot: Int) = slot in 0 until MOD_FIRST_SLOT_INDEX
        fun isMod(slot: Int) = slot >= MOD_FIRST_SLOT_INDEX
    }

    init {
        var vanillaSlotIndex = 0
        var slotIndex = 0
        slotSets.forEach {
            val slots: Array<Slot>
            if (it is IVanillaSlotSet) {
                slots = it.buildSlots(vanillaSlotIndex)
                vanillaSlotIndex += it.numSlots
            } else {
                slots = it.buildSlots(slotIndex)
                slotIndex += it.numSlots
            }
            slots.forEach {
                addSlotToContainer(it)
            }
        }
    }

    private var cachedFields: IntArray? = null

    override fun canInteractWith(playerIn: EntityPlayer): Boolean {
        return tile.isUsableByPlayer(playerIn)
    }

    override fun onContainerClosed(playerIn: EntityPlayer?) {
        super.onContainerClosed(playerIn)
        tile.closeInventory(playerIn)
    }

    abstract fun onPlayerToContainer(player: EntityPlayer, sourceStack: ItemStack): ItemStack?
    abstract fun onContainerToPlayer(player: EntityPlayer, sourceStack: ItemStack): ItemStack?

    override fun transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack {
        val sourceSlot = inventorySlots[index]
        if (sourceSlot == null || !sourceSlot.hasStack) return ItemStack.EMPTY
        val sourceStack = sourceSlot.stack
        val copyOfSourceStack = sourceStack.copy()

        if (isPlayerInventory(index)) {
            val stack = onPlayerToContainer(playerIn, sourceStack)
            if (stack != null) return stack
        } else {
            val stack = onContainerToPlayer(playerIn, sourceStack)
            if (stack != null) return stack
        }

        if (sourceStack.count == 0) {
            sourceSlot.putStack(ItemStack.EMPTY)
        } else {
            sourceSlot.onSlotChanged()
        }

        sourceSlot.onTake(playerIn, sourceStack)
        return copyOfSourceStack
    }

    override fun detectAndSendChanges() {
        super.detectAndSendChanges()
        if (tile.fieldCount > 0) {
            var allFieldsHaveChanged = false
            val fieldHasChanged = BooleanArray(tile.fieldCount, { false })
            if (cachedFields == null) {
                cachedFields = IntArray(tile.fieldCount)
                allFieldsHaveChanged = true
            }
            val cachedFields = this.cachedFields!! //force smart cast
            for (i in 0 until cachedFields.size) {
                if (allFieldsHaveChanged || cachedFields[i] != tile.getField(i)) {
                    cachedFields[i] = tile.getField(i)
                    fieldHasChanged[i] = true
                }
            }

            listeners.forEach {
                (0 until cachedFields.size)
                        .filter { fieldHasChanged[it] }
                        .forEach { i -> it.sendWindowProperty(this, i, cachedFields[i]) }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    override fun updateProgressBar(id: Int, data: Int) {
        tile.setField(id, data)
    }

    /**
     * Returns true if successful
     */
    protected fun mergeWithHotbar(source: ItemStack): Boolean {
        return mergeItemStack(source, VANILLA_FIRST_SLOT_INDEX, PLAYER_INVENTORY_FIRST_SLOT_INDEX, false)
    }

    /**
     * Returns true if successful
     */
    protected fun mergeWithPlayerInventory(source: ItemStack): Boolean {
        return mergeItemStack(source, PLAYER_INVENTORY_FIRST_SLOT_INDEX, MOD_FIRST_SLOT_INDEX, false)
    }
}