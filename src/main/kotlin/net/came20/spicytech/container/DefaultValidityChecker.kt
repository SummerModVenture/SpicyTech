package net.came20.spicytech.container

import net.minecraft.item.ItemStack

object DefaultValidityChecker: IValidityChecker {
    override fun checkValidity(stack: ItemStack): Boolean {
        return true
    }
}