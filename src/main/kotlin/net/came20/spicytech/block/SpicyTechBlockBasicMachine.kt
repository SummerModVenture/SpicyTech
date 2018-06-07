package net.came20.spicytech.block

import net.came20.spicytech.guihandler.SpicyTechGuiHandler
import net.came20.spicytech.tile.IBasicMachineRunningAccess
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.ChunkCache
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraft.world.chunk.Chunk

abstract class SpicyTechBlockBasicMachine(name: String, guiHandler: SpicyTechGuiHandler): SpicyTechBlockMachine(name, guiHandler, Material.ROCK) {
    companion object {
        val BURNING = PropertyBool.create("burning")
    }

    override fun getStateForPlacement(world: World?, pos: BlockPos?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase, hand: EnumHand?): IBlockState {
        val state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand)
        return state.withProperty(BURNING, false)
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, FACING, ACTIVE, BURNING)
    }

    override fun getActualState(state: IBlockState, worldIn: IBlockAccess?, pos: BlockPos?): IBlockState {
        val tile: TileEntity?
        if (worldIn is ChunkCache) {
            tile = worldIn.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK)
        } else {
            tile = worldIn?.getTileEntity(pos)
        }
        if (tile is IBasicMachineRunningAccess) {
            return defaultState
                    .withProperty(FACING, state.getValue(FACING))
                    .withProperty(ACTIVE, tile.isRunning())
                    .withProperty(BURNING, tile.isBurning())
        }
        return state
    }
}