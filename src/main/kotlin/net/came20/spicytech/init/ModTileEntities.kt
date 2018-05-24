package net.came20.spicytech.init

import net.came20.spicytech.ModInfo
import net.came20.spicytech.tile.TestStorageTileEntity
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.registry.GameRegistry

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
object ModTileEntities {
    fun init() {
        GameRegistry.registerTileEntity(TestStorageTileEntity::class.java, ModInfo.MODID + "TestStorageTileEntity")
    }
}