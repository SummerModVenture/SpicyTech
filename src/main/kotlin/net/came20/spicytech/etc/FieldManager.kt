package net.came20.spicytech.etc

import net.came20.spicytech.SpicyTech
import net.came20.spicytech.controller.IController

/**
 * Manages the networking fields for a series of controllers
 * @param startingIndex The index to start making fields at, useful if you have your own fields in your tile
 */
class FieldManager(startingIndex: Int = 0) {
    private val controllers = arrayListOf<ControllerFieldMapping>()
    private var currentIndex = startingIndex

    private class ControllerFieldMapping(val controller: IController, val startIndex: Int) {
        fun getField(id: Int): Int {
            return controller.getField(id - startIndex)
        }

        fun setField(id: Int, value: Int) {
            controller.setField(id - startIndex, value)
        }

        fun getNumFields(): Int {
            return controller.getNumFields()
        }

        fun hasId(id: Int) = id in startIndex until getNumFields() + startIndex
    }

    fun register(controller: IController) {
        controllers.add(ControllerFieldMapping(controller, currentIndex))
        currentIndex += controller.getNumFields()
    }

    fun getField(id: Int): Int {
        return try {
            controllers.first { it.hasId(id) }.getField(id)
        } catch (e: NoSuchElementException) {
            SpicyTech.logger.error("Field manager doesn't have field $id!", e)
            0
        }
    }

    fun setField(id: Int, value: Int) {
        controllers.first { it.hasId(id) }.setField(id, value)
    }

    fun getNumFields(): Int {
        var sum = 0
        controllers.forEach {
            sum += it.getNumFields()
        }
        return sum
    }
}