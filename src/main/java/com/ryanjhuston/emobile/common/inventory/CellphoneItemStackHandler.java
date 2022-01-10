package com.ryanjhuston.emobile.common.inventory;

import com.ryanjhuston.emobile.config.EMConfig;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.ItemHandlerHelper;
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
        return EMConfig.enderPearlStackSize.get();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack item, boolean simulate) {
        if(!isItemValid(slot, item)) {
            return item;
        }

        if (item.isEmpty()) {
            return ItemStack.EMPTY;
        }

        validateSlotIndex(slot);

        ItemStack existing = this.stacks.get(slot);

        int limit = this.getSlotLimit(slot);

        if (!existing.isEmpty())
        {
            if (!ItemHandlerHelper.canItemStacksStack(item, existing))
                return item;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return item;

        boolean reachedLimit = item.getCount() > limit;

        if (!simulate)
        {
            if (existing.isEmpty())
            {
                this.stacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(item, limit) : item);
            }
            else
            {
                existing.grow(reachedLimit ? limit : item.getCount());
            }
            onContentsChanged(slot);
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(item, item.getCount()- limit) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.EMPTY;

        validateSlotIndex(slot);

        ItemStack existing = this.stacks.get(slot);

        if (existing.isEmpty())
            return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract)
        {
            if (!simulate)
            {
                this.stacks.set(slot, ItemStack.EMPTY);
                onContentsChanged(slot);
                return existing;
            }
            else
            {
                return existing.copy();
            }
        }
        else
        {
            if (!simulate)
            {
                this.stacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                onContentsChanged(slot);
            }

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
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

    public void refundFuel() {
        if (this.getStoredPearls() != 0) {
            this.getStackInSlot(0).setCount(this.getStackInSlot(0).getCount()+1);
        } else {
            this.setStackInSlot(0, new ItemStack(Items.ENDER_PEARL, 1));
        }
        isDirty = true;
    }
}
