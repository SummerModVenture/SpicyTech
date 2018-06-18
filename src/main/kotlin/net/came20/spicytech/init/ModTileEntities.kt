package net.came20.spicytech.init

import net.came20.spicytech.ModInfo
import net.came20.spicytech.tile.BasicCrusherTileEntity
import net.came20.spicytech.tile.TestStorageTileEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.registry.GameRegistry

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
object ModTileEntities {
    fun init() {
        GameRegistry.registerTileEntity(TestStorageTileEntity::class.java, ResourceLocation(ModInfo.MODID, "TestStorageTileEntity"))
        GameRegistry.registerTileEntity(BasicCrusherTileEntity::class.java, ResourceLocation(ModInfo.MODID, "BasicCrusherTileEntity"))
    }
}