package com.sonicether.soundphysics.config.presets;

import com.sonicether.soundphysics.config.BlueTapePack.ConfigManager;
import com.sonicether.soundphysics.config.MaterialData;
import com.sonicether.soundphysics.config.SoundPhysicsConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import static java.util.Map.entry;
import java.util.function.Consumer;
import java.lang.String;

import static com.sonicether.soundphysics.config.presets.ConfigChanger.changeConfig;

@SuppressWarnings({"unused", "RedundantTypeArguments"})
public enum ConfigPresets {
    // Press ctrl+shift+numpad_'-' to collapse all
    LOAD_SUCCESS("Choose", null),
    //<editor-fold desc="DEFAULT_BALANCED,">
    DEFAULT_BALANCED("Balanced (Base)", (SoundPhysicsConfig c) -> changeConfig(c, true,

            1.0, 1.0, 1.0, 1.0,
            1.0, 4.0, 1.0, 1.0, 1.0, 0.8,

            true, 224, 12, false,

            Map.ofEntries(entry("DEFAULT", new MaterialData(0.5, 0.5))),

            4, 10.0, true, true, 0.5, false
    )),//</editor-fold>
    //<editor-fold desc="DEFAULT_PERFORMANCE,">
    DEFAULT_PERFORMANCE("Performance (Base)", (SoundPhysicsConfig c) -> changeConfig(c, true,

            1.0, 1.0, 1.0, 1.0,
            1.0, 4.0, 1.0, 1.0, 1.0, 0.8,

            true, 96, 6, true,

            Map.ofEntries(entry("DEFAULT", new MaterialData(0.5, 0.5))),

            4, 10.0, true, true, 0.5, true
    )),//</editor-fold>
    //<editor-fold desc="DEFAULT_QUALITY,">
    DEFAULT_QUALITY("Quality (Base)", (SoundPhysicsConfig c) -> changeConfig(c, true,

            1.0, 1.0, 1.0, 1.0,
            1.0, 4.0, 1.0, 1.0, 1.0, 0.8,

            false, 512, 24, false,

            Map.ofEntries(entry("DEFAULT", new MaterialData(0.5, 0.5))),

            4, 10.0, true, true, 0.5, false
    )),//</editor-fold>
    //<editor-fold desc="THEDOCRUBY,">
    THEDOCRUBY("Dr. Rubisco's Signature Sound", (SoundPhysicsConfig c) -> changeConfig(c, true,

            1.0, 0.8, 1.0, 0.8,
            1.0, 3.5, 1.0, 1.0, 1.0, 0.8,

            true, 256, 16, false,

            //<editor-fold desc="Map.ofEntries(),">
            Map.<String, MaterialData>ofEntries(
                    entry("field_11528", new MaterialData(0.2,                  0.45 )),    // Coral              (coral_block)
                    entry("field_11529", new MaterialData(0.5,                  0.4  )),    // Gravel, Dirt       (gravel, rooted_dirt)
                    entry("field_27197", new MaterialData(0.75,                 0.45 )),    // Amethyst           (amethyst_block, small_amethyst_bud, medium_amethyst_bud, large_amethyst_bud, amethyst_cluster)
                    entry("field_11526", new MaterialData(0.1,                  0.45 )),    // Sand               (sand)
                    entry("field_27196", new MaterialData(0.4,                  0.3  )),    // Candle Wax         (candle)
                    entry("field_22140", new MaterialData(0.2,                  0.2  )),    // Weeping Vines      (weeping_vines, weeping_vines_low_pitch)
                    entry("field_22141", new MaterialData(0.05,                 0.65 )),    // Soul Sand          (soul_sand)
                    entry("field_22142", new MaterialData(0.1,                  0.7  )),    // Soul Soil          (soul_soil)
                    entry("field_22143", new MaterialData(0.8,                  0.3  )),    // Basalt             (basalt)
                    entry("field_22145", new MaterialData(0.75,                 0.45 )),    // Netherrack         (netherrack, nether_ore, nether_gold_ore)
                    entry("field_22146", new MaterialData(0.85,                 0.55 )),    // Nether Brick       (nether_bricks)
                    entry("field_21214", new MaterialData(0.08,                 0.85 )),    // Honey              (honey_block)
                    entry("field_22149", new MaterialData(0.7,                  0.55 )),    // Bone               (bone_block)
                    entry("field_17581", new MaterialData(0.2,                  0.8  )),    // Nether Wart        (nether_wart, wart_block)
                    entry("field_11535", new MaterialData(0.2,                  0.6  )),    // Crops and Foliage  (grass, crop, bamboo_sapling, sweet_berry_bush)
                    entry("field_11533", new MaterialData(0.85,                 0.5  )),    // Metal              (metal, copper, anvil)
                    entry("field_11534", new MaterialData(0.15,                 0.8  )),    // Aquatic Foliage    (wet_grass, lily_pad)
                    entry("field_11537", new MaterialData(0.5,                  0.45 )),    // Glass, Ice         (glass)
                    entry("field_28116", new MaterialData(0.4,                  0.6  )),    // Sculk Sensor       (sculk_sensor)
                    entry("field_22138", new MaterialData(0.15,                 0.55 )),    // Nether Foliage     (roots, nether_sprouts)
                    entry("field_22139", new MaterialData(0.85,                 0.75 )),    // Shroomlight        (shroomlight)
                    entry("field_24119", new MaterialData(0.4,                  0.4  )),    // Chain              (chain)
                    entry("field_29033", new MaterialData(0.88,                 0.55 )),    // Deepslate          (deepslate)
                    entry("field_11547", new MaterialData(0.65,                 0.45 )),    // Wood               (wood, ladder)
                    entry("field_29035", new MaterialData(0.95,                 0.55 )),    // Deepslate Tiles    (deepslate_tiles)
                    entry("field_11544", new MaterialData(0.83,                 0.5  )),    // Stone, Blackstone  (stone, calcite, gilded_blackstone)
                    entry("field_11545", new MaterialData(1.0,                  0.25 )),    // Slime              (slime_block)
                    entry("field_29036", new MaterialData(0.99,                 0.55 )),    // Polished Deepslate (polished_deepslate, deepslate_bricks)
                    entry("field_11548", new MaterialData(0.1,                  0.5  )),    // Snow               (snow)
                    entry("field_28702", new MaterialData(0.3,                  0.35 )),    // Azalea Leaves      (azalea_leaves)
                    entry("field_11542", new MaterialData(0.5,                  0.4  )),    // Bamboo             (bamboo, scaffolding)
                    entry("field_18852", new MaterialData(0.6,                  0.65 )),    // Mushroom Stems     (stem)
                    entry("field_11543", new MaterialData(0.02,                 1.0  )),    // Wool               (wool)
                    entry("field_23083", new MaterialData(0.1,                  0.15 )),    // Dry Foliage        (vine, hanging_roots, glow_lichen)
                    entry("field_28694", new MaterialData(0.15,                 0.5  )),    // Azalea Bush        (azalea)
                    entry("field_28692", new MaterialData(0.2,                  0.2  )),    // Lush Foliage       (cave_vines, spore_blossom, small_dripleaf, big_dripleaf)
                    entry("field_22150", new MaterialData(1.0,                  0.6  )),    // Netherite          (netherite_block, lodestone)
                    entry("field_22151", new MaterialData(0.45,                 0.8  )),    // Ancient Debris     (ancient_debris)
                    entry("field_22152", new MaterialData(0.3,                  0.55 )),    // Nether Fungus Stem (nether_stem)
                    entry("field_27884", new MaterialData(0.01,                 0.1  )),    // Powder Snow        (powder_snow)
                    entry("field_27202", new MaterialData(0.35,                 0.4  )),    // Tuff               (tuff)
                    entry("field_28697", new MaterialData(0.1,                  0.85 )),    // Moss               (moss, moss_carpet)
                    entry("field_22153", new MaterialData(0.4,                  0.55 )),    // Nylium             (nylium)
                    entry("field_22154", new MaterialData(0.4,                  0.6  )),    // Nether Fungus      (fungus)
                    entry("field_17734", new MaterialData(0.75,                 0.4  )),    // Lanterns           (lantern)
                    entry("field_28060", new MaterialData(0.9,                  0.6  )),    // Dripstone          (dripstone_block, pointed_dripstone)
                    entry("DEFAULT"    , new MaterialData(0.5,                  0.5  )),    // Default Material   ()
                    entry("block.minecraft.water"    , new MaterialData(0.5,    0.03 ))     // Water              (block.minecraft.water)
            ),//</editor-fold>

            4, 10.0, true, true, 0.5, false

    )),//</editor-fold>
    //<editor-fold desc="SUPER_REVERB,">
    SUPER_REVERB("Super Reverb", (SoundPhysicsConfig c) -> changeConfig(c, true,
            null, 1.8, null, null,
            4.0, null, null, null, null, null,

            null, null, null, null,

            null,

            null, null, null, null, null, null

    )),//</editor-fold>
    //<editor-fold desc="LUSH_REVERB,">
    LUSH_REVERB("More Lush Cave Reverb", (SoundPhysicsConfig c) -> changeConfig(c, true,
            null, null, null, null,
            null, null, null, null, null, null,

            null, null, null, null,

            //<editor-fold desc="Map.ofEntries(),">
            Map.<String, MaterialData>ofEntries(
                    entry("field_28697", new MaterialData(0.85,                 0.85 )),    // Moss               (moss, moss_carpet)
                    entry("field_11529", new MaterialData(0.7,                  0.4  )),    // Gravel, Dirt       (gravel, rooted_dirt)
                    entry("field_23083", new MaterialData(0.25,                 0.15 )),    // Dry Foliage        (vine, hanging_roots, glow_lichen)
                    entry("field_28694", new MaterialData(0.45,                 0.5  )),    // Azalea Bush        (azalea)
                    entry("field_28692", new MaterialData(0.65,                 0.2  ))     // Lush Foliage       (cave_vines, spore_blossom, small_dripleaf, big_dripleaf)
            ),//</editor-fold>

            null, null, null, null, null, null
    )),//</editor-fold>
    //<editor-fold desc="NO_ABSORPTION,">
    NO_ABSORPTION("No Absorption", (SoundPhysicsConfig c) -> changeConfig(c, true,
            null, null, null, 0.0,
            null, null, 0.0, 0.0, 0.0, null,

            null, null, null, null,

            null,

            null, 0.0, null, null, null, null
    )),//</editor-fold>
    //<editor-fold desc="LOW_FREQ,">
    LOW_FREQ("Bass Boost", (SoundPhysicsConfig c) -> changeConfig(c, true,
            null, null, 0.2, null,
            null, null, 2.0, 2.0, 2.0, null,

            null, null, null, null,

            null,

            null, null, null, null, null, null
    )),//</editor-fold>
    //<editor-fold desc="HIGH_FREQ,">
    HIGH_FREQ("Treble Boost", (SoundPhysicsConfig c) -> changeConfig(c, true,
            null, null, 1.8, null,
            null, null, 0.5, 0.5, 0.5, null,

            null, null, null, null,

            null,

            null, null, null, null, null, null
    )),//</editor-fold>
    //<editor-fold desc="FOG,">
    FOG("Foggy Air", (SoundPhysicsConfig c) -> changeConfig(c, true,
            2.5, null, null, null,
            null, null, 7.5, 0.5, 1.0, null,

            null, null, null, null,

            null,

            null, null, null, null, null, null
    )),//</editor-fold>
    //<editor-fold desc="TOTAL_OCCLUSION,">
    TOTAL_OCCLUSION("Total Occlusion", (SoundPhysicsConfig c) -> changeConfig(c, true,

            null, null, null, 10.0,
            null, null, null, null, null, null,

            null, null, null, null,

            null,

            null, 10.0,  null, null, null, null
    )),//</editor-fold>
    //<editor-fold desc="RESET_MATERIALS;">
    RESET_MATERIALS("Clear Material Properties", (SoundPhysicsConfig c) -> changeConfig(c, true,

            null, null, null, null,
            null, null, null, null,

            null, null, null, null, null, null,

            //<editor-fold desc="Map.ofEntries(),">
            Map.<String, MaterialData>ofEntries(
                    entry("field_11528", new MaterialData(0.2,                  0.45 )),    // Coral              (coral_block)
                    entry("field_11529", new MaterialData(0.5,                  0.4  )),    // Gravel, Dirt       (gravel, rooted_dirt)
                    entry("field_27197", new MaterialData(0.75,                 0.45 )),    // Amethyst           (amethyst_block, small_amethyst_bud, medium_amethyst_bud, large_amethyst_bud, amethyst_cluster)
                    entry("field_11526", new MaterialData(0.1,                  0.45 )),    // Sand               (sand)
                    entry("field_27196", new MaterialData(0.4,                  0.3  )),    // Candle Wax         (candle)
                    entry("field_22140", new MaterialData(0.2,                  0.2  )),    // Weeping Vines      (weeping_vines, weeping_vines_low_pitch)
                    entry("field_22141", new MaterialData(0.05,                 0.65 )),    // Soul Sand          (soul_sand)
                    entry("field_22142", new MaterialData(0.1,                  0.7  )),    // Soul Soil          (soul_soil)
                    entry("field_22143", new MaterialData(0.8,                  0.3  )),    // Basalt             (basalt)
                    entry("field_22145", new MaterialData(0.75,                 0.45 )),    // Netherrack         (netherrack, nether_ore, nether_gold_ore)
                    entry("field_22146", new MaterialData(0.85,                 0.55 )),    // Nether Brick       (nether_bricks)
                    entry("field_21214", new MaterialData(0.08,                 0.85 )),    // Honey              (honey_block)
                    entry("field_22149", new MaterialData(0.7,                  0.55 )),    // Bone               (bone_block)
                    entry("field_17581", new MaterialData(0.2,                  0.8  )),    // Nether Wart        (nether_wart, wart_block)
                    entry("field_11535", new MaterialData(0.2,                  0.6  )),    // Crops and Foliage  (grass, crop, bamboo_sapling, sweet_berry_bush)
                    entry("field_11533", new MaterialData(0.85,                 0.5  )),    // Metal              (metal, copper, anvil)
                    entry("field_11534", new MaterialData(0.15,                 0.8  )),    // Aquatic Foliage    (wet_grass, lily_pad)
                    entry("field_11537", new MaterialData(0.5,                  0.45 )),    // Glass, Ice         (glass)
                    entry("field_28116", new MaterialData(0.4,                  0.6  )),    // Sculk Sensor       (sculk_sensor)
                    entry("field_22138", new MaterialData(0.15,                 0.55 )),    // Nether Foliage     (roots, nether_sprouts)
                    entry("field_22139", new MaterialData(0.85,                 0.75 )),    // Shroomlight        (shroomlight)
                    entry("field_24119", new MaterialData(0.4,                  0.4  )),    // Chain              (chain)
                    entry("field_29033", new MaterialData(0.88,                 0.55 )),    // Deepslate          (deepslate)
                    entry("field_11547", new MaterialData(0.65,                 0.45 )),    // Wood               (wood, ladder)
                    entry("field_29035", new MaterialData(0.95,                 0.55 )),    // Deepslate Tiles    (deepslate_tiles)
                    entry("field_11544", new MaterialData(0.83,                 0.5  )),    // Stone, Blackstone  (stone, calcite, gilded_blackstone)
                    entry("field_11545", new MaterialData(1.0,                  0.25 )),    // Slime              (slime_block)
                    entry("field_29036", new MaterialData(0.99,                 0.55 )),    // Polished Deepslate (polished_deepslate, deepslate_bricks)
                    entry("field_11548", new MaterialData(0.1,                  0.5  )),    // Snow               (snow)
                    entry("field_28702", new MaterialData(0.3,                  0.35 )),    // Azalea Leaves      (azalea_leaves)
                    entry("field_11542", new MaterialData(0.5,                  0.4  )),    // Bamboo             (bamboo, scaffolding)
                    entry("field_18852", new MaterialData(0.6,                  0.65 )),    // Mushroom Stems     (stem)
                    entry("field_11543", new MaterialData(0.02,                 1.0  )),    // Wool               (wool)
                    entry("field_23083", new MaterialData(0.1,                  0.15 )),    // Dry Foliage        (vine, hanging_roots, glow_lichen)
                    entry("field_28694", new MaterialData(0.15,                 0.5  )),    // Azalea Bush        (azalea)
                    entry("field_28692", new MaterialData(0.2,                  0.2  )),    // Lush Foliage       (cave_vines, spore_blossom, small_dripleaf, big_dripleaf)
                    entry("field_22150", new MaterialData(1.0,                  0.6  )),    // Netherite          (netherite_block, lodestone)
                    entry("field_22151", new MaterialData(0.45,                 0.8  )),    // Ancient Debris     (ancient_debris)
                    entry("field_22152", new MaterialData(0.3,                  0.55 )),    // Nether Fungus Stem (nether_stem)
                    entry("field_27884", new MaterialData(0.01,                 0.1  )),    // Powder Snow        (powder_snow)
                    entry("field_27202", new MaterialData(0.35,                 0.4  )),    // Tuff               (tuff)
                    entry("field_28697", new MaterialData(0.1,                  0.85 )),    // Moss               (moss, moss_carpet)
                    entry("field_22153", new MaterialData(0.4,                  0.55 )),    // Nylium             (nylium)
                    entry("field_22154", new MaterialData(0.4,                  0.6  )),    // Nether Fungus      (fungus)
                    entry("field_17734", new MaterialData(0.75,                 0.4  )),    // Lanterns           (lantern)
                    entry("field_28060", new MaterialData(0.9,                  0.6  )),    // Dripstone          (dripstone_block, pointed_dripstone)
                    entry("DEFAULT"    , new MaterialData(0.5,                  0.5  )),    // Default Material   ()
                    entry("block.minecraft.water"    , new MaterialData(0.5,    0.03 ))     // Water              (block.minecraft.water)
            ),//</editor-fold>

            null, null, null, null,null, null
    ));//</editor-fold>

    public final Consumer<SoundPhysicsConfig> configChanger;
    public final String text;
    public void setConfig(){ if (configChanger != null) {configChanger.accept(ConfigManager.getConfig());ConfigManager.save();}}

    ConfigPresets(String text, @Nullable Consumer<SoundPhysicsConfig> c) {
        this.configChanger = c; 
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
