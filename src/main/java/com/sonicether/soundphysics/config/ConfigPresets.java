package com.sonicether.soundphysics.config;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Consumer;

import static com.sonicether.soundphysics.config.ConfigChanger.changeConfig;
import static java.util.Map.entry;

@SuppressWarnings("unused")
public enum ConfigPresets {
    LOAD_SUCCESS(null),
    DEFAULT((SoundPhysicsConfig c) -> changeConfig(c, true,

            1.0, 1.0, 1.0, 1.0,
            1.0, 4.0, 1.0, 0.8,

            true, 256, 4, false,

            Map.ofEntries(entry(".DEFAULT", 0.5), entry("STONE", 1.0), entry("WOOD", 0.4), entry("GRAVEL", 0.3),
            entry("GRASS", 0.5), entry("METAL", 1.0), entry("GLASS", 0.5), entry("WOOL", 0.05),
            entry("SAND", 0.2), entry("SNOW", 0.2), entry("LADDER", 0.4), entry("ANVIL", 1.0)),

            0.15, 10.0, true, true, 0.5, true
    )),
    RESET_MATERIALS((SoundPhysicsConfig c) -> changeConfig(c, true,

            null, null, null, null,
            null, null, null, null,

            null, null, null, null,

            Map.ofEntries(entry(".DEFAULT", 0.5), entry("STONE", 1.0), entry("WOOD", 0.4), entry("GRAVEL", 0.3),
            entry("GRASS", 0.5), entry("METAL", 1.0), entry("GLASS", 0.5), entry("WOOL", 0.05),
            entry("SAND", 0.2), entry("SNOW", 0.2), entry("LADDER", 0.4), entry("ANVIL", 1.0)),

            null, null, null,null, null, null
    )),
    SP1_0_SOUND_OCCLUSION((SoundPhysicsConfig c) -> changeConfig(c, true,

            null, null, null, 10.0,
            null, null, null, null,

            null, null, null, null,

            null,

            null, 10.0, null, null, null, null
    )),
    Dr_Rubisco_Signature((SoundPhysicsConfig c) -> changeConfig(c, true,

            0.7, 1.1, 1.0, 0.8,
            1.2, 4.2, 1.1, 1.0,

            false, 256, 16, false,

            Map.ofEntries(entry("field_11528", 0.5), entry("field_27199", 0.5), entry("field_27198", 0.5),
            entry("field_11529", 1.2), entry("field_27197", 0.5), entry("field_11526", 0.3),
            entry("field_27196", 0.1), entry("field_22140", 0.2), entry("field_24121", 1.8),
            entry("field_22141", 0.1), entry("field_24120", 1.6), entry("field_22142", 0.4),
            entry("field_22143", 1.6), entry("field_22144", 0.1), entry("field_22145", 0.35),
            entry("field_22146", 1.1), entry("field_22147", 0.1), entry("field_22148", 1.2),
            entry("field_21214", 0.01), entry("field_22149", 0.9), entry("field_17581", 0.1),
            entry("field_17580", 0.2), entry("field_16498", 0.03), entry("field_11535", 0.6),
            entry("field_11533", 2.4), entry("field_11534", 0.15), entry("field_11537", 1.2),
            entry("field_11538", 0.1), entry("field_28116", 0.3), entry("field_23265", 1.0),
            entry("field_22138", 0.3), entry("field_22139", 0.6), entry("field_24119", 0.2),
            entry("field_11531", 2.1), entry("field_11532", 0.3), entry("field_29033", 1.9),
            entry("field_29034", 2.0), entry("field_11547", 0.2), entry("field_29035", 2.0),
            entry("field_11544", 1.7), entry("field_28061", 1.5), entry("field_11545", 0.01),
            entry("field_29036", 2.1), entry("field_11548", 0.02), entry("field_28427", 0.3),
            entry("field_28702", 0.3), entry("field_28701", 0.3), entry("field_28700", 0.8),
            entry("field_11542", 0.3), entry("field_18852", 0.8), entry("field_11543", 0.01),
            entry("field_28695", 0.3), entry("field_23083", 0.3), entry("field_23082", 0.3),
            entry("field_28694", 0.2), entry("field_28693", 0.3), entry("field_25183", 0.3),
            entry("field_28692", 0.3), entry("field_22150", 1.5), entry("field_27203", 1.4),
            entry("field_22151", 1.6), entry("field_28699", 0.3), entry("field_22152", 0.6),
            entry("field_27884", 0.01), entry("field_27202", 1.3), entry("field_28698", 0.4),
            entry("field_28697", 0.1), entry("field_22153", 0.6), entry("field_27201", 0.3),
            entry("field_27200", 0.3), entry("field_22154", 0.4), entry("field_28696", 0.1),
            entry("field_27204", 2.3), entry("field_17734", 0.2), entry("field_17579", 0.3),
            entry("field_28060", 1.8), entry("DEFAULT", 0.6)),

            0.3, 10.0, true, true, 0.1, false

    ));




    private final Consumer<SoundPhysicsConfig> configChanger;
    public void setConfig(){ if (configChanger != null) configChanger.accept(ConfigManager.getConfig());}

    ConfigPresets(@Nullable Consumer<SoundPhysicsConfig> c) { this.configChanger = c; }


}
