package net.came20.spicytech.gui

import net.came20.spicytech.ModInfo
import net.came20.spicytech.container.BasicCrusherContainer
import net.came20.spicytech.tile.BasicCrusherTileEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.awt.Color

@SideOnly(Side.CLIENT)
class BasicCrusherGui(val invPlayer: InventoryPlayer, val tile: BasicCrusherTileEntity): GuiContainer(BasicCrusherContainer(invPlayer, tile)) {
    companion object {
        private val texture = ResourceLocation(ModInfo.MODID, "textures/gui/basic_crusher_bg.png")
    }

    init {
        xSize = 176
        ySize = 166
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        val i = (this.width - this.xSize) / 2
        val j = (this.height - this.ySize) / 2
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
        if (mouseX in i + 9 until i + 9 + 14 && mouseY in j + 8 until j + 8 + 42) {
            drawHoveringText("${getPower()}/${getMaxPower()} SJ", mouseX, mouseY)
        }
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        val invName = tile.displayName.unformattedComponentText
        fontRenderer.drawString(invName, xSize / 2 - fontRenderer.getStringWidth(invName) / 2, 6, Color.darkGray.rgb)
        fontRenderer.drawString(invPlayer.displayName.unformattedComponentText, 8, this.ySize - 96 + 3, Color.darkGray.rgb)
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        Minecraft.getMinecraft().textureManager.bindTexture(texture)
        GlStateManager.color(1f, 1f, 1f, 1f)
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize)
        val i = (this.width - this.xSize) / 2
        val j = (this.height - this.ySize) / 2
        if (isBurning()) { //If the furnace is burning
            val k = getRuntimeScaled(13)
            drawTexturedModalRect(i + 28, j + 67 - k, 176, 12 - k, 14, k + 1)
        }
        val l = getProgressScaled(24)
        drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16)
        val p = getPowerScaled(42)
        drawTexturedModalRect(i + 9, j + 8 + 42 - p, 176, 73 - p, 14, p)
    }

    private fun isBurning() = tile.generator.isRunning()

    private fun getRuntimeScaled(pixels: Int): Int {
        return (tile.generator.getBurnTimePercentage() * pixels).toInt()
    }

    private fun getProgressScaled(pixels: Int): Int {
        return (tile.crusher.getProgressPercentage() * pixels).toInt()
    }

    private fun getPowerScaled(pixels: Int): Int {
        return (tile.generator.getPowerPercentage() * pixels).toInt()
    }

    private fun getPower(): Int {
        return tile.generator.getAvailablePower()
    }

    private fun getMaxPower(): Int {
        return tile.generator.maxPower
    }
}