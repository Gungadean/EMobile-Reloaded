package com.ryanjhuston.emobile.common.inventory;

import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class CellphoneItemStackHandler extends ItemStackHandler {

    private boolean isDirty = true;

    public CellphoneItemStackHandler(int slotNumber) {
        super(slotNumber);
    }

    public boolean isDirty() {
        boolean currentState = isDirty;
        isDirty = false;
        return currentState;
    }

    @Override
    protected void onContentsChanged(int slot) {
        isDirty = true;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack item) {
        return item != null && item.getItem() instanceof EnderPearlItem;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack item, boolean simulate) {
        if(!isItemValid(slot, item)) {
            return item;
        }

        return super.insertItem(slot, item, simulate);
    }

    public int getStoredPearls() {
        ItemStack pearls = this.getStackInSlot(0);
        return pearls != null ? pearls.getCount() : 0;
    }

    public boolean useFuel() {
        if (this.getStoredPearls() <= 0) {
            return false;
        }

        this.getStackInSlot(0).setCount(this.getStackInSlot(0).getCount()-1);
        isDirty = true;
        return true;
    }
}
