package com.ryanjhuston.emobile.config;

import com.ryanjhuston.emobile.EMobileMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = EMobileMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EMConfig {

    public static ForgeConfigSpec config;
    public static List<ConfigSection> configSections = new ArrayList<ConfigSection>();

    public static final ConfigSection sectionGeneral = new ConfigSection("General Settings", "general");
    public static final ConfigSection sectionTweaks = new ConfigSection("Tweaks Settings", "tweaks");
    public static final ConfigSection sectionFluxCellphone = new ConfigSection("Fluxed Ender Cellphone Settings", "fluxCellphone");

    // general default
    public static final boolean allowTeleportPlayers_default = true;
    public static final boolean allowTeleportHome_default = true;
    public static final boolean allowTeleportSpawn_default = true;
    public static final String[] dimensionsBlacklist_default = new String[0];
    public static final boolean dimensionsWhitelist_default = false;
    public static final String[] bedBlocks_default = new String[] { "com.carpentersblocks.block.BlockCarpentersBed" };

    // tweaks default
    public static final int enderPearlStackSize_default = 16;

    // fluxCellphone default
    public static final boolean fluxCellphoneEnabled_default = true;
    public static final int fluxCellphoneMaxEnergy_default = 600000;
    public static final int fluxCellphoneMaxInput_default = 2000;
    public static final int fluxCellphoneEnergyPerUse_default = 30000;

    // general
    public static boolean allowTeleportPlayers = allowTeleportHome_default;
    public static boolean allowTeleportHome = allowTeleportHome_default;
    public static boolean allowTeleportSpawn = allowTeleportHome_default;
    public static String[] dimensionsBlacklist = dimensionsBlacklist_default;
    public static boolean dimensionsWhitelist = dimensionsWhitelist_default;
    public static List<String> bedBlocks = Arrays.asList(bedBlocks_default);

    // tweaks
    public static int enderPearlStackSize = enderPearlStackSize_default;

    // fluxCellphone
    public static boolean fluxCellphoneEnabled = fluxCellphoneEnabled_default;
    public static int fluxCellphoneMaxEnergy = fluxCellphoneMaxEnergy_default;
    public static int fluxCellphoneMaxInput = fluxCellphoneMaxInput_default;
    public static int fluxCellphoneEnergyPerUse = fluxCellphoneEnergyPerUse_default;

    public static class Common {

    }

    public static void register(final ModLoadingContext context) {
        context.registerConfig(ModConfig.Type.COMMON, config);
    }

    /*public static void syncConfig() {
        try {
            processConfig();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            config.save();
        }
        if (EMobile.cellphoneRF != null) {
            EMobile.cellphoneRF.maxEnergy = fluxCellphoneMaxEnergy;
            EMobile.cellphoneRF.maxInput = fluxCellphoneMaxInput;
            EMobile.cellphoneRF.energyPerUse = fluxCellphoneEnergyPerUse;
        }
    }

    public static void processConfig() {
        ForgeConfigSpec.Builder builder;
        builder.comment().translation().define();
        allowTeleportPlayers = config.get(sectionGeneral.name, "Allow teleporting to players", allowTeleportPlayers_default, "When enabled, the Ender Cellphone may be used to teleport to other players.").getBoolean(allowTeleportPlayers_default);
        allowTeleportHome = config.get(sectionGeneral.name, "Allow teleporting home", allowTeleportHome_default, "When enabled, the Ender Cellphone may be used by players to teleport to their beds.").getBoolean(allowTeleportHome_default);
        allowTeleportSpawn = config.get(sectionGeneral.name, "Allow teleporting to spawn", allowTeleportSpawn_default, "When enabled, the Ender Cellphone may be used to teleport to the world spawn.").getBoolean(allowTeleportSpawn_default);
        dimensionsBlacklist = config.get(sectionGeneral.name, "Dimensions Blacklist", dimensionsBlacklist_default, "The blacklist of dimension ids that can be teleported to or from using the Ender Cellphone. These dimensions may not be teleported to or from.").getIntList();
        dimensionsWhitelist = config.get(sectionGeneral.name, "Dimensions Whitelist", dimensionsWhitelist_default, "If enabled, the blacklist of dimension ids will be treated as a whitelist instead. The dimensions will then be the only dimensions that may be teleported to or from.").getBoolean(dimensionsWhitelist_default);
        bedBlocks = Arrays.asList(config.get(sectionGeneral.name, "Bed Blocks", bedBlocks_default, "A list of full class names of Blocks that count as beds. Use this to add support for beds from mods.").getStringList());

        enderPearlStackSize = config.get(sectionTweaks.name, "Ender Pearl stack size", enderPearlStackSize_default, "This config option can be used to change the maximum stack size of Ender Pearls.").setMinValue(1).setMaxValue(512).setRequiresMcRestart(true).getInt(enderPearlStackSize_default);

        fluxCellphoneEnabled = config.get(sectionFluxCellphone.name, "Enabled", fluxCellphoneEnabled_default, "Whether the Fluxed Ender Cellphone is enabled at all.").setRequiresMcRestart(true).getBoolean();
        fluxCellphoneMaxEnergy = config.get(sectionFluxCellphone.name, "Max Energy", fluxCellphoneMaxEnergy_default, "The maximum amount of RF that a Fluxed Ender Cellphone can store.").setMinValue(1).getInt(fluxCellphoneMaxEnergy_default);
        fluxCellphoneMaxInput = config.get(sectionFluxCellphone.name, "Max Input", fluxCellphoneMaxInput_default, "The maximum RF/t rate that the Fluxed Ender Cellphone can be charged with.").setMinValue(0).getInt(fluxCellphoneMaxInput_default);
        fluxCellphoneEnergyPerUse = config.get(sectionFluxCellphone.name, "Energy Per Use", fluxCellphoneEnergyPerUse_default, "The amount of RF that the Fluxed Ender Cellphone consumes when teleporting.").setMinValue(0).getInt(fluxCellphoneEnergyPerUse_default);
    }*/

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent event) {
        if(event.getConfig().getSpec() == config) {
            //processConfig();
        }
    }

    public static class ConfigSection {
        public String name;
        public String id;

        public ConfigSection(String name, String id) {
            this.name = name;
            this.id = id;
            EMConfig.configSections.add(this);
        }

        public String toLowerCase() {
            return this.name.toLowerCase();
        }
    }
}
