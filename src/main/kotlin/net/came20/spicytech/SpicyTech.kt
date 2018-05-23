package net.came20.spicytech

import net.came20.spicytech.init.ModBlocks
import net.came20.spicytech.init.ModTileEntities
import net.minecraft.init.Blocks
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.registry.ForgeRegistries
import org.apache.logging.log4j.Logger

@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION)
class SpicyTech {
    companion object {
        lateinit var logger: Logger
    }

    @Mod.EventHandler
    fun preInit(e: FMLPreInitializationEvent) {
        logger = e.modLog
        ModBlocks.init()
    }

    @Mod.EventHandler
    fun init(e: FMLInitializationEvent) {
        ModTileEntities.init()
    }
}