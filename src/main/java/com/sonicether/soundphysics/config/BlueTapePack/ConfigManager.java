package com.sonicether.soundphysics.config.BlueTapePack;

import com.sonicether.soundphysics.ALstuff.SPEfx;
import com.sonicether.soundphysics.SPLog;
import com.sonicether.soundphysics.SoundPhysicsMod;
import com.sonicether.soundphysics.config.MaterialData;
import com.sonicether.soundphysics.config.PrecomputedConfig;
import com.sonicether.soundphysics.config.SoundPhysicsConfig;
import com.sonicether.soundphysics.config.presets.ConfigPresets;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.util.ActionResult;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigManager {
    private static ConfigHolder<SoundPhysicsConfig> holder;

    public static final SoundPhysicsConfig DEFAULT = new SoundPhysicsConfig(){{
        Map<String, MaterialData> map =
                SoundPhysicsMod.blockSoundGroups.entrySet().stream()
                        .collect(Collectors.toMap((e)-> e.getValue().getLeft(), (e) -> new MaterialData(e.getValue().getRight(), 0.5, 0.5)));
        map.putIfAbsent("DEFAULT", new MaterialData(SoundPhysicsMod.groupMap.get("DEFAULT"), 0.5, 0.5));
        Materials.materialProperties = map;
    }};

    public static void registerAutoConfig() {
        if (holder != null) {throw new IllegalStateException("Configuration already registered");}
        holder = AutoConfig.register(SoundPhysicsConfig.class, JanksonConfigSerializer::new);

        try {GuiRegistryinit.register();} catch (@SuppressWarnings("CatchMayIgnoreException") Exception ignored){ignored.printStackTrace();}

        holder.registerSaveListener((holder, config) -> onSave(config));
        holder.load();
        onSave(holder.getConfig());
        save();
    }

    public static SoundPhysicsConfig getConfig() {
        if (holder == null) {return DEFAULT;}

        return holder.getConfig();
    }

    public static void reload(boolean load) {
        if (holder == null) {return;}

        if(load) holder.load();
        holder.getConfig().preset.setConfig();
        SPEfx.syncReverbParams();
        holder.save();
    }

    public static void save() { if (holder == null) {registerAutoConfig();} holder.save(); }

    public static void handleBrokenMaterials( SoundPhysicsConfig c ){
        SPLog.logError("Critical materialProperties error. Resetting materialProperties");
        SoundPhysicsConfig fallback = DEFAULT;
        ConfigPresets.THEDOCRUBY.configChanger.accept(fallback);
        c.Materials.materialProperties = fallback.Materials.materialProperties;
        c.Materials.blockWhiteList = List.of("block.minecraft.water");
    }

    public static ActionResult onSave(SoundPhysicsConfig c) {
        if (c.Materials.materialProperties == null || c.Materials.materialProperties.get("DEFAULT") == null)
            handleBrokenMaterials(c);
        if (c.preset != ConfigPresets.LOAD_SUCCESS) {c.preset.configChanger.accept(c);}
        if(PrecomputedConfig.pC != null) PrecomputedConfig.pC.deactivate();
        try {
            PrecomputedConfig.pC = new PrecomputedConfig(c);} catch (CloneNotSupportedException e) {e.printStackTrace(); return ActionResult.FAIL;}
        SPEfx.syncReverbParams();
        return ActionResult.SUCCESS;
    }
}
