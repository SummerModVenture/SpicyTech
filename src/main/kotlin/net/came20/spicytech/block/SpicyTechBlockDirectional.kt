package net.came20.spicytech.block

import net.came20.spicytech.guihandler.SpicyTechGuiHandler
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

abstract class SpicyTechBlockDirectional(name: String, material: Material, guiHandler: SpicyTechGuiHandler): SpicyTechBlockContainer(name, material, guiHandler) {
    companion object {
        val FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL)
    }

    override fun getStateForPlacement(world: World?, pos: BlockPos?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase, hand: EnumHand?): IBlockState {
        val state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand)
        return state.withProperty(FACING, placer.horizontalFacing.opposite)
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return state.getValue(FACING).horizontalIndex
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(FACING, EnumFacing.getHorizontal(meta))
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, FACING)
    }
}