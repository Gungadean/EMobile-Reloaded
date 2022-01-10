package com.ryanjhuston.emobile.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ryanjhuston.emobile.common.container.CellphoneBaseContainer;
import com.ryanjhuston.emobile.common.container.CellphoneFEContainer;
import com.ryanjhuston.emobile.config.EMConfig;
import com.ryanjhuston.emobile.util.ServerUtils;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class FECellphoneScreen extends CellphoneScreen{

    private CellphoneFEContainer rfContainer;

    public FECellphoneScreen(CellphoneBaseContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title);

        rfContainer = (CellphoneFEContainer)screenContainer;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
        this.font.drawText(matrixStack, new StringTextComponent("FE Ender Cellphone"), 8, 6, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX , int mouseY) {
        super.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
        this.blit(matrixStack, this.guiLeft + 127, this.guiTop + 7, 176, 18, 42, 14);
        this.blit(matrixStack, this.guiLeft + 127, this.guiTop + 7, 176, 32, (int) (42 * ((double) this.getEnergyStored() / (double) this.getMaxEnergyStored())), 14);
    }

    @Override
    protected void renderHoveredTooltip(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.renderHoveredTooltip(matrixStack, mouseX, mouseY);

        List<ITextComponent> tooltips = new ArrayList<>();

        if(mouseX >= this.guiLeft + 127 && mouseX <= this.guiLeft + 169 && mouseY >= this.guiTop + 7 && mouseY <= this.guiTop + 21) {
            tooltips.add(new StringTextComponent(this.getEnergyStored() + " / " + this.getMaxEnergyStored()));
            if(this.getEnergyStored() == 0) {
                tooltips.add(new TranslationTextComponent("gui.cellphone.fe.1"));
                tooltips.add(new TranslationTextComponent("gui.cellphone.fe.2"));
                if(this.minecraft.player.isCreative()) {
                    tooltips.add(new TranslationTextComponent("gui.cellphone.pearls.creative"));
                }
            }
            tooltips.add(new TranslationTextComponent("gui.cellphone.fe.3", EMConfig.client_feCellphoneEnergyPerUse));
            func_243308_b(matrixStack, tooltips, mouseX, mouseY);
        }
    }

    @Override
    protected boolean hasEnoughFuel() {
        return this.minecraft.player.isCreative() || (getEnergyStored() - EMConfig.client_feCellphoneEnergyPerUse) > 0;
    }

    private int getEnergyStored() {
        return rfContainer.energyHandler.getEnergyStored();
    }

    private int getMaxEnergyStored() {
        return EMConfig.client_feCellphoneMaxEnergy;
    }
}
