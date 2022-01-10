package com.ryanjhuston.emobile.client.audio;

import com.ryanjhuston.emobile.EMobileMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundRegister {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, EMobileMod.MODID);

    public static final RegistryObject<SoundEvent> DIAL_TONE =
            registerSoundEvent("dial_tone");

    public static final RegistryObject<SoundEvent> PHONE_COUNTDOWN =
            registerSoundEvent("phone_countdown");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(EMobileMod.MODID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
