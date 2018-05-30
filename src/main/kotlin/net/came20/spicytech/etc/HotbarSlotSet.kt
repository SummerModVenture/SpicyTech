package net.came20.spicytech.etc

import net.minecraft.entity.player.InventoryPlayer

class HotbarSlotSet(invPlayer: InventoryPlayer, xPos: Int, yPos: Int): SlotSetBase("hotbar", invPlayer, 1, 9, xPos, yPos), IVanillaSlotSet