package net.came20.spicytech.network

import io.netty.buffer.ByteBuf
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.simpleimpl.IMessage

class PowerUpdatePacket(var pos: BlockPos = BlockPos(0, 0, 0), var dim: Int = 0, var power: Int = 0) : IMessage {
    override fun fromBytes(buf: ByteBuf?) {
        if (buf != null) {
            pos = BlockPos.fromLong(buf.readLong())
            dim = buf.readInt()
            power = buf.readInt()
        }
    }

    override fun toBytes(buf: ByteBuf?) {
        if (buf != null) {
            buf.writeLong(pos.toLong())
            buf.writeInt(dim)
            buf.writeInt(power)
        }
    }

    override fun toString(): String {
        return "PowerUpdatePacket{pos=$pos, dim=$dim, power=$power}"
    }
}