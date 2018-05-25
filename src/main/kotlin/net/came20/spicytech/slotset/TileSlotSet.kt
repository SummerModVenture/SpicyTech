package net.came20.spicytech.slotset

import net.came20.spicytech.container.DefaultValidityChecker
import net.came20.spicytech.container.IValidityChecker
import net.minecraft.inventory.IInventory

class TileSlotSet(name: String,
                  inventory: IInventory,
                  numRows: Int,
                  numCols: Int,
                  startXPos: Int,
                  startYPos: Int,
                  xSpacing: Int = 18,
                  ySpacing: Int = 18,
                  validityChecker: IValidityChecker = DefaultValidityChecker) : SlotSetBase(
        name, inventory, numRows, numCols, startXPos, startYPos, xSpacing, ySpacing, validityChecker
)