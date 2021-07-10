package com.sonicether.soundphysics;

import com.sonicether.soundphysics.config.ConfigManager;
import net.fabricmc.api.ModInitializer;

public class SoundPhysicsMod implements ModInitializer {
    @Override
    public void onInitialize()
    {
        ConfigManager.registerAutoConfig();
    }
}
