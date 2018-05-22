package net.came20.spicytech.init

import net.came20.spicytech.ModInfo
import net.came20.spicytech.TestMod
import net.came20.spicytech.block.*
import net.minecraft.block.Block
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.ShapedRecipes
import net.minecraft.item.crafting.ShapelessRecipes
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.common.crafting.IShapedRecipe
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.GameRegistry

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
object ModBlocks {
    //private lateinit var CoolBlock: Block


    private fun registerRender(b: Block) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0, ModelResourceLocation(b.registryName, "inventory"))
    }

    @SubscribeEvent
    @JvmStatic fun registerBlocks(e: RegistryEvent.Register<Block>) {
        e.registry.apply {
            register(CoolBlock)
        }
    }

    @SubscribeEvent
    @JvmStatic fun registerItemBlocks(e: RegistryEvent.Register<Item>) {
        e.registry.apply {
            register(ItemBlock(CoolBlock).setRegistryName(CoolBlock.registryName))
        }
    }

    @SubscribeEvent
    @JvmStatic fun registerRenders(e: ModelRegistryEvent) {
        registerRender(CoolBlock)
    }

    fun init() {

    }
}