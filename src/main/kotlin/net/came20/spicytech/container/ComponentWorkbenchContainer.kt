package net.came20.spicytech.container

import net.came20.spicytech.etc.HotbarSlotSet
import net.came20.spicytech.etc.PlayerInventorySlotSet
import net.came20.spicytech.etc.TileSlotSet
import net.came20.spicytech.inventory.ComponentWorkbenchInventory
import net.came20.spicytech.inventory.SpicyTechInventory
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ComponentWorkbenchContainer(invPlayer: InventoryPlayer, tile: ComponentWorkbenchInventory, val world: World, val pos: BlockPos): SpicyTechContainer(invPlayer, tile,
        HotbarSlotSet(invPlayer, 8, 192), //Hotbar
        PlayerInventorySlotSet(invPlayer, 8, 134),
        TileSlotSet("input_grid", tile, 2, 2, 8, 50) { tile.updateRecipes() },
        TileSlotSet("output_grid", tile, 6, 5, 80, 15)
        ) {
    override fun onContainerToPlayer(player: EntityPlayer, sourceStack: ItemStack, index: Int): ItemStack? {
        if (index in MOD_FIRST_SLOT_INDEX until MOD_FIRST_SLOT_INDEX+4) {
            if (!mergeWithPlayerInventory(sourceStack)) {
                if (!mergeWithHotbar(sourceStack)) {
                    return ItemStack.EMPTY
                }
            }
            return null
        } else {
            val ctx = (tile as? ComponentWorkbenchInventory)?.handleRemoveAll(index)
            if (ctx != null) {
                when {
                    mergeWithPlayerInventory(ctx.stack) -> ctx.shrink()
                    mergeWithHotbar(ctx.stack) -> ctx.shrink()
                    else -> return ItemStack.EMPTY
                }
                (tile as? ComponentWorkbenchInventory)?.updateRecipes()
            } else {
                return ItemStack.EMPTY
            }
            return null
        }
    }

    override fun onPlayerToContainer(player: EntityPlayer, sourceStack: ItemStack, index: Int): ItemStack? {
        if (tile.isItemValidForSlot(0, sourceStack)) { //Check if the item is valid for the input slot
            if (!mergeItemStack(sourceStack, MOD_FIRST_SLOT_INDEX + 0, MOD_FIRST_SLOT_INDEX + 4, false)) { //If we can't put the item in that slot
                return ItemStack.EMPTY //This signals the method that calls this one to return an empty stack
            }
        } else {
            return ItemStack.EMPTY
        }
        return null //This signals to the method that calls this one that we completed our move successfully
    }

    override fun onContainerClosed(playerIn: EntityPlayer) {
        super.onContainerClosed(playerIn)
        if (!world.isRemote) {
            (tile as? SpicyTechInventory)?.dropItems(playerIn.world, playerIn.position, ComponentWorkbenchInventory.ITEM_GRID_FIRST_INDEX, ComponentWorkbenchInventory.OUTPUT_GRID_FIRST_INDEX)
        }
    }
}