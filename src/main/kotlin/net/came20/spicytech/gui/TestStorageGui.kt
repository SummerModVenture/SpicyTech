package net.came20.spicytech.gui

import net.came20.spicytech.ModInfo
import net.came20.spicytech.container.TestStorageContainer
import net.came20.spicytech.tile.TestStorageTileEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.awt.Color

@SideOnly(Side.CLIENT)
class TestStorageGui(val playerInventory: InventoryPlayer, val tile: TestStorageTileEntity): GuiContainer(TestStorageContainer(playerInventory, tile)) {
    companion object {
        private val texture = ResourceLocation(ModInfo.MODID, "textures/gui/test_storage_bg.png")
    }

    init {
        xSize = 176
        ySize = 133
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        Minecraft.getMinecraft().textureManager.bindTexture(texture)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize)
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        val LABEL_XPOS = 5
        val LABEL_YPOS = 5
        fontRenderer.drawString(tile.displayName?.unformattedComponentText ?: "ERROR", LABEL_XPOS, LABEL_YPOS, Color.darkGray.rgb)
    }
}