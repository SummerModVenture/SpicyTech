package net.came20.spicytech.block

import assets.spicytech.serverOnly
import net.came20.spicytech.SpicyTech
import net.came20.spicytech.tile.PowerControllerTileEntity
import net.came20.spicytech.tile.PowerNetworkNodeTileEntity
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.passive.EntityHorse
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.World

object PowerCableBlock: SpicyTechBlockContainer("power_cable", Material.ROCK) {
    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity? {
        return PowerNetworkNodeTileEntity()
    }

    fun notifyNeighborsPlaced(world: World, controllerPos: BlockPos, hasController: Boolean, myPos: BlockPos, notified: MutableSet<BlockPos>) {
        notified.add(myPos)
        val myTile = world.getTileEntity(myPos) as PowerNetworkNodeTileEntity
        myTile.hasController = hasController
        myTile.controllerPosition = controllerPos
        for (direction in EnumFacing.VALUES) {
            val neighborPos = myPos.offset(direction)
            if (!notified.contains(neighborPos)) {
                val neighborState = world.getBlockState(neighborPos)
                val neighborBlock = neighborState.block
                if (neighborBlock == PowerCableBlock) {
                    SpicyTech.logger.info("$myPos: Notifying neighbor at $neighborPos")
                    (neighborBlock as PowerCableBlock).notifyNeighborsPlaced(world, controllerPos, hasController, neighborPos, notified)
                }
            }
        }
    }

    override fun onBlockPlacedBy(worldIn: World, pos: BlockPos, state: IBlockState?, placer: EntityLivingBase?, stack: ItemStack?) {
        serverOnly {
            val rawTile = worldIn.getTileEntity(pos)
            var controllerFound = false
            if (rawTile != null) {
                val myTile = rawTile as PowerNetworkNodeTileEntity
                for (direction in EnumFacing.VALUES) {
                    val neighborPos = pos.offset(direction)
                    val neighborState = worldIn.getBlockState(neighborPos)
                    val neighborBlock = neighborState.block
                    if (neighborBlock == PowerControllerBlock) {
                        controllerFound = true
                        notifyNeighborsPlaced(worldIn, neighborPos, true, pos, mutableSetOf())
                    }
                }

                if (!controllerFound) {
                    for (direction in EnumFacing.VALUES) {
                        val neighborPos = pos.offset(direction)
                        val neighborState = worldIn.getBlockState(neighborPos)
                        val neighborBlock = neighborState.block
                        if (neighborBlock == PowerCableBlock) {
                            val neighborTile = worldIn.getTileEntity(neighborPos) as PowerNetworkNodeTileEntity
                            if (neighborTile.hasController) {
                                SpicyTech.logger.info("Inheriting controller position: ${neighborTile.controllerPosition}")
                                myTile.hasController = true
                                myTile.controllerPosition = neighborTile.controllerPosition
                                return
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState?, playerIn: EntityPlayer?, hand: EnumHand?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (playerIn?.isSneaking == true) {
            return false
        }
        serverOnly {
            val myTile = worldIn.getTileEntity(pos) as PowerNetworkNodeTileEntity
            if (myTile.hasController) {
                val controllerTile = worldIn.getTileEntity(myTile.controllerPosition) as PowerControllerTileEntity
                playerIn?.sendMessage(TextComponentString("Power Cable: ${controllerTile.power}"))
            } else {
                playerIn?.sendMessage(TextComponentString("Power Cable: No controller"))
            }
            return true
        }
        return true
    }
}