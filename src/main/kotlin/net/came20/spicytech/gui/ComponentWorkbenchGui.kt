package net.came20.spicytech.gui

import net.came20.spicytech.ModInfo
import net.came20.spicytech.container.ComponentWorkbenchContainer
import net.came20.spicytech.inventory.ComponentWorkbenchInventory
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.awt.Color

@SideOnly(Side.CLIENT)
class ComponentWorkbenchGui(val invPlayer: InventoryPlayer, val tile: ComponentWorkbenchInventory, world: World, pos: BlockPos): GuiContainer(ComponentWorkbenchContainer(invPlayer, tile, world, pos)) {
    companion object {
        private val texture = ResourceLocation(ModInfo.MODID, "textures/gui/component_workbench_bg.png")
    }

    init {
        xSize = 176
        ySize = 216
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        Minecraft.getMinecraft().textureManager.bindTexture(texture)
        GlStateManager.color(1f, 1f, 1f, 1f)
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize)
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        val invName = tile.displayName.unformattedComponentText
        fontRenderer.drawString(invName, xSize / 2 - fontRenderer.getStringWidth(invName) / 2, 5, Color.darkGray.rgb)
        fontRenderer.drawString(invPlayer.displayName?.unformattedComponentText ?: "ERROR", 8, 122, Color.darkGray.rgb)
    }
}