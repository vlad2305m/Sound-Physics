package com.sonicether.soundphysics;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigHolder;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;

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
