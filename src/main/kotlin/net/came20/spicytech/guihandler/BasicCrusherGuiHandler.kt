package net.came20.spicytech.guihandler

import net.came20.spicytech.container.BasicCrusherContainer
import net.came20.spicytech.gui.BasicCrusherGui
import net.came20.spicytech.tile.BasicCrusherTileEntity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object BasicCrusherGuiHandler: SpicyTechGuiHandler(GuiId.BASIC_CRUSHER) {
    override fun getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
        val pos = BlockPos(x, y, z)
        val tile = world.getTileEntity(pos)
        if (tile is BasicCrusherTileEntity) {
            return BasicCrusherGui(player.inventory, tile)
        }
        return null
    }

    override fun getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
        val pos = BlockPos(x, y, z)
        val tile = world.getTileEntity(pos)
        if (tile is BasicCrusherTileEntity) {
            return BasicCrusherContainer(player.inventory, tile)
        }
        return null
    }
}