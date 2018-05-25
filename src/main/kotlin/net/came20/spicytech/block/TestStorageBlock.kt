package net.came20.spicytech.block

import net.came20.spicytech.SpicyTech
import net.came20.spicytech.gui.TestStorageGuiHandler
import net.came20.spicytech.tile.TestStorageTileEntity
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

object TestStorageBlock: SpicyTechBlockMachine("test_storage", TestStorageGuiHandler) {
    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity? {
        return TestStorageTileEntity()
    }
}