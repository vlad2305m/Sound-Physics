package com.sonicether.soundphysics.config;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static com.sonicether.soundphysics.config.ConfigChanger.changeConfig;

@SuppressWarnings("unused")
public enum ConfigPresets {
    LOAD_SUCCESS(null),
    DEFAULT((SoundPhysicsConfig c) -> changeConfig(c, true,

            1.0, 1.0, 1.0, 1.0,
            1.0, 4.0, 1.0, 0.8,

            true, 32, false,

            1.0, 0.4, 0.3,  0.5, 1.0,
            0.5, 0.05, 0.2, 0.2,

            0.15, 10.0, true,

            false, false, false, false
    )),
    RESET_MATERIALS((SoundPhysicsConfig c) -> changeConfig(c, true,

            null, null, null, null,
            null, null, null, null,

            null, null, null,

            1.0, 0.4, 0.3,  0.5, 1.0,
            0.5, 0.05, 0.2, 0.2,

            null, null, null,

            null, null, null, null
    ));


    private Consumer<SoundPhysicsConfig> configChanger = null;
    public void setConfig(){ if (configChanger != null) configChanger.accept(ConfigManager.getConfig());}

    ConfigPresets(@Nullable Consumer<SoundPhysicsConfig> c) { this.configChanger = c; }


}
