package net.came20.spicytech.block

import net.came20.spicytech.tab.SpicyTechTab
import net.minecraft.block.Block
import net.minecraft.block.material.Material

abstract class SpicyTechBlock(name: String, material: Material): Block(material) {
    init {
        unlocalizedName = name
        setRegistryName(name)
        setCreativeTab(SpicyTechTab)
    }
}