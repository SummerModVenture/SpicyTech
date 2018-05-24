package net.came20.spicytech.tile

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation

abstract class SpicyTechMachineTileEntity: TileEntity(), IInventory, ITickable {
    override fun openInventory(player: EntityPlayer?) {

    }

    override fun closeInventory(player: EntityPlayer?) {

    }

    override fun getField(id: Int): Int {
        return 0
    }

    override fun setField(id: Int, value: Int) {

    }

    override fun getFieldCount(): Int {
        return 0
    }

    override fun update() {

    }

    override fun hasCustomName(): Boolean {
        return false
    }

    override fun getDisplayName(): ITextComponent? {
        return if (hasCustomName()) {
            TextComponentString(name)
        } else {
            TextComponentTranslation(name)
        }
    }
}