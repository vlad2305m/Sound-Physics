package com.sonicether.soundphysics;

import net.fabricmc.api.ModInitializer;

public class SoundPhysicsMod implements ModInitializer {
    @Override
    public void onInitialize()
    {
        ConfigManager.registerAutoConfig();
    }
}
