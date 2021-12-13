package com.sonicether.soundphysics;

import com.sonicether.soundphysics.config.BlueTapePack.ConfigManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Pair;


import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/*
    Notes for developers:
    !!! Import settings.zip to your IDEA !!! - important notices are marked like that
    ψ This is for anchors that you'd like to jump to from the scroll bar ψ
    //rm is for temporary test code like "System.out.println(var) //rm" (actually */  //rm)


public class SoundPhysicsMod implements ModInitializer {
    public static Map<BlockSoundGroup, Pair<String, String>> blockSoundGroups;
    public static Map<String, BlockSoundGroup> groupSoundBlocks;
    @Override
    public void onInitialize()
    {
        blockSoundGroups = Arrays.stream(BlockSoundGroup.class.getDeclaredFields())
                .filter((f) -> {
                    try {
                        return Modifier.isStatic(f.getModifiers()) && Modifier.isPublic(f.getModifiers())
                                && (f.get(null) instanceof BlockSoundGroup group) && !SoundPhysics.redirectMap.containsKey(group);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return false;
                })
                .collect(Collectors.toMap(
                        (f) -> {
                            try {
                                return (BlockSoundGroup)f.get(null);
                            } catch (IllegalAccessException | ClassCastException e) {
                                e.printStackTrace();
                            }
                            return null;
                        },
                         (f) -> {
                             try {
                                 return new Pair<>(f.getName(), (f.get(null) instanceof BlockSoundGroup g ? (SoundPhysics.groupMap.containsKey(f.getName()) ?  SoundPhysics.groupMap.get(f.getName()) : g.getBreakSound().getId().getPath().split("\\.")[1] ): "not a group"));
                             } catch (IllegalAccessException e) {
                                 e.printStackTrace();
                             }
                             return new Pair<>("", "");
                         }));
        groupSoundBlocks = Arrays.stream(BlockSoundGroup.class.getDeclaredFields())
                .filter((f) -> {
                    try {
                        return Modifier.isStatic(f.getModifiers()) && Modifier.isPublic(f.getModifiers())
                                && (f.get(null) instanceof BlockSoundGroup group) && !SoundPhysics.redirectMap.containsKey(group);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).map((f)-> {
                    BlockSoundGroup b;
                    try { b = (BlockSoundGroup)f.get(null); }
                    catch (IllegalAccessException | ClassCastException e) { e.printStackTrace(); b = null;}
                    return new Pair<>(f.getName(),b);
                }).filter((f) -> f.getRight() != null)
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));

        ConfigManager.registerAutoConfig();
    }
}
