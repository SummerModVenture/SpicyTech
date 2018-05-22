package net.came20.spicytech

import net.minecraft.init.Blocks
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.apache.logging.log4j.Logger

@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION)
class TestMod {
    companion object {
        lateinit var logger: Logger
    }

    @Mod.EventHandler
    fun preInit(e: FMLPreInitializationEvent) {
        logger = e.modLog
    }

    @Mod.EventHandler
    fun init(e: FMLInitializationEvent) {
        logger.info("GRASS BLOCK >> ${Blocks.DIRT.registryName}")
    }
}