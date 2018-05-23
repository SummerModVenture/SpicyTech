package net.came20.spicytech.tile

import net.came20.spicytech.SpicyTech
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable

class PowerControllerTileEntity: TileEntity(), ITickable {
    var power = 0.0

    override fun update() {
        power += 0.01
    }

    override fun writeToNBT(compound: NBTTagCompound?): NBTTagCompound {
        compound?.setDouble("power", power)
        SpicyTech.logger.info("Wrote controller to nbt")
        return super.writeToNBT(compound)
    }

    override fun readFromNBT(compound: NBTTagCompound?) {
        if (compound != null && compound.hasKey("power")) power = compound.getDouble("power")
        SpicyTech.logger.info("Read controller from nbt")
        super.readFromNBT(compound)
    }
}