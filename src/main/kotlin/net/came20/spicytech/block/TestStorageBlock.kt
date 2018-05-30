package net.came20.spicytech.block

import net.came20.spicytech.guihandler.TestStorageGuiHandler
import net.came20.spicytech.tile.TestStorageTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

object TestStorageBlock: SpicyTechBlockMachine("test_storage", TestStorageGuiHandler) {
    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity? {
        return TestStorageTileEntity()
    }
}