package net.came20.spicytech.block

import net.came20.spicytech.guihandler.ComponentWorkbenchGuiHandler
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

object ComponentWorkbenchBlock: SpicyTechBlockContainer("component_workbench", Material.ROCK, ComponentWorkbenchGuiHandler) {
    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity? {
        return null //this block doesn't need a tile
    }
}