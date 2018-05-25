package net.came20.spicytech.container

import net.came20.spicytech.SpicyTech
import net.came20.spicytech.tile.TestStorageTileEntity
import net.came20.spicytech.slotset.HotbarSlotSet
import net.came20.spicytech.slotset.PlayerInventorySlotSet
import net.came20.spicytech.slotset.TileSlotSet
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.item.ItemStack

class TestStorageContainer(val invPlayer: InventoryPlayer, val tile: TestStorageTileEntity): SpicyTechContainer(invPlayer, tile,
        HotbarSlotSet(invPlayer, 8, 109), //Hotbar
        PlayerInventorySlotSet(invPlayer, 8, 51), //Player inventory
        TileSlotSet("test_storage", tile,1, 9, 8, 20) //Test inventory
        ) {
    override fun onPlayerToContainer(player: EntityPlayer, sourceStack: ItemStack): ItemStack? {
        SpicyTech.logger.info("player -> container")
        return ItemStack.EMPTY
    }

    override fun onContainerToPlayer(player: EntityPlayer, sourceStack: ItemStack): ItemStack? {
        SpicyTech.logger.info("container -> player")
        return ItemStack.EMPTY
    }
}