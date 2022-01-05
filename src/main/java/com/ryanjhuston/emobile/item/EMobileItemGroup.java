package com.ryanjhuston.emobile.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class EMobileItemGroup {

    public static final ItemGroup EMOBILE_GROUP = new ItemGroup("emobileItemTab")
    {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(EMobileItems.CELLPHONE.get());
        }
    };
}
