package net.came20.spicytech.block

import net.came20.spicytech.guihandler.BasicCrusherGuiHandler
import net.came20.spicytech.tile.BasicCrusherTileEntity
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

object BasicCrusherBlock : SpicyTechBlockBasicMachine("basic_crusher", BasicCrusherGuiHandler) {
    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity? {
        return BasicCrusherTileEntity()
    }

    override fun onBlockAdded(worldIn: World?, pos: BlockPos?, state: IBlockState?) {
        super.onBlockAdded(worldIn, pos, state)
    }
}