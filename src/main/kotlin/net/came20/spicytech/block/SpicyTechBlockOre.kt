package net.came20.spicytech.block

import net.minecraft.block.material.Material

abstract class SpicyTechBlockOre(name: String, harvestLevel: Int): SpicyTechBlock(name, Material.ROCK, 3f, 5f) {
    init {
        setHarvestLevel("pickaxe", harvestLevel)
    }
}