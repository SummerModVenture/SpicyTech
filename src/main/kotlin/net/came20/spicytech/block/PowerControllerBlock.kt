package net.came20.spicytech.block

import assets.spicytech.serverOnly
import net.came20.spicytech.SpicyTech
import net.came20.spicytech.tile.PowerControllerTileEntity
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.World

object PowerControllerBlock: SpicyTechBlockContainer("power_controller", Material.ROCK) {
    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity? {
        return PowerControllerTileEntity()
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState?, playerIn: EntityPlayer?, hand: EnumHand?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (playerIn?.isSneaking == true) {
            return false
        }
        serverOnly {
            val myTile = worldIn.getTileEntity(pos) as PowerControllerTileEntity
            playerIn?.sendMessage(TextComponentString("Power Controller: ${myTile.power}"))
            return true
        }
        return true
    }

    override fun onBlockPlacedBy(worldIn: World, pos: BlockPos, state: IBlockState?, placer: EntityLivingBase?, stack: ItemStack?) {
        serverOnly {
            for (direction in EnumFacing.VALUES) {
                val neighborPos = pos.offset(direction)
                val neighborState = worldIn.getBlockState(neighborPos)
                val neighborBlock = neighborState.block
                if (neighborBlock == PowerCableBlock) {
                    SpicyTech.logger.info("Controller notifying cable at ${neighborPos}")
                    (neighborBlock as PowerCableBlock).notifyNeighborsPlaced(worldIn, pos, true, neighborPos, mutableSetOf())
                }
            }
        }
    }
}