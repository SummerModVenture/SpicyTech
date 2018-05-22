package net.came20.spicytech.block

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.util.ResourceLocation

object CoolBlock : Block(Material.ROCK) {
    init {
        unlocalizedName = "cool_block"
        setRegistryName("cool_block")
        setCreativeTab(CreativeTabs.COMBAT)
    }
}