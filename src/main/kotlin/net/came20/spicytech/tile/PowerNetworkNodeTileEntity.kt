package net.came20.spicytech.tile

import assets.spicytech.toArray
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos

open class PowerNetworkNodeTileEntity : TileEntity() {
    var hasController = false
    var controllerPosition = BlockPos(0, 0, 0)

    override fun writeToNBT(compound: NBTTagCompound?): NBTTagCompound {
        val location = controllerPosition.toArray()
        compound?.setBoolean("hasController", hasController)
        compound?.setIntArray("controllerPosition", location)
        return super.writeToNBT(compound)
    }

    override fun readFromNBT(compound: NBTTagCompound?) {
        if (compound != null) {
            hasController = if (compound.hasKey("hasController"))
                compound.getBoolean("hasController")
            else false

            val pos = if (compound.hasKey("controllerPosition"))
                compound.getIntArray("controllerPosition")
            else intArrayOf(0, 0, 0)

            controllerPosition = BlockPos(pos[0], pos[1], pos[2])
        }
        super.readFromNBT(compound)
    }
}