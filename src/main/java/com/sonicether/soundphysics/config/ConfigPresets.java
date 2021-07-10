package com.sonicether.soundphysics.config;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static com.sonicether.soundphysics.config.ConfigChanger.changeConfig;

@SuppressWarnings("unused")
public enum ConfigPresets {
    LOAD_SUCCESS(null),
    DEFAULT((SoundPhysicsConfig c) -> changeConfig(c, true,

            1f, 1f, 1f, 1f,
            1f, 4f, 1f, 0.8f,

            true, 32, false,

            1f, 0.4f, 0.3f,  0.5f, 1f,
            0.5f, 0.05f, 0.2f, 0.2f,

            0.15f, 10f, true,

            false, false, false, false
    )),
    RESET_MATERIALS((SoundPhysicsConfig c) -> changeConfig(c, true,

            null, null, null, null,
            null, null, null, null,

            null, null, null,

            1f, 0.4f, 0.3f,  0.5f, 1f,
            0.5f, 0.05f, 0.2f, 0.2f,

            null, null, null,

            null, null, null, null
    ));


    private Consumer<SoundPhysicsConfig> configChanger = null;
    public void setConfig(){ if (configChanger != null) configChanger.accept(ConfigManager.getConfig());}

    ConfigPresets(@Nullable Consumer<SoundPhysicsConfig> c) { this.configChanger = c; }


}
