package net.came20.spicytech.network

import net.came20.spicytech.ModInfo
import net.came20.spicytech.tile.IPoweredMachineAccess
import net.came20.spicytech.tile.SpicyTechMachineTileEntity
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.*

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
class PowerUpdatePacketHandler: IMessageHandler<PowerUpdatePacket, IMessage> {
    companion object {
        private val queue = Vector<PowerUpdatePacket>()

        @SubscribeEvent
        @JvmStatic fun onClientTick(event: TickEvent.ClientTickEvent) {
            if (event.phase == TickEvent.Phase.START) {
                while (queue.isNotEmpty()) {
                    val update = queue.removeAt(0)
                    if (update.dim == Minecraft.getMinecraft().world.provider.dimension) { //If the dimension of the update is the dimension we are in
                        val tile = Minecraft.getMinecraft().world.getTileEntity(update.pos)
                        if (tile is IPoweredMachineAccess) {
                            tile.setPower(update.power)
                        }
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    override fun onMessage(message: PowerUpdatePacket?, ctx: MessageContext?): IMessage? {
        queue.add(message)
        return null
    }
}