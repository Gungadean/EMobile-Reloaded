package com.ryanjhuston.emobile.session;

import com.ryanjhuston.emobile.util.ServerUtils;
import com.ryanjhuston.emobile.util.TeleportUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class CellphoneSessionLocation extends CellphoneSessionBase{

    protected ServerPlayerEntity player;
    protected String unlocalizedLocation;
    protected World world;
    protected int posX;
    protected int posY;
    protected int posZ;

    public CellphoneSessionLocation(int duration, String unlocalizedLocation, ServerPlayerEntity player, World world, int posX, int posY, int posZ) {
        super(player.getUniqueID().toString(), duration);
        this.unlocalizedLocation = unlocalizedLocation;
        this.player = player;
        this.world = world;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;

        ServerUtils.sendChatToPlayer(player.getUniqueID(), new TranslationTextComponent("chat.cellphone.start.location").setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.GOLD))));
    }

    @Override
    public void tick() {
        if (!ServerUtils.canPlayerTeleport(this.player)) {
            this.invalidate();
            return;
        } else if (!TeleportUtils.isWorldTeleportAllowed(this.player.getServerWorld(), this.world)) {
            //this.player.worldObj.provider.getDimensionName(), this.player.mcServer.worldServerForDimension(this.dimension).provider.getDimensionName()
            //String msg = String.format(StringUtils.translate("chat.cellphone.cancel.dimension"), this.player.worldObj.provider.getDimensionName(), this.player.mcServer.worldServerForDimension(this.dimension).provider.getDimensionName());
            player.sendMessage(ServerUtils.getTranslationTextComponent("chat.cellphone.cancel.dimension", TextFormatting.RED), player.getUniqueID());
            this.invalidate();
            return;
        }

        if (this.ticks % Math.max(this.countdownSecs - 2, 1) == 0) {
            ServerUtils.sendDiallingParticles(this.player);
            ServerUtils.sendDiallingParticles(this.world, this.posX, this.posY, this.posZ);
        }

        super.tick();
    }

    @Override
    public void onCountdownSecond() {
        if (this.countdownSecs <= 3 || this.countdownSecs % 2 == 0) {
            //this.countdownSecs
            this.player.sendMessage(ServerUtils.getTranslationTextComponent("chat.cellphone.countdown", TextFormatting.LIGHT_PURPLE), player.getUniqueID());
        }
    }

    @Override
    public void onCountdownFinished() {
        //this.player.worldObj.playSoundAtEntity(this.player, "mob.endermen.portal", 1.0F, 1.0F);
        ServerUtils.sendTeleportParticles(this.player);
        TeleportUtils.teleportPlayer(this.player, this.world, this.posX, this.posY, this.posZ);
        //this.player.worldObj.playSoundAtEntity(this.player, "mob.endermen.portal", 1.0F, 1.0F);
        ServerUtils.sendTeleportParticles(this.player);
        player.sendMessage(ServerUtils.getTranslationTextComponent("chat.cellphone.success.location", TextFormatting.GOLD), player.getUniqueID());
    }

    @Override
    public boolean isPlayerInSession(PlayerEntity player) {
        return this.player.equals(player);
    }

    @Override
    public void cancel(String canceledBy) {
        super.cancel(canceledBy);
        player.sendMessage(new TranslationTextComponent("chat.cellphone.cancel").setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.RED))), player.getUniqueID());
    }
}
