package com.sonicether.soundphysics.config;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import static java.util.Map.entry;
import java.util.function.Consumer;
import java.lang.String;

import static com.sonicether.soundphysics.config.ConfigChanger.changeConfig;

@SuppressWarnings({"unused", "RedundantTypeArguments"})
public enum ConfigPresets {
    // !!! Press ctrl+shift+numpad_'-' to collapse all and enjoy !!!
    LOAD_SUCCESS("Choose", null),
    //<editor-fold desc="DEFAULT_BALANCED,">
    DEFAULT_BALANCED("Balanced (Base)", (SoundPhysicsConfig c) -> changeConfig(c, true,

            1.0, 1.0, 1.0, 1.0,
            1.0, 4.0, 1.0, 0.8,

            true, 224, 12, false,

            Map.ofEntries(entry("DEFAULT", new MaterialData(0.5, 1))),

            0.25, 10.0, true, true, 0.0, false
    )),
    //</editor-fold>
    //<editor-fold desc="DEFAULT_PERFORMANCE,">
    DEFAULT_PERFORMANCE("Performance (Base)", (SoundPhysicsConfig c) -> changeConfig(c, true,

            1.0, 1.0, 1.0, 1.0,
            1.0, 4.0, 1.0, 0.8,

            true, 96, 6, true,

            Map.ofEntries(entry("DEFAULT", new MaterialData(0.5, 1))),

            0.25, 10.0, true, true, 0.0, true
    )),
    //</editor-fold>
    //<editor-fold desc="DEFAULT_QUALITY,">
    DEFAULT_QUALITY("Quality (Base)", (SoundPhysicsConfig c) -> changeConfig(c, true,

            1.0, 1.0, 1.0, 1.0,
            1.0, 4.0, 1.0, 0.8,

            false, 512, 24, false,

            Map.ofEntries(entry("DEFAULT", new MaterialData(0.5, 1))),

            0.25, 10.0, true, true, 0.0, false
    )),
    //</editor-fold>
    //<editor-fold desc="THEDOCRUBY,">
    THEDOCRUBY("Dr. Rubisco's Signature Sound", (SoundPhysicsConfig c) -> changeConfig(c, true,

            1.0, 0.8, 1.0, 0.8,
            3.0, 3.0, 1.0, 0.8,

            false, 256, 16, false,

            //<editor-fold desc="Material data Map,">

            Map.<String, MaterialData>ofEntries(
                    entry("field_11528", new MaterialData(0.16666666666666666, -1)),    //coral_block
                    entry("field_27199", new MaterialData(0.16666666666666666, -1)),    //small_amethyst_bud
                    entry("field_27198", new MaterialData(0.16666666666666666, -1)),    //amethyst_cluster
                    entry("field_11529", new MaterialData(0.39999999999999997, -1)),    //gravel
                    entry("field_27197", new MaterialData(0.16666666666666666, -1)),    //amethyst_block
                    entry("field_11526", new MaterialData(0.09999999999999999, -1)),    //sand
                    entry("field_27196", new MaterialData(0.03333333333333333, -1)),    //candle
                    entry("field_22140", new MaterialData(0.06666666666666667, -1)),    //weeping_vines
                    entry("field_24121", new MaterialData(0.6, -1)),    //gilded_blackstone
                    entry("field_22141", new MaterialData(0.03333333333333333, -1)),    //soul_sand
                    entry("field_24120", new MaterialData(0.5333333333333333, -1)),    //nether_gold_ore
                    entry("field_22142", new MaterialData(0.13333333333333333, -1)),    //soul_soil
                    entry("field_22143", new MaterialData(0.5333333333333333, -1)),    //basalt
                    entry("field_22144", new MaterialData(0.03333333333333333, -1)),    //wart_block
                    entry("field_22145", new MaterialData(0.11666666666666665, -1)),   //netherrack
                    entry("field_22146", new MaterialData(0.3666666666666667, -1)),    //nether_bricks
                    entry("field_22147", new MaterialData(0.03333333333333333, -1)),    //nether_sprouts
                    entry("field_22148", new MaterialData(0.39999999999999997, -1)),    //nether_ore
                    entry("field_21214", new MaterialData(0.0033333333333333335, -1)),   //honey_block
                    entry("field_22149", new MaterialData(0.3, -1)),    //bone_block
                    entry("field_17581", new MaterialData(0.03333333333333333, -1)),    //nether_wart
                    entry("field_17580", new MaterialData(0.06666666666666667, -1)),    //crop
                    entry("field_16498", new MaterialData(0.01, -1)),   //scaffolding
                    entry("field_11535", new MaterialData(0.19999999999999998, -1)),    //grass
                    entry("field_11533", new MaterialData(0.7999999999999999, -1)),    //metal
                    entry("field_11534", new MaterialData(0.049999999999999996, -1)),   //wet_grass
                    entry("field_11537", new MaterialData(0.39999999999999997, -1)),    //glass
                    entry("field_11538", new MaterialData(0.03333333333333333, -1)),    //bamboo_sapling
                    entry("field_28116", new MaterialData(0.09999999999999999, -1)),    //sculk_sensor
                    entry("field_23265", new MaterialData(0.3333333333333333, -1)),    //lodestone
                    entry("field_22138", new MaterialData(0.09999999999999999, -1)),    //roots (nether grass)
                    entry("field_22139", new MaterialData(0.19999999999999998, -1)),    //shroomlight
                    entry("field_24119", new MaterialData(0.06666666666666667, -1)),    //chain
                    entry("field_11531", new MaterialData(0.7000000000000001, -1)),    //anvil
                    entry("field_11532", new MaterialData(0.09999999999999999, -1)),    //ladder
                    entry("field_29033", new MaterialData(0.6333333333333333, -1)),    //deepslate
                    entry("field_29034", new MaterialData(0.6666666666666666, -1)),    //deepslate_bricks
                    entry("field_11547", new MaterialData(0.06666666666666667, -1)),    //wood (stem/wood?)
                    entry("field_29035", new MaterialData(0.6666666666666666, -1)),    //deepslate_tiles
                    entry("field_11544", new MaterialData(0.5666666666666667, -1)),    //stone
                    entry("field_28061", new MaterialData(0.5, -1)),    //pointed_dripstone
                    entry("field_11545", new MaterialData(0.0033333333333333335, -1)),   //slime_block
                    entry("field_29036", new MaterialData(0.7000000000000001, -1)),    //polished_deepslate
                    entry("field_11548", new MaterialData(0.006666666666666667, -1)),   //snow
                    entry("field_28427", new MaterialData(0.09999999999999999, -1)),    //grass
                    entry("field_28702", new MaterialData(0.09999999999999999, -1)),    //azalea_leaves
                    entry("field_28701", new MaterialData(0.09999999999999999, -1)),    //hanging_roots
                    entry("field_28700", new MaterialData(0.26666666666666666, -1)),    //rooted_dirt
                    entry("field_11542", new MaterialData(0.09999999999999999, -1)),    //bamboo
                    entry("field_18852", new MaterialData(0.26666666666666666, -1)),    //wood (stem/wood?)
                    entry("field_11543", new MaterialData(0.0033333333333333335, -1)),   //wool
                    entry("field_28695", new MaterialData(0.09999999999999999, -1)),    //flowering_azalea
                    entry("field_23083", new MaterialData(0.09999999999999999, -1)),    //vine
                    entry("field_23082", new MaterialData(0.09999999999999999, -1)),    //weeping_vines
                    entry("field_28694", new MaterialData(0.06666666666666667, -1)),    //azalea
                    entry("field_28693", new MaterialData(0.09999999999999999, -1)),    //spore_blossom
                    entry("field_25183", new MaterialData(0.09999999999999999, -1)),    //grass
                    entry("field_28692", new MaterialData(0.09999999999999999, -1)),    //cave_vines
                    entry("field_22150", new MaterialData(0.5, -1)),    //netherite_block
                    entry("field_27203", new MaterialData(0.4666666666666666, -1)),    //calcite
                    entry("field_22151", new MaterialData(0.5333333333333333, -1)),    //ancient_debris
                    entry("field_28699", new MaterialData(0.09999999999999999, -1)),    //small_dripleaf
                    entry("field_22152", new MaterialData(0.19999999999999998, -1)),    //stem (nether)
                    entry("field_27884", new MaterialData(0.0033333333333333335, -1)),   //powder_snow
                    entry("field_27202", new MaterialData(0.43333333333333335, -1)),    //tuff
                    entry("field_28698", new MaterialData(0.13333333333333333, -1)),    //big_dripleaf
                    entry("field_28697", new MaterialData(0.03333333333333333, -1)),    //moss
                    entry("field_22153", new MaterialData(0.19999999999999998, -1)),    //nylium
                    entry("field_27201", new MaterialData(0.09999999999999999, -1)),    //large_amethyst_bud
                    entry("field_27200", new MaterialData(0.09999999999999999, -1)),    //medium_amethyst_bud
                    entry("field_22154", new MaterialData(0.13333333333333333, -1)),    //fungus
                    entry("field_28696", new MaterialData(0.03333333333333333, -1)),    //moss_carpet
                    entry("field_27204", new MaterialData(0.7666666666666666, -1)),    //copper
                    entry("field_17734", new MaterialData(0.06666666666666667, -1)),    //lantern
                    entry("field_17579", new MaterialData(0.09999999999999999, -1)),    //sweet_berry_bush
                    entry("field_28060", new MaterialData(0.6, -1)),    //dripstone_block
                    entry("DEFAULT", new MaterialData(0.6, 1.0))),

            //</editor-fold>

            0.25, 10.0, true, true, 0.1, false

    )),
    //</editor-fold>
    //<editor-fold desc="SUPER_REVERB,">
    SUPER_REVERB("Super Reverb", (SoundPhysicsConfig c) -> changeConfig(c, true,
            null, 1.8, null, null,
            2.0, null, null, null,

            null, null, null, null,

            null,

            null, null, null, null, null, null

    )),
    //</editor-fold>
    //<editor-fold desc="NO_ABSORPTION,">
    NO_ABSORPTION("No Absorption", (SoundPhysicsConfig c) -> changeConfig(c, true,
            null, null, null, 0.0,
            null, null, 0.0, null,

            null, null, null, null,

            null,

            0.0, 0.0, null, null, null, null
    )),
    //</editor-fold>
    //<editor-fold desc="LOW_FREQ,">
    LOW_FREQ("Bass Boost", (SoundPhysicsConfig c) -> changeConfig(c, true,
            null, null, 0.2, null,
            null, null, 2.0, null,

            null, null, null, null,

            null,

            null, null, null, null, null, null
    )),
    //</editor-fold>
    //<editor-fold desc="HIGH_FREQ,">
    HIGH_FREQ("Treble Boost", (SoundPhysicsConfig c) -> changeConfig(c, true,
            null, null, 1.8, null,
            null, null, 0.5, null,

            null, null, null, null,

            null,

            null, null, null, null, null, null
    )),
    //</editor-fold>
    //<editor-fold desc="FOG,">
    FOG("Foggy Air", (SoundPhysicsConfig c) -> changeConfig(c, true,
            2.5, null, null, null,
            null, null, 25.0, null,

            null, null, null, null,

            null,

            null, null, null, null, null, null
    )),
    //</editor-fold>
    //<editor-fold desc="TOTAL_OCCLUSION,">
    TOTAL_OCCLUSION("Total Occlusion", (SoundPhysicsConfig c) -> changeConfig(c, true,

            null, null, null, 10.0,
            null, null, null, null,

            null, null, null, null,

            null,

            null, 10.0, null, null, null, null
    )),
    //</editor-fold>
    //<editor-fold desc="RESET_MATERIALS;">
    RESET_MATERIALS("Reset Material Properties", (SoundPhysicsConfig c) -> changeConfig(c, true,

            null, null, null, null,
            null, null, null, null,

            null, null, null, null,

            //<editor-fold desc="Material data Map,">

            Map.<String, MaterialData>ofEntries(
                    entry("field_11528", new MaterialData(0.16666666666666666, 1)),    //coral_block
                    entry("field_27199", new MaterialData(0.16666666666666666, 1)),    //small_amethyst_bud
                    entry("field_27198", new MaterialData(0.16666666666666666, 1)),    //amethyst_cluster
                    entry("field_11529", new MaterialData(0.39999999999999997, 1)),    //gravel
                    entry("field_27197", new MaterialData(0.16666666666666666, 1)),    //amethyst_block
                    entry("field_11526", new MaterialData(0.09999999999999999, 1)),    //sand
                    entry("field_27196", new MaterialData(0.03333333333333333, 1)),    //candle
                    entry("field_22140", new MaterialData(0.06666666666666667, 1)),    //weeping_vines
                    entry("field_24121", new MaterialData(0.6, 1)),    //gilded_blackstone
                    entry("field_22141", new MaterialData(0.03333333333333333, 1)),    //soul_sand
                    entry("field_24120", new MaterialData(0.5333333333333333, 1)),    //nether_gold_ore
                    entry("field_22142", new MaterialData(0.13333333333333333, 1)),    //soul_soil
                    entry("field_22143", new MaterialData(0.5333333333333333, 1)),    //basalt
                    entry("field_22144", new MaterialData(0.03333333333333333, 1)),    //wart_block
                    entry("field_22145", new MaterialData(0.11666666666666665, 1)),   //netherrack
                    entry("field_22146", new MaterialData(0.3666666666666667, 1)),    //nether_bricks
                    entry("field_22147", new MaterialData(0.03333333333333333, 1)),    //nether_sprouts
                    entry("field_22148", new MaterialData(0.39999999999999997, 1)),    //nether_ore
                    entry("field_21214", new MaterialData(0.0033333333333333335, 1)),   //honey_block
                    entry("field_22149", new MaterialData(0.3, 1)),    //bone_block
                    entry("field_17581", new MaterialData(0.03333333333333333, 1)),    //nether_wart
                    entry("field_17580", new MaterialData(0.06666666666666667, 1)),    //crop
                    entry("field_16498", new MaterialData(0.01, 1)),   //scaffolding
                    entry("field_11535", new MaterialData(0.19999999999999998, 1)),    //grass
                    entry("field_11533", new MaterialData(0.7999999999999999, 1)),    //metal
                    entry("field_11534", new MaterialData(0.049999999999999996, 1)),   //wet_grass
                    entry("field_11537", new MaterialData(0.39999999999999997, 1)),    //glass
                    entry("field_11538", new MaterialData(0.03333333333333333, 1)),    //bamboo_sapling
                    entry("field_28116", new MaterialData(0.09999999999999999, 1)),    //sculk_sensor
                    entry("field_23265", new MaterialData(0.3333333333333333, 1)),    //lodestone
                    entry("field_22138", new MaterialData(0.09999999999999999, 1)),    //roots (nether grass)
                    entry("field_22139", new MaterialData(0.19999999999999998, 1)),    //shroomlight
                    entry("field_24119", new MaterialData(0.06666666666666667, 1)),    //chain
                    entry("field_11531", new MaterialData(0.7000000000000001, 1)),    //anvil
                    entry("field_11532", new MaterialData(0.09999999999999999, 1)),    //ladder
                    entry("field_29033", new MaterialData(0.6333333333333333, 1)),    //deepslate
                    entry("field_29034", new MaterialData(0.6666666666666666, 1)),    //deepslate_bricks
                    entry("field_11547", new MaterialData(0.06666666666666667, 1)),    //wood (stem/wood?)
                    entry("field_29035", new MaterialData(0.6666666666666666, 1)),    //deepslate_tiles
                    entry("field_11544", new MaterialData(0.5666666666666667, 1)),    //stone
                    entry("field_28061", new MaterialData(0.5, 1)),    //pointed_dripstone
                    entry("field_11545", new MaterialData(0.0033333333333333335, 1)),   //slime_block
                    entry("field_29036", new MaterialData(0.7000000000000001, 1)),    //polished_deepslate
                    entry("field_11548", new MaterialData(0.006666666666666667, 1)),   //snow
                    entry("field_28427", new MaterialData(0.09999999999999999, 1)),    //grass
                    entry("field_28702", new MaterialData(0.09999999999999999, 1)),    //azalea_leaves
                    entry("field_28701", new MaterialData(0.09999999999999999, 1)),    //hanging_roots
                    entry("field_28700", new MaterialData(0.26666666666666666, 1)),    //rooted_dirt
                    entry("field_11542", new MaterialData(0.09999999999999999, 1)),    //bamboo
                    entry("field_18852", new MaterialData(0.26666666666666666, 1)),    //wood (stem/wood?)
                    entry("field_11543", new MaterialData(0.0033333333333333335, 1)),   //wool
                    entry("field_28695", new MaterialData(0.09999999999999999, 1)),    //flowering_azalea
                    entry("field_23083", new MaterialData(0.09999999999999999, 1)),    //vine
                    entry("field_23082", new MaterialData(0.09999999999999999, 1)),    //weeping_vines
                    entry("field_28694", new MaterialData(0.06666666666666667, 1)),    //azalea
                    entry("field_28693", new MaterialData(0.09999999999999999, 1)),    //spore_blossom
                    entry("field_25183", new MaterialData(0.09999999999999999, 1)),    //grass
                    entry("field_28692", new MaterialData(0.09999999999999999, 1)),    //cave_vines
                    entry("field_22150", new MaterialData(0.5, 1)),    //netherite_block
                    entry("field_27203", new MaterialData(0.4666666666666666, 1)),    //calcite
                    entry("field_22151", new MaterialData(0.5333333333333333, 1)),    //ancient_debris
                    entry("field_28699", new MaterialData(0.09999999999999999, 1)),    //small_dripleaf
                    entry("field_22152", new MaterialData(0.19999999999999998, 1)),    //stem (nether)
                    entry("field_27884", new MaterialData(0.0033333333333333335, 1)),   //powder_snow
                    entry("field_27202", new MaterialData(0.43333333333333335, 1)),    //tuff
                    entry("field_28698", new MaterialData(0.13333333333333333, 1)),    //big_dripleaf
                    entry("field_28697", new MaterialData(0.03333333333333333, 1)),    //moss
                    entry("field_22153", new MaterialData(0.19999999999999998, 1)),    //nylium
                    entry("field_27201", new MaterialData(0.09999999999999999, 1)),    //large_amethyst_bud
                    entry("field_27200", new MaterialData(0.09999999999999999, 1)),    //medium_amethyst_bud
                    entry("field_22154", new MaterialData(0.13333333333333333, 1)),    //fungus
                    entry("field_28696", new MaterialData(0.03333333333333333, 1)),    //moss_carpet
                    entry("field_27204", new MaterialData(0.7666666666666666, 1)),    //copper
                    entry("field_17734", new MaterialData(0.06666666666666667, 1)),    //lantern
                    entry("field_17579", new MaterialData(0.09999999999999999, 1)),    //sweet_berry_bush
                    entry("field_28060", new MaterialData(0.6, 1)),    //dripstone_block
                    entry("DEFAULT", new MaterialData(0.6, 1.0))),

            //</editor-fold>

            null, null, null,null, null, null
    ));
    //</editor-fold>


    public final Consumer<SoundPhysicsConfig> configChanger;
    public final String text;
    public void setConfig(){ if (configChanger != null) configChanger.accept(ConfigManager.getConfig());}

    ConfigPresets(String text, @Nullable Consumer<SoundPhysicsConfig> c) {
        this.configChanger = c; 
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
