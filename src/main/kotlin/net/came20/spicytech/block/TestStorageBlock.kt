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

    override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
        val inventory = worldIn.getTileEntity(pos) as? TestStorageTileEntity
        if (inventory != null) {
            for (i in 0 until inventory.sizeInventory) {
                if (!inventory.getStackInSlot(i).isEmpty) {
                    val item = EntityItem(worldIn, pos.x + .5, pos.y + .5, pos.z + .5, inventory.getStackInSlot(i))
                    val multiplier = 0.1f
                    val motionX = worldIn.rand.nextFloat() - .5
                    val motionY = worldIn.rand.nextFloat() - .5
                    val motionZ = worldIn.rand.nextFloat() - .5

                    item.motionX = motionX * multiplier
                    item.motionY = motionY * multiplier
                    item.motionZ = motionZ * multiplier

                    worldIn.spawnEntity(item)
                }
            }

            inventory.clear()
        }
        super.breakBlock(worldIn, pos, state)
    }


}