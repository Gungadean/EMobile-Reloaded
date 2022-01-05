package com.ryanjhuston.emobile.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ryanjhuston.emobile.common.container.CellphoneBaseContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class RFCellphoneScreen extends CellphoneScreen{

    public RFCellphoneScreen(CellphoneBaseContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
        this.font.drawText(matrixStack, new StringTextComponent("RF Ender Cellphone"), 8, 6, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX , int mouseY) {
        super.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
        this.blit(matrixStack, this.guiLeft + 127, this.guiTop + 7, 176, 18, 42, 14);
        this.blit(matrixStack, this.guiLeft + 127, this.guiTop + 7, 176, 32, (int) (42 * ((double) this.getEnergyStored() / (double) this.getMaxEnergyStored())), 14);
    }

    @Override
    protected boolean hasEnoughFuel() {
        return this.minecraft.player.isCreative();
    }

    private int getEnergyStored() {
        return 0;
    }

    private int getMaxEnergyStored() {
        return 0;
    }
}
