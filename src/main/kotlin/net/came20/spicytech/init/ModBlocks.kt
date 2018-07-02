package net.came20.spicytech.init

import net.came20.spicytech.ModInfo
import net.came20.spicytech.block.*
import net.minecraft.block.Block
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
object ModBlocks {
    private val blocks = arrayOf(
            TestStorageBlock,
            BasicCrusherBlock,
            BasicMachineFrameBlock,
            AdvancedMachineFrameBlock,
            TitaniumOreBlock,
            TitaniumBlock,
            ComponentWorkbenchBlock
    )

    private fun registerRender(b: Block) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0, ModelResourceLocation(b.registryName, "inventory"))
    }

    private fun itemBlock(b: Block) = ItemBlock(b).setRegistryName(b.registryName)

    @SubscribeEvent
    @JvmStatic fun registerBlocks(e: RegistryEvent.Register<Block>) {
        e.registry.registerAll(*blocks)
    }

    @SubscribeEvent
    @JvmStatic fun registerItemBlocks(e: RegistryEvent.Register<Item>) {
        e.registry.registerAll(*blocks.map { itemBlock(it) }.toTypedArray())
    }

    @SubscribeEvent
    @JvmStatic fun registerRenders(e: ModelRegistryEvent) {
        blocks.forEach {
            registerRender(it)
        }
    }

    fun init() {

    }
}