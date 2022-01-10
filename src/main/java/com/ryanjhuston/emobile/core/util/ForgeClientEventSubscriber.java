package com.ryanjhuston.emobile.core.util;

import com.ryanjhuston.emobile.EMobileMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EMobileMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeClientEventSubscriber {
}
