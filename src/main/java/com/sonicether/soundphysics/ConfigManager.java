package com.sonicether.soundphysics;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class ConfigManager {
    private static ConfigHolder<SoundPhysicsConfig> holder;
    public static final SoundPhysicsConfig DEFAULT = new SoundPhysicsConfig();

    public static void registerAutoConfig() {
        if (holder != null) {
            throw new IllegalStateException("Configuration already registered");
        }

        holder = AutoConfig.register(SoundPhysicsConfig.class, JanksonConfigSerializer::new);
    }

    public static SoundPhysicsConfig getConfig() {
        if (holder == null) {
            return DEFAULT;
        }

        return holder.getConfig();
    }

    public static boolean isConfigUsable() {
        return holder != null && getConfig().isEnabled();
    }

    public static void reload() {
        if (holder == null) {
            return;
        }

        holder.load();
        SoundPhysics.applyConfigChanges();
    }
}
