package net.came20.spicytech.block

import net.came20.spicytech.ModInfo
import net.came20.spicytech.SpicyTech
import net.came20.spicytech.guihandler.SpicyTechGuiHandler
import net.came20.spicytech.tab.SpicyTechTab
import net.came20.spicytech.tile.SpicyTechMachineTileEntity
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World


abstract class SpicyTechBlockContainer(name: String, material: Material, private val guiHandler: SpicyTechGuiHandler, hardness: Float = 1.5f, resistance: Float = 10f): BlockContainer(material) {
    init {
        unlocalizedName = "${ModInfo.MODID}:$name"
        setRegistryName(name)
        setCreativeTab(SpicyTechTab)
        setHardness(hardness)
        setResistance(resistance)
    }

    override fun getRenderType(state: IBlockState?): EnumBlockRenderType {
        return EnumBlockRenderType.MODEL
    }


    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState?, playerIn: EntityPlayer, hand: EnumHand?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (worldIn.isRemote) return true
        playerIn.openGui(SpicyTech.instance, guiHandler.id.ordinal, worldIn, pos.x, pos.y, pos.z)
        return true
    }

    override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState?) {
        val tile = worldIn.getTileEntity(pos) as? SpicyTechMachineTileEntity
        tile?.dropItems(worldIn, pos)
        super.breakBlock(worldIn, pos, state) //This is required to destroy the tile entity associated with this block
    }
}