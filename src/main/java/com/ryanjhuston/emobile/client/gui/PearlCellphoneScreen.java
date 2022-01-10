package com.ryanjhuston.emobile.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ryanjhuston.emobile.common.container.CellphoneBaseContainer;
import com.ryanjhuston.emobile.common.container.CellphoneItemContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class PearlCellphoneScreen extends CellphoneScreen {

    private CellphoneItemContainer itemContainer;

    public PearlCellphoneScreen(CellphoneBaseContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title);

        itemContainer = (CellphoneItemContainer) screenContainer;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
        this.font.drawText(matrixStack, new StringTextComponent("Ender Cellphone"), 8, 6, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX , int mouseY) {
        super.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
        this.blit(matrixStack, this.guiLeft + 151, this.guiTop + 7, 176, 0, 18, 18);
    }

    @Override
    protected void renderHoveredTooltip(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.renderHoveredTooltip(matrixStack, mouseX, mouseY);

        List<ITextComponent> tooltips = new ArrayList<>();

        if (this.hoveredSlot != null) {
            if (mouseX >= this.guiLeft + 151 && mouseX <= this.guiLeft + 169 && mouseY >= this.guiTop + 7 && mouseY <= this.guiTop + 25 && !this.hoveredSlot.getHasStack()) {
                tooltips.add(new TranslationTextComponent("gui.cellphone.pearls.1"));
                tooltips.add(new TranslationTextComponent("gui.cellphone.pearls.2"));
                if (this.minecraft.player.isCreative()) {
                    tooltips.add(new TranslationTextComponent("gui.cellphone.pearls.creative"));
                }
                func_243308_b(matrixStack, tooltips, mouseX, mouseY);
            }
        }
    }

    @Override
    protected boolean hasEnoughFuel() {
        return this.minecraft.player.isCreative() || itemContainer.itemStackHandler.getStoredPearls() > 0;
    }
}
