package assets.spicytech

import net.minecraft.item.ItemStack
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

fun Collection<ItemStack>.hasStackIgnoreDurability(stack: ItemStack): Boolean {
    forEach {
        if (it.isItemEqualIgnoreDurability(stack)) return true
    }
    return false
}