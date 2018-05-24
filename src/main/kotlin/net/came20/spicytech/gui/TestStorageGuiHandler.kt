package net.came20.spicytech.gui

import net.came20.spicytech.container.TestStorageContainer
import net.came20.spicytech.tile.TestStorageTileEntity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object TestStorageGuiHandler : SpicyTechGuiHandler(GuiId.TEST_STORAGE) {
    override fun getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
        val pos = BlockPos(x, y, z)
        val tile = world.getTileEntity(pos)
        if (tile is TestStorageTileEntity) {
            return TestStorageContainer(player.inventory, tile)
        }
        return null
    }

    override fun getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
        val pos = BlockPos(x, y, z)
        val tile = world.getTileEntity(pos)
        if (tile is TestStorageTileEntity) {
            return TestStorageGui(player.inventory, tile)
        }
        return null
    }
}