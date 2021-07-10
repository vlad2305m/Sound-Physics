package com.sonicether.soundphysics.config;

import com.sonicether.soundphysics.SoundPhysics;
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

    public static void reload() {
        if (holder == null) {
            return;
        }

        holder.load();
        holder.getConfig().preset.setConfig();
        SoundPhysics.syncReverbParams();
    }

    public static void save() {
        if (holder == null) {
            registerAutoConfig();
        }

        holder.save();
    }
}
