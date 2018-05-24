package net.came20.spicytech.block

import net.came20.spicytech.SpicyTech
import net.came20.spicytech.gui.SpicyTechGuiHandler
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * Will need later for directional blocks
 */
abstract class SpicyTechBlockMachine(name: String, private val guiHandler: SpicyTechGuiHandler): SpicyTechBlockContainer(name, Material.IRON) {
    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState?, playerIn: EntityPlayer, hand: EnumHand?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (worldIn.isRemote) return true
        playerIn.openGui(SpicyTech.instance, guiHandler.id.ordinal, worldIn, pos.x, pos.y, pos.z)
        return true
    }
}