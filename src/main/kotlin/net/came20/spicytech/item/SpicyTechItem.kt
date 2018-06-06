package net.came20.spicytech.item

import net.came20.spicytech.ModInfo
import net.came20.spicytech.tab.SpicyTechTab
import net.minecraft.item.Item

abstract class SpicyTechItem(name: String): Item() {
    init {
        unlocalizedName = "${ModInfo.MODID}:$name"
        setRegistryName(name)
        creativeTab = SpicyTechTab
    }
}