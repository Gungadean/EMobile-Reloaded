package com.ryanjhuston.emobile.item;

import com.ryanjhuston.emobile.EMobileMod;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EMobileItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, EMobileMod.MODID);

    public static final RegistryObject<Item> CELLPHONE = ITEMS.register("cellphone",
            () -> new CellphonePearlItem(new Item.Properties()
                    .maxStackSize(1).group(EMobileItemGroup.EMOBILE_GROUP)));

    public static final RegistryObject<Item> CELLPHONE_RF = ITEMS.register("cellphone.rf",
            () -> new CellphoneFEItem(new Item.Properties()
                    .maxStackSize(1).group(EMobileItemGroup.EMOBILE_GROUP)));

    public static void registerItems(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
