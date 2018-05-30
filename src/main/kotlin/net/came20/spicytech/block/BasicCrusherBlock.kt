package net.came20.spicytech.block

import net.came20.spicytech.guihandler.BasicCrusherGuiHandler
import net.came20.spicytech.tile.BasicCrusherTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

object BasicCrusherBlock : SpicyTechBlockMachine("basic_crusher", BasicCrusherGuiHandler) {
    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity? {
        return BasicCrusherTileEntity()
    }
}