package net.came20.spicytech.container

import net.came20.spicytech.SpicyTech
import net.came20.spicytech.tile.TestStorageTileEntity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack

class TestStorageContainer(val playerInventory: InventoryPlayer, val tile: TestStorageTileEntity): Container() {
    companion object {
        private val HOTBAR_SLOT_COUNT = 9
        private val PLAYER_INVENTORY_ROW_COUNT = 3
        private val PLAYER_INVENTORY_COLUMN_COUNT = 9
        private val PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT
        private val VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT

        private val VANILLA_FIRST_SLOT_INDEX = 0
        private val TILE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT
        private val TILE_INVENTORY_SLOT_COUNT = 9
        private val TILE_INVENTORY_LAST_SLOT_INDEX = TILE_INVENTORY_FIRST_SLOT_INDEX + TILE_INVENTORY_SLOT_COUNT - 1

        private val SLOT_X_SPACING = 18
        private val SLOT_Y_SPACING = 18
        private val HOTBAR_XPOS = 8
        private val HOTBAR_YPOS = 109
        private val PLAYER_INVENTORY_XPOS = 8
        private val PLAYER_INVENTORY_YPOS = 51

        private val TILE_INVENTORY_XPOS = 8
        private val TILE_INVENTORY_YPOS = 51
    }

    init {
        for (slotNumber in 0 until HOTBAR_SLOT_COUNT) {
            addSlotToContainer(Slot(playerInventory, slotNumber, HOTBAR_XPOS + SLOT_X_SPACING * slotNumber, HOTBAR_YPOS))
        }

        for (y in 0 until PLAYER_INVENTORY_ROW_COUNT) {
            for (x in 0 until PLAYER_INVENTORY_COLUMN_COUNT) {
                val slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x
                val xpos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING
                val ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING
                addSlotToContainer(Slot(playerInventory, slotNumber, xpos, ypos))
            }
        }

        for (slotNumber in 0 until TILE_INVENTORY_SLOT_COUNT) {
            addSlotToContainer(Slot(tile, slotNumber, TILE_INVENTORY_XPOS + SLOT_X_SPACING * slotNumber, TILE_INVENTORY_YPOS))
        }
    }

    override fun transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack {
        val sourceSlot = inventorySlots[index]
        if (sourceSlot == null || !sourceSlot.hasStack) return ItemStack.EMPTY
        val sourceStack = sourceSlot.stack
        val sourceStackCopy = sourceStack.copy()

        if (index in VANILLA_FIRST_SLOT_INDEX until TILE_INVENTORY_FIRST_SLOT_INDEX) {
            if (!mergeItemStack(sourceStack, TILE_INVENTORY_FIRST_SLOT_INDEX, TILE_INVENTORY_FIRST_SLOT_INDEX + TILE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY
            }
        } else if (index in TILE_INVENTORY_FIRST_SLOT_INDEX..TILE_INVENTORY_LAST_SLOT_INDEX) {
            if (!mergeItemStack(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY
            }
        } else {
            SpicyTech.logger.error("Invalid slot index: " + index)
            return ItemStack.EMPTY
        }

        if (sourceStack.count == 0) {
            sourceSlot.putStack(ItemStack.EMPTY)
        } else {
            sourceSlot.onSlotChanged()
        }
        sourceSlot.onTake(playerIn, sourceStack)
        return sourceStackCopy
    }

    override fun canInteractWith(playerIn: EntityPlayer): Boolean {
        return tile.isUsableByPlayer(playerIn)
    }

    override fun onContainerClosed(playerIn: EntityPlayer?) {
        super.onContainerClosed(playerIn)
        tile.closeInventory(playerIn)
    }
}