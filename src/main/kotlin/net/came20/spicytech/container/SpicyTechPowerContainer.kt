package net.came20.spicytech.container

import net.came20.spicytech.SpicyTech
import net.came20.spicytech.etc.SlotSetBase
import net.came20.spicytech.network.PowerUpdatePacket
import net.came20.spicytech.tile.IPoweredMachineAccess
import net.came20.spicytech.tile.SpicyTechMachineTileEntity
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.tileentity.TileEntity

abstract class SpicyTechPowerContainer(invPlayer: InventoryPlayer, tile: SpicyTechMachineTileEntity, vararg slotSets: SlotSetBase): SpicyTechContainer(invPlayer, tile, *slotSets) {
    private var cachedPower = 0

    override fun detectAndSendChanges() {
        super.detectAndSendChanges()

        if (tile is IPoweredMachineAccess && tile is TileEntity) {
            val power = tile.getPower()
            if (power != cachedPower) { //power has changed
                cachedPower = power //update cache
                val packet = PowerUpdatePacket(tile.pos, tile.world.provider.dimension, power)
                if (invPlayer.player is EntityPlayerMP) { //This should always be true, as the client runs a local server
                    SpicyTech.network.sendTo(packet, invPlayer.player as EntityPlayerMP) //This only sends it to the player who has the inventory open
                } else { //If not, we have a fallback
                    SpicyTech.network.sendToAll(packet) //This sends it to all players, which is less performant
                }
            }
        }
    }
}