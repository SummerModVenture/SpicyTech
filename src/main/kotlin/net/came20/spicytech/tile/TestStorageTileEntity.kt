package net.came20.spicytech.tile

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import java.util.*
import kotlin.experimental.and

class TestStorageTileEntity: SpicyTechMachineTileEntity(9) {
    private val itemStacks = Array<ItemStack>(9, {ItemStack.EMPTY})

    override fun getName(): String {
        return "container.teststorage.name"
    }
}