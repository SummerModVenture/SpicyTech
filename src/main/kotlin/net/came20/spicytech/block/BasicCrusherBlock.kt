package net.came20.spicytech.block

import net.came20.spicytech.guihandler.BasicCrusherGuiHandler
import net.came20.spicytech.tile.BasicCrusherTileEntity
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.World

object BasicCrusherBlock : SpicyTechBlockMachine("basic_crusher", BasicCrusherGuiHandler) {
    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity? {
        return BasicCrusherTileEntity()
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState?, playerIn: EntityPlayer, hand: EnumHand?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ)
        val tile = worldIn.getTileEntity(pos) as BasicCrusherTileEntity
        playerIn.sendMessage(TextComponentString("Generator:  power=${tile.generator.getAvailablePower()}  running=${tile.generator.isRunning()}  burnPercent=${tile.generator.getBurnTimePercentage()}"))
        return true
    }
}