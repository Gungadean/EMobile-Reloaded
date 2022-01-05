package com.ryanjhuston.emobile.core.init;

import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.common.container.CellphoneItemContainer;
import com.ryanjhuston.emobile.common.container.CellphoneRFContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerTypesInit {

    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, EMobileMod.MODID);

    public static final RegistryObject<ContainerType<CellphoneItemContainer>> CELLPHONE_ITEM_CONTAINER_TYPE =
            CONTAINER_TYPES.register("cellphone_pearl", () -> IForgeContainerType.create(CellphoneItemContainer::createContainerClientSide));

    public static final RegistryObject<ContainerType<CellphoneRFContainer>> CELLPHONE_RF_CONTAINER_TYPE =
            CONTAINER_TYPES.register("cellphone_rf", () -> IForgeContainerType.create(CellphoneRFContainer::createContainerClientSide));
}