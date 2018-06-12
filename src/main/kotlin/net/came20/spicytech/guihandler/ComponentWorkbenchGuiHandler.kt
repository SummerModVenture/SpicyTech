package net.came20.spicytech.guihandler

import net.came20.spicytech.container.ComponentWorkbenchContainer
import net.came20.spicytech.gui.ComponentWorkbenchGui
import net.came20.spicytech.inventory.ComponentWorkbenchInventory
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object ComponentWorkbenchGuiHandler: SpicyTechGuiHandler(GuiId.COMPONENT_WORKBENCH) {
    override fun getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
        return ComponentWorkbenchGui(player.inventory, ComponentWorkbenchInventory(), world, BlockPos(x, y, z))
    }

    override fun getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
        return ComponentWorkbenchContainer(player.inventory, ComponentWorkbenchInventory(), world, BlockPos(x, y, z))
    }
}