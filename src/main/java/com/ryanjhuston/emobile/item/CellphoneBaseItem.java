package com.ryanjhuston.emobile.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class CellphoneBaseItem extends Item {

    public CellphoneBaseItem(Properties properties) {
        super(properties);
    }

    public abstract boolean useFuel(ItemStack item);
}
