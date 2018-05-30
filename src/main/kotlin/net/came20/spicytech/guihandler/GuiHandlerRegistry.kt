package net.came20.spicytech.guihandler

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler

object GuiHandlerRegistry: IGuiHandler {
    private val handlers = hashMapOf<GuiId, IGuiHandler>()

    fun register(id: GuiId, handler: IGuiHandler) {
        handlers.put(id, handler)
    }

    fun register(handler: SpicyTechGuiHandler) {
        register(handler.id, handler)
    }

    override fun getServerGuiElement(ID: Int, player: EntityPlayer?, world: World?, x: Int, y: Int, z: Int): Any? {
        val idObj = GuiId.values()[ID]
        return if (handlers.containsKey(idObj)) {
            handlers[idObj]?.getServerGuiElement(ID, player, world, x, y, z)
        } else {
            null
        }
    }

    override fun getClientGuiElement(ID: Int, player: EntityPlayer?, world: World?, x: Int, y: Int, z: Int): Any? {
        val idObj = GuiId.values()[ID]
        return if (handlers.containsKey(idObj)) {
            handlers[idObj]?.getClientGuiElement(ID, player, world, x, y, z)
        } else {
            null
        }
    }
}