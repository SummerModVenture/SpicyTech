package net.came20.spicytech.etc

import net.minecraft.inventory.IInventory

class TileSlotSet(name: String,
                  inventory: IInventory,
                  numRows: Int,
                  numCols: Int,
                  startXPos: Int,
                  startYPos: Int,
                  xSpacing: Int = 18,
                  ySpacing: Int = 18,
                  onChanged: (Int) -> Unit = {}) : SlotSetBase(
        name, inventory, numRows, numCols, startXPos, startYPos, xSpacing, ySpacing, onChanged)