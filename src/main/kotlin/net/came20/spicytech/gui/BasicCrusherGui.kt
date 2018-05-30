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
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        fontRenderer.drawString(tile.displayName.unformattedComponentText, 5, 5, Color.darkGray.rgb)
        fontRenderer.drawString(invPlayer.displayName.unformattedComponentText, 8, this.ySize - 96 + 2, Color.darkGray.rgb)
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        Minecraft.getMinecraft().textureManager.bindTexture(texture)
        GlStateManager.color(1f, 1f, 1f, 1f)
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize)
        val i = (this.width - this.xSize) / 2
        val j = (this.height - this.ySize) / 2
        if (isBurning()) { //If the furnace is burning
            val k = getRuntimeScaled(13)
            drawTexturedModalRect(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1)
        }
        val l = getProgressScaled(24)
        drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16)
    }

    private fun isBurning() = tile.getField(BasicCrusherTileEntity.FIELD_RUN_TIME) > 0

    private fun getRuntimeScaled(pixels: Int): Int {
        var i = tile.getField(BasicCrusherTileEntity.FIELD_CURRENT_ITEM_RUN_TIME)
        if (i == 0) i = 200
        return tile.getField(BasicCrusherTileEntity.FIELD_RUN_TIME) * pixels / i
    }

    private fun getProgressScaled(pixels: Int): Int {
        val progress = tile.getField(BasicCrusherTileEntity.FIELD_PROGRESS)
        val totalProgress = tile.getField(BasicCrusherTileEntity.FIELD_TOTAL_PROGRESS)
        if (progress != 0 && totalProgress != 0) {
            return progress * pixels / totalProgress
        }
        return 0
    }
}