package net.came20.spicytech.block

import net.came20.spicytech.ModInfo
import net.came20.spicytech.tab.SpicyTechTab
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.util.EnumBlockRenderType


abstract class SpicyTechBlockContainer(name: String, material: Material): BlockContainer(material) {
    init {
        unlocalizedName = "${ModInfo.MODID}:$name"
        setRegistryName(name)
        setCreativeTab(SpicyTechTab)
    }

    override fun getRenderType(state: IBlockState?): EnumBlockRenderType {
        return EnumBlockRenderType.MODEL
    }
}