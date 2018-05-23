package assets.spicytech

import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.relauncher.Side

fun BlockPos.toArray() = intArrayOf(x, y, z)

inline fun serverOnly(action: () -> Unit) {
    if (FMLCommonHandler.instance().effectiveSide == Side.SERVER) {
        action()
    }
}

inline fun clientOnly(action: () -> Unit) {
    if (FMLCommonHandler.instance().effectiveSide == Side.CLIENT) {
        action()
    }
}