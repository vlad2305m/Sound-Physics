package com.sonicether.soundphysics.config;

import com.sonicether.soundphysics.SoundPhysics;
import com.sonicether.soundphysics.SoundPhysicsMod;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.util.Pair;

import java.util.Map;
import java.util.stream.Collectors;

public class ConfigManager {
    private static ConfigHolder<SoundPhysicsConfig> holder;
    public static final SoundPhysicsConfig DEFAULT = new SoundPhysicsConfig(){{
        Map<String, Pair<Double, String>> map =
                SoundPhysicsMod.blockSoundGroups.entrySet().stream()
                        .collect(Collectors.toMap((e)-> e.getValue().getLeft(), (e) -> new Pair<>(0.5, e.getValue().getRight())));
        map.putIfAbsent("DEFAULT", new Pair<>(0.5, ""));
        Material_Properties.reflectivityMap = map;
    }};

    public static void registerAutoConfig() {
        if (holder != null) {
            throw new IllegalStateException("Configuration already registered");
        }

        holder = AutoConfig.register(SoundPhysicsConfig.class, JanksonConfigSerializer::new);
        holder.load();
        if (!(holder.getConfig().Material_Properties.reflectivityMap == null)) {
            holder.getConfig().preset = ConfigPresets.DrRubisco_Signature;
            holder.getConfig().Material_Properties.reflectivityMap = DEFAULT.Material_Properties.reflectivityMap;
        }
        reload(false);
    }

    public static SoundPhysicsConfig getConfig() {
        if (holder == null) {
            return DEFAULT;
        }

        return holder.getConfig();
    }

    public static void reload(boolean load) {
        if (holder == null) {
            return;
        }

        if(load) holder.load();
        holder.getConfig().preset.setConfig();
        SoundPhysics.syncReverbParams();
        holder.save();
    }

    public static void save() {
        if (holder == null) {
            registerAutoConfig();
        }

        holder.save();
    }
}
