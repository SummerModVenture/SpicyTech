package net.came20.spicytech.init

import net.came20.spicytech.guihandler.BasicCrusherGuiHandler
import net.came20.spicytech.guihandler.ComponentWorkbenchGuiHandler
import net.came20.spicytech.guihandler.GuiHandlerRegistry
import net.came20.spicytech.guihandler.TestStorageGuiHandler

object ModGuiHandlers {
    fun init() {
        GuiHandlerRegistry.register(TestStorageGuiHandler)
        GuiHandlerRegistry.register(BasicCrusherGuiHandler)
        GuiHandlerRegistry.register(ComponentWorkbenchGuiHandler)
    }
}