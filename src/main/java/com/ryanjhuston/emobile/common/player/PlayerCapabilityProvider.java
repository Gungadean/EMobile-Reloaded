package com.ryanjhuston.emobile.common.player;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerCapabilityProvider implements ICapabilitySerializable<INBT> {

    private final Direction NO_SPECIFIC_SIDE = null;

    private PlayerTeleportData playerTeleportData = new PlayerTeleportData();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(CapabilityTeleportData.CAPABILITY_PLAYER_TELEPORT == cap) {
            return (LazyOptional<T>) LazyOptional.of(() -> playerTeleportData);
        }
        return LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("emobile", CapabilityTeleportData.CAPABILITY_PLAYER_TELEPORT.writeNBT(playerTeleportData, null));
        return nbt;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CapabilityTeleportData.CAPABILITY_PLAYER_TELEPORT.readNBT(playerTeleportData, null, nbt);
    }
}
