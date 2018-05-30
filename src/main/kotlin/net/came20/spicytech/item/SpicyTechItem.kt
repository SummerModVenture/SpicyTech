package net.came20.spicytech.item

import net.came20.spicytech.tab.SpicyTechTab
import net.minecraft.item.Item

abstract class SpicyTechItem(name: String): Item() {
    init {
        unlocalizedName = name
        setRegistryName(name)
        creativeTab = SpicyTechTab
    }
}