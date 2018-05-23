package net.came20.spicytech.init

import net.came20.spicytech.ModInfo
import net.came20.spicytech.tile.PowerControllerTileEntity
import net.came20.spicytech.tile.PowerNetworkNodeTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.GameRegistry

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
object ModTileEntities {
    fun init() {
        GameRegistry.registerTileEntity(PowerControllerTileEntity::class.java, ModInfo.MODID + "PowerControllerTileEntity")
        GameRegistry.registerTileEntity(PowerNetworkNodeTileEntity::class.java, ModInfo.MODID + "PowerNetworkNodeTileEntity")
    }
}