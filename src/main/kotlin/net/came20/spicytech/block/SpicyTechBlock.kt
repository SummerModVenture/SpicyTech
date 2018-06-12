package net.came20.spicytech.block

import net.came20.spicytech.ModInfo
import net.came20.spicytech.tab.SpicyTechTab
import net.minecraft.block.Block
import net.minecraft.block.material.Material

abstract class SpicyTechBlock(name: String, material: Material, hardness: Float = 1.5f, resistance: Float = 10f): Block(material) {
    init {
        unlocalizedName = "${ModInfo.MODID}:$name"
        setRegistryName(name)
        setCreativeTab(SpicyTechTab)
        setHardness(hardness)
        setResistance(resistance)
    }
}