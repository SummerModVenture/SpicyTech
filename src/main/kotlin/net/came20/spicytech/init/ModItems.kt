package net.came20.spicytech.init

import net.came20.spicytech.ModInfo
import net.came20.spicytech.item.GoldBitsItem
import net.came20.spicytech.item.IronBitsItem
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
object ModItems {
    private fun registerRender(i: Item) {
        ModelLoader.setCustomModelResourceLocation(i, 0, ModelResourceLocation(i.registryName, "inventory"))
    }

    @SubscribeEvent
    @JvmStatic fun registerItems(e: RegistryEvent.Register<Item>) {
        e.registry.registerAll(
                IronBitsItem,
                GoldBitsItem
        )
    }

    @SubscribeEvent
    @JvmStatic fun registerRenders(e: ModelRegistryEvent) {
        registerRender(IronBitsItem)
        registerRender(GoldBitsItem)
    }
}