package com.ryanjhuston.emobile.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraftforge.common.ForgeConfigSpec.*;

public class EMConfig {


    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static List<ConfigSection> configSections = new ArrayList<>();

    public static final ConfigSection sectionGeneral = new ConfigSection("General Settings", "general");
    public static final ConfigSection sectionTweaks = new ConfigSection("Tweaks Settings", "tweaks");
    public static final ConfigSection sectionFECellphone = new ConfigSection("Forge Energy Cellphone Settings", "feCellphone");

    // general default
    private static final int teleportDuration_default = 8;
    private static final boolean allowTeleportPlayers_default = true;
    private static final boolean allowTeleportHome_default = true;
    private static final boolean allowTeleportSpawn_default = true;
    private static final String[] worldBlacklist_default = new String[0];
    private static final boolean worldWhitelist_default = false;
    private static final String[] bedBlocks_default = new String[] { "com.carpentersblocks.block.BlockCarpentersBed" };

    // tweaks default
    private static final int enderPearlStackSize_default = 64;

    // feCellphone default
    private static final int feCellphoneMaxEnergy_default = 600000;
    private static final int feCellphoneMaxInput_default = 2000;
    private static final int feCellphoneEnergyPerUse_default = 30000;

    // client synced settings
    public static boolean client_allowTeleportPlayers = true;
    public static boolean client_allowTeleportHome = true;
    public static boolean client_allowTeleportSpawn = true;
    public static int client_feCellphoneMaxEnergy = 600000;
    public static int client_feCellphoneMaxInput = 2000;
    public static int client_feCellphoneEnergyPerUse = 30000;

    // general
    public static ConfigValue<Integer> teleportDuration;
    public static ConfigValue<Boolean> allowTeleportPlayers;
    public static ConfigValue<Boolean> allowTeleportHome;
    public static ConfigValue<Boolean> allowTeleportSpawn;
    public static ConfigValue<List<String>> worldBlacklist;
    public static ConfigValue<Boolean> worldWhitelist;
    public static ConfigValue<List<String>> bedBlocks;

    // tweaks
    public static ConfigValue<Integer> enderPearlStackSize;

    // feCellphone
    public static ConfigValue<Integer> feCellphoneMaxEnergy;
    public static ConfigValue<Integer> feCellphoneMaxInput;
    public static ConfigValue<Integer> feCellphoneEnergyPerUse;

    static {
        BUILDER.push(sectionGeneral.name);

        teleportDuration = BUILDER.comment("Sets time it takes to teleport when using Ender Cellphones.")
                .defineInRange("Teleport-Duration", teleportDuration_default, 0, 60);
        allowTeleportPlayers = BUILDER.comment("When enabled, the Ender Cellphone may be used to teleport to other players.")
                .define("Allow-Player-Teleport", allowTeleportPlayers_default);
        allowTeleportHome = BUILDER.comment("When enabled, the Ender Cellphone may be used by players to teleport to their beds.")
                .define("Allow-Home-Teleport", allowTeleportHome_default);
        allowTeleportSpawn = BUILDER.comment("When enabled, the Ender Cellphone may be used to teleport to the world spawn.")
                .define("Allow-Spawn-Teleport", allowTeleportSpawn_default);
        worldBlacklist = BUILDER.comment("The blacklist of world names that can be teleported to or from using the Ender Cellphone. These worlds may not be teleported to or from.")
                .define("World-Blacklist", Arrays.asList(worldBlacklist_default));
        worldWhitelist = BUILDER.comment("If enabled, the blacklist of world names will be treated as a whitelist instead. The worlds will then be the only worlds that may be teleported to or from.")
                .define("World-Whitelist", worldWhitelist_default);
        bedBlocks = BUILDER.comment("A list of full class names of Blocks that count as beds. Use this to add support for beds from other mods.")
                .define("Bed-Blocks", Arrays.asList(bedBlocks_default));

        BUILDER.pop();
        BUILDER.push(sectionTweaks.name);

        enderPearlStackSize = BUILDER.comment("This config option can be used to change the maximum stack size of Ender Pearls.")
                .defineInRange("EnderPearl-Stack-Size", enderPearlStackSize_default, 1, 512);

        BUILDER.pop();
        BUILDER.push(sectionFECellphone.name);

        feCellphoneMaxEnergy = BUILDER.comment("The maximum amount of FE that a Forge Energy Cellphone can store.")
                .define("FECellphone-MaxEnergy", feCellphoneMaxEnergy_default);
        feCellphoneMaxInput = BUILDER.comment("The maximum FE/t rate that the Forge Energy Cellphone can be charged with.")
                .define("FECellphone-MaxInput", feCellphoneMaxInput_default);
        feCellphoneEnergyPerUse = BUILDER.comment("The amount of FE that the Forge Energy Cellphone consumes when teleporting.")
                .define("FECellphone-EnergyPerUse", feCellphoneEnergyPerUse_default);

        BUILDER.pop();
        SPEC = BUILDER.build();
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
