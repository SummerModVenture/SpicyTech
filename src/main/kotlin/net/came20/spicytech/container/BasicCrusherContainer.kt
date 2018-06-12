package net.came20.spicytech.container

import net.came20.spicytech.etc.HotbarSlotSet
import net.came20.spicytech.etc.PlayerInventorySlotSet
import net.came20.spicytech.etc.TileSlotSet
import net.came20.spicytech.tile.BasicCrusherTileEntity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.item.ItemStack

class BasicCrusherContainer(invPlayer: InventoryPlayer, tile: BasicCrusherTileEntity): SpicyTechPowerContainer(invPlayer, tile,
        HotbarSlotSet(invPlayer, 8, 142),
        PlayerInventorySlotSet(invPlayer, 8, 84),
        TileSlotSet("input_slot", tile, 1, 1, 56, 34),
        TileSlotSet("fuel_slot", tile, 1, 1, 8, 54),
        TileSlotSet("output_slot", tile, 1, 1, 116, 35)
        ) {

    override fun onContainerToPlayer(player: EntityPlayer, sourceStack: ItemStack, index: Int): ItemStack? {
        if (!mergeWithPlayerInventory(sourceStack)) {
            if (!mergeWithHotbar(sourceStack)) {
                return ItemStack.EMPTY
            }
        }
        return null
    }

    override fun onPlayerToContainer(player: EntityPlayer, sourceStack: ItemStack, index: Int): ItemStack? {
        if (tile.isItemValidForSlot(BasicCrusherTileEntity.INPUT_SLOT, sourceStack)) { //Check if the item is valid for the input slot
            if (!mergeItemStack(sourceStack, MOD_FIRST_SLOT_INDEX + BasicCrusherTileEntity.INPUT_SLOT, MOD_FIRST_SLOT_INDEX + BasicCrusherTileEntity.INPUT_SLOT + 1, false)) { //If we can't put the item in that slot
                return ItemStack.EMPTY //This signals the method that calls this one to return an empty stack
            }
        } else if (tile.isItemValidForSlot(BasicCrusherTileEntity.FUEL_SLOT, sourceStack)) { //Check if the item is valid for the fuel slot
            if (!mergeItemStack(sourceStack, MOD_FIRST_SLOT_INDEX + BasicCrusherTileEntity.FUEL_SLOT, MOD_FIRST_SLOT_INDEX + BasicCrusherTileEntity.FUEL_SLOT + 1, false)) { //If we can't put the item in that slot
                return ItemStack.EMPTY //see above
            }
        } else {
            return ItemStack.EMPTY
        }
        return null //This signals to the method that calls this one that we completed our move successfully
    }
}