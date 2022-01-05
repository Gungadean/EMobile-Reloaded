package com.ryanjhuston.emobile.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ryanjhuston.emobile.common.container.CellphoneBaseContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class PearlCellphoneScreen extends CellphoneScreen {

    public PearlCellphoneScreen(CellphoneBaseContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title);
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
    protected boolean hasEnoughFuel() {
        return this.minecraft.player.isCreative() || this.getPearls() > 0;
    }

    private int getPearls() {
        ItemStack pearls = this.container.getInventory().get(0);
        return pearls != null && pearls.getItem() instanceof EnderPearlItem ? pearls.getCount() : 0;
    }
}
