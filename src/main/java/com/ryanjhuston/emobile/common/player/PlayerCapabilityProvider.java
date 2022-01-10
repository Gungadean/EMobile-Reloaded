package com.ryanjhuston.emobile.common.player;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerCapabilityProvider implements ICapabilitySerializable<INBT> {

    private PlayerTeleportDataHandler playerTeleportData = new PlayerTeleportDataHandler();

    @CapabilityInject(PlayerTeleportDataHandler.class)
    public static Capability<PlayerTeleportDataHandler> CAPABILITY_PLAYER_TELEPORT = null;

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(CAPABILITY_PLAYER_TELEPORT == cap) {
            return (LazyOptional<T>) LazyOptional.of(() -> playerTeleportData);
        }
        return LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        if(CAPABILITY_PLAYER_TELEPORT != null) {
            return CAPABILITY_PLAYER_TELEPORT.writeNBT(playerTeleportData, null);
        }
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        if(CAPABILITY_PLAYER_TELEPORT != null) {
            CAPABILITY_PLAYER_TELEPORT.readNBT(playerTeleportData, null, nbt);
        }
    }
}
