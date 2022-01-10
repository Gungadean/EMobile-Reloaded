package com.ryanjhuston.emobile.common.inventory;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemCapabilityProvider implements ICapabilitySerializable<INBT> {

    private CellphoneItemStackHandler cellphoneItemStackHandler;
    private final LazyOptional<IItemHandler> lazyInitSupplier = LazyOptional.of(this::getCachedInventory);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == cap) {
            return (LazyOptional<T>) (lazyInitSupplier);
        }

        return LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(getCachedInventory(), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(getCachedInventory(), null, nbt);
    }

    private CellphoneItemStackHandler getCachedInventory() {
        if (cellphoneItemStackHandler == null) {
            cellphoneItemStackHandler = new CellphoneItemStackHandler(1);
        }
        return cellphoneItemStackHandler;
    }
}
