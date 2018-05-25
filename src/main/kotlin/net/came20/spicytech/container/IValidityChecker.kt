package net.came20.spicytech.container

import net.minecraft.item.ItemStack

interface IValidityChecker {
    fun checkValidity(stack: ItemStack): Boolean
}