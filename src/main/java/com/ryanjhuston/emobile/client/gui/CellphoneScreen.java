package com.ryanjhuston.emobile.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.common.container.CellphoneBaseContainer;
import com.ryanjhuston.emobile.network.PacketHandler;
import com.ryanjhuston.emobile.network.message.*;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class CellphoneScreen extends ContainerScreen<CellphoneBaseContainer> {

    private static final ResourceLocation CELLPHONE_GUI = new ResourceLocation(EMobileMod.MODID, "textures/gui/item/cellphone.png");

    protected TextFieldWidget accept;
    protected TextFieldWidget receiver;
    protected SmallButton buttonAccept;
    protected SmallButton buttonReceiver;
    protected SmallButton buttonHome;
    protected SmallButton buttonSpawn;
    protected SmallButton buttonCancel;

    public CellphoneScreen(CellphoneBaseContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title);

        this.xSize = 176;
        this.ySize = 203;
    }

    public void tick() {
        super.tick();
        this.accept.tick();
        this.receiver.tick();
    }

    @Override
    public void init() {
        this.minecraft.keyboardListener.enableRepeatEvents(true);

        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        this.accept = new TextFieldWidget(this.font, this.guiLeft + 8, this.guiTop + 41, 127, 12, new TranslationTextComponent("gui.cellphone.accept"));
        this.accept.setTextColor(-1);
        this.accept.setMaxStringLength(35);
        //TO-DO: add config
        this.accept.setEnabled(true);
        this.accept.setVisible(true);
        this.children.add(accept);

        this.receiver = new TextFieldWidget(this.font, this.guiLeft + 8, this.guiTop + 71, 127, 12, new TranslationTextComponent("gui.cellphone.teleport"));
        this.receiver.setTextColor(-1);
        this.receiver.setMaxStringLength(35);
        //TO-DO: add config
        this.receiver.setEnabled(true);
        this.receiver.setEnabled(true);
        this.children.add(receiver);

        this.buttonAccept = new SmallButton(this.guiLeft + 139, this.guiTop + 40, 30, 14, new StringTextComponent("OK"), p_onPress_1_ -> acceptPlayer());
        this.buttonAccept.visible = true;
        this.children.add(buttonAccept);

        this.buttonReceiver = new SmallButton(this.guiLeft + 139, this.guiTop + 70, 30, 14, new StringTextComponent("OK"), p_onPress_1_ -> requestPlayerTeleport());
        this.buttonReceiver.visible = true;
        this.children.add(buttonReceiver);

        this.buttonHome = new SmallButton(this.guiLeft + 7, this.guiTop + 89, 16, 16, new StringTextComponent("H"), p_onPress_1_ -> requestHomeTeleport());
        this.buttonHome.visible = true;
        this.children.add(buttonHome);

        this.buttonSpawn = new SmallButton(this.guiLeft + 25, this.guiTop + 89, 16, 16, new StringTextComponent("S"), p_onPress_1_ -> requestSpawnTeleport());
        this.buttonSpawn.visible = true;
        this.children.add(buttonSpawn);

        this.buttonCancel = new SmallButton(this.guiLeft + 153, this.guiTop + 89, 16, 16, new StringTextComponent("X"), p_onPress_1_ -> cancelSessions());
        this.children.add(buttonCancel);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        this.renderTextFields(matrixStack, mouseX, mouseY, partialTicks);
        this.renderButtons(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderHoveredTooltip(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.renderHoveredTooltip(matrixStack, mouseX, mouseY);

        this.accept.renderToolTip(matrixStack, mouseX, mouseY);
        this.receiver.renderToolTip(matrixStack, mouseX, mouseY);
    }

    @Override
    public void onClose() {
        super.onClose();
        this.minecraft.keyboardListener.enableRepeatEvents(false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.accept.mouseClicked(mouseX, mouseY, button);
        this.receiver.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.minecraft.player.closeScreen();
        }

        if(this.accept.isFocused()) {
            if(keyCode == 13) {
                acceptPlayer();
            } else {
                this.accept.keyPressed(keyCode, scanCode, modifiers);
            }
        }

        if(this.receiver.isFocused()) {
            if(keyCode == 13) {
                requestPlayerTeleport();
            } else {
                this.receiver.keyPressed(keyCode, scanCode, modifiers);
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.font.drawText(matrixStack, new TranslationTextComponent("gui.cellphone.accept"),(float) 8, (float) 30, 4210752);
        this.font.drawText(matrixStack, new TranslationTextComponent("gui.cellphone.teleport"),(float) 8, (float) 60, 4210752);
        this.font.drawText(matrixStack, new TranslationTextComponent("container.inventory"), 8, this.ySize - 93, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX , int mouseY) {
        this.minecraft.textureManager.bindTexture(CELLPHONE_GUI);

        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.getXSize(), this.getYSize());
    }

    public void renderTextFields(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.accept.render(matrixStack, mouseX, mouseY, partialTicks);
        this.receiver.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public void renderButtons(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        buttonAccept.render(matrixStack, mouseX, mouseY, partialTicks);
        buttonReceiver.render(matrixStack, mouseX, mouseY, partialTicks);
        buttonHome.render(matrixStack, mouseX, mouseY, partialTicks);
        buttonSpawn.render(matrixStack, mouseX, mouseY, partialTicks);
        buttonCancel.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    protected void acceptPlayer() {
        if (!this.accept.getText().equals("")) {
            this.minecraft.player.closeScreen();
            PacketHandler.INSTANCE.sendToServer(new MessageCellphoneAuthorize(this.minecraft.player.getUniqueID().toString(), this.accept.getText()));
        }
    }

    protected void cancelSessions() {
        this.minecraft.player.closeScreen();
        PacketHandler.INSTANCE.sendToServer(new MessageCellphoneCancel(this.minecraft.player.getUniqueID().toString()));
    }

    protected void requestPlayerTeleport() {
        if(!this.receiver.getText().equals("") && this.hasEnoughFuel()) {
            this.minecraft.player.closeScreen();
            //PacketHandler.INSTANCE.sendToServer(new MessageCellphonePlayer(this.minecraft.player.getUniqueID().toString(), this.receiver.getText()));
        }
    }

    protected void requestSpawnTeleport() {
        if (this.hasEnoughFuel()) {
            this.minecraft.player.closeScreen();
            PacketHandler.INSTANCE.sendToServer(new MessageCellphoneSpawn(this.minecraft.player.getUniqueID().toString()));
        }
    }

    protected void requestHomeTeleport() {
        if (this.hasEnoughFuel()) {
            this.minecraft.player.closeScreen();
            PacketHandler.INSTANCE.sendToServer(new MessageCellphoneHome(this.minecraft.player.getUniqueID().toString()));
        }
    }

    abstract boolean hasEnoughFuel();
}
