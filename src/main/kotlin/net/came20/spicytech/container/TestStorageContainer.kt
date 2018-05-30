package net.came20.spicytech.container

import net.came20.spicytech.tile.TestStorageTileEntity
import net.came20.spicytech.etc.HotbarSlotSet
import net.came20.spicytech.etc.PlayerInventorySlotSet
import net.came20.spicytech.etc.TileSlotSet
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack

class TestStorageContainer(val invPlayer: InventoryPlayer, val tile: TestStorageTileEntity): SpicyTechContainer(invPlayer, tile,
        HotbarSlotSet(invPlayer, 8, 109), //Hotbar
        PlayerInventorySlotSet(invPlayer, 8, 51), //Player inventory
        TileSlotSet("test_storage", tile, 1, 9, 8, 20) //Test inventory
        ) {
    companion object {
        const val MOD_SLOT_LAST = MOD_FIRST_SLOT_INDEX + 9
    }

    override fun onPlayerToContainer(player: EntityPlayer, sourceStack: ItemStack): ItemStack? {
        if (!mergeItemStack(sourceStack, MOD_FIRST_SLOT_INDEX, MOD_SLOT_LAST, false)) {
            if (!mergeWithPlayerInventory(sourceStack)) {
                return ItemStack.EMPTY
            }
        }
        return null
    }

    override fun onContainerToPlayer(player: EntityPlayer, sourceStack: ItemStack): ItemStack? {
        if (!mergeItemStack(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_SLOT_COUNT, false)) {
            return ItemStack.EMPTY
        }
        return null
    }
}