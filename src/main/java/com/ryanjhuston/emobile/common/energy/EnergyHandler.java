package com.ryanjhuston.emobile.common.energy;

import com.ryanjhuston.emobile.config.EMConfig;
import com.ryanjhuston.emobile.item.CellphoneFEItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyHandler implements IEnergyStorage, ICapabilityProvider {

    private final LazyOptional<IEnergyStorage> storage = LazyOptional.of(() -> this);
    private final ItemStack item;

    public EnergyHandler(ItemStack item) {
        this.item = item;
    }

    public void setEnergyStored(int energy) {
        CellphoneFEItem.setStoredEnergy(item, energy);
    }

    public boolean useEnergy(int energyUse) {
        if(this.getEnergyStored() < energyUse) {
            return false;
        } else {
            setEnergyStored(this.getEnergyStored() - energyUse);
            return true;
        }
    }

    public void addEnergy(int energyAdd) {
        if(this.getEnergyStored() + energyAdd > this.getMaxEnergyStored()) {
            this.setEnergyStored(this.getMaxEnergyStored());
        } else {
            this.setEnergyStored(this.getEnergyStored() + energyAdd);
        }
    }

    @Override
    public int receiveEnergy(int receive, boolean simulate) {
        if (!canReceive()){
            return 0;
        }

        int stored = this.getEnergyStored();
        int energyReceived = Math.min(this.getMaxEnergyStored() - stored, Math.min(EMConfig.feCellphoneMaxInput.get(), receive));
        if (!simulate){
            this.setEnergyStored(stored + energyReceived);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int extract, boolean simulate) {
        if (!canExtract()){
            return 0;
        }

        int stored = this.getEnergyStored();
        int energyExtracted = Math.min(stored, Math.min(EMConfig.feCellphoneMaxInput.get(), extract));
        if (!simulate){
            this.setEnergyStored(stored - energyExtracted);
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return CellphoneFEItem.getStoredEnergy(item);
    }

    @Override
    public int getMaxEnergyStored() {
        return EMConfig.feCellphoneMaxEnergy.get();
    }

    @Override
    public boolean canExtract() {
        return this.getEnergyStored() > 0;
    }

    public boolean hasEnoughFuel() {
        return this.getEnergyStored() < this.getMaxEnergyStored();
    }

    @Override
    public boolean canReceive() {
        return this.getEnergyStored() < this.getMaxEnergyStored();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityEnergy.ENERGY.orEmpty(cap, storage);
    }
}
