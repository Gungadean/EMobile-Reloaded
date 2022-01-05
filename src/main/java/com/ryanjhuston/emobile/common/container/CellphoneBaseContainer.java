package com.ryanjhuston.emobile.common.container;

import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.common.inventory.CellphoneItemStackHandler;
import com.ryanjhuston.emobile.core.init.ContainerTypesInit;
import com.ryanjhuston.emobile.item.CellphoneBaseItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class CellphoneBaseContainer extends Container {

    protected CellphoneItemStackHandler itemStackHandler = null;
    protected ItemStack cellphone = null;

    int xOffset = 8;
    int yOffset = 121;


    public static CellphoneBaseContainer createContainerClientSide(int windowId, PlayerInventory playerInventory, PacketBuffer data) {
        try {
            CellphoneItemStackHandler itemStackHandler = new CellphoneItemStackHandler(1);

            return new CellphoneBaseContainer(ContainerTypesInit.CELLPHONE_ITEM_CONTAINER_TYPE.get(), windowId);
        } catch (IllegalArgumentException e) {
            EMobileMod.LOGGER.warn(e);
        }
        return null;
    }

    public CellphoneBaseContainer(ContainerType<?> containerType, final int windowId) {
        super(containerType, windowId);
    }

    protected void createInventory(PlayerInventory playerInventory) {
        //Player Inventory
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, xOffset + j * 18, yOffset + i * 18));
            }
        }

        //Player Hotbar
        for(int i = 0; i < 9; i++) {
            if(i == playerInventory.currentItem) {
                this.addSlot(new SlotLocked(playerInventory, i, xOffset + i * 18, yOffset + 58));
            } else {
                this.addSlot(new Slot(playerInventory, i, xOffset + i * 18, yOffset + 58));
            }
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return player.getHeldItemMainhand().getItem() instanceof CellphoneBaseItem;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack item = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack()) {
            ItemStack currentItem = slot.getStack();
            item = currentItem.copy();
            if(index < 1 && !this.mergeItemStack(currentItem, 1, this.inventorySlots.size(), false)) {
                return ItemStack.EMPTY;
            }

            if(!this.mergeItemStack(currentItem, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if(currentItem.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();;
            }
        }

        return item;
    }
}
