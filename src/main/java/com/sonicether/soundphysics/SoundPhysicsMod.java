package com.sonicether.soundphysics;

import com.sonicether.soundphysics.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;

public class SoundPhysicsMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ConfigManager.registerAutoConfig();
    }
}
