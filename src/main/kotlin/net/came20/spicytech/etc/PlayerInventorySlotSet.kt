package net.came20.spicytech.etc

import net.minecraft.entity.player.InventoryPlayer

class PlayerInventorySlotSet(invPlayer: InventoryPlayer, xPos: Int, yPos: Int): SlotSetBase("player_inventory", invPlayer, 3, 9, xPos, yPos), IVanillaSlotSet