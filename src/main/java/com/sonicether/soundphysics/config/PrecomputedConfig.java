package com.sonicether.soundphysics.config;

import com.sonicether.soundphysics.SPLog;
import com.sonicether.soundphysics.SoundPhysicsMod;
import it.unimi.dsi.fastutil.objects.Reference2DoubleOpenHashMap;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

/*
    Values, which remain constant after the config has changed
 */
public class PrecomputedConfig {
    public boolean on;
    public double attenuationFactor;
    public double globalReverbGain;
    public double globalReverbBrightness;
    public double globalBlockAbsorption;
    public double globalBlockReflectance;
    public double airAbsorption;
    public double underwaterFilter;

    public boolean skipRainOcclusionTracing;
    public int environmentEvaluationRays;
    public int environmentEvaluationRayBounces;
    public boolean simplerSharedAirspaceSimulation;

    public Reference2DoubleOpenHashMap<BlockSoundGroup> reflectivityMap;
    public double defaultReflectivity;
    public Reference2DoubleOpenHashMap<BlockSoundGroup> absorptionMap;
    public double defaultAbsorption;
    public Set<String> blockWhiteSet;
    public Map<String, MaterialData> blockWhiteMap;

    public double maxDirectOcclusionFromBlocks;
    public boolean _9Ray;
    public boolean soundDirectionEvaluation;
    public double directRaysDirEvalMultiplier;
    public double maxDirVariance;
    public boolean notOccludedNoRedirect;

    public boolean dLog;
    public boolean oLog;
    public boolean eLog;
    public boolean pLog;
    public boolean rays;



    public PrecomputedConfig(SoundPhysicsConfig c){
        on = c.enabled;

        attenuationFactor = c.General.attenuationFactor;
        globalReverbGain = c.General.globalReverbGain;
        globalReverbBrightness = c.General.globalReverbBrightness;
        globalBlockAbsorption = c.General.globalBlockAbsorption;
        globalBlockReflectance = c.General.globalBlockReflectance;
        airAbsorption = c.General.airAbsorption;
        underwaterFilter = c.General.underwaterFilter;

        skipRainOcclusionTracing = c.Performance.skipRainOcclusionTracing;
        environmentEvaluationRays = c.Performance.environmentEvaluationRays;
        environmentEvaluationRayBounces = c.Performance.environmentEvaluationRayBounces;
        simplerSharedAirspaceSimulation = c.Performance.simplerSharedAirspaceSimulation;

        blockWhiteSet = new HashSet<>(c.Material_Properties.blockWhiteList);
        blockWhiteMap = c.Material_Properties.blockWhiteList.stream()
                .map((a) -> new Pair<>(a, c.Material_Properties.reflectivityMap.get(a)))
                .filter((e) -> {
                    if (e.getRight() != null) return true;
                    SPLog.logError("Missing material data for "+e.getLeft());
                    return false;
                }).collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
        defaultReflectivity = c.Material_Properties.reflectivityMap.get("DEFAULT").reflectivity;
        defaultAbsorption = c.Material_Properties.reflectivityMap.get("DEFAULT").absorption;
        reflectivityMap = new Reference2DoubleOpenHashMap<>();
        absorptionMap = new Reference2DoubleOpenHashMap<>();
        final List<String> wrong = new java.util.ArrayList<>();
        c.Material_Properties.reflectivityMap.forEach((k, v) -> {
            BlockSoundGroup bsg = SoundPhysicsMod.groupSoundBlocks.get(k);
            if (bsg != null){
                reflectivityMap.put(bsg, v.reflectivity);
                absorptionMap.put(bsg, v.absorption);
            }
            else {
                if (!k.equals("DEFAULT") && !blockWhiteSet.contains(k)){
                    wrong.add(k+" ("+v.example+")");
                }
            }
        });
        if (!wrong.isEmpty()) SPLog.logError("MaterialData map contains "+wrong.size()+" extra entries: "+ Arrays.toString(new List[]{wrong}));

        maxDirectOcclusionFromBlocks = c.Vlads_Tweaks.maxDirectOcclusionFromBlocks;
        _9Ray = c.Vlads_Tweaks._9RayDirectOcclusion;
        soundDirectionEvaluation = c.Vlads_Tweaks.soundDirectionEvaluation;
        directRaysDirEvalMultiplier = c.Vlads_Tweaks.directRaysDirEvalMultiplier;
        maxDirVariance = c.Vlads_Tweaks.maxDirVariance;
        notOccludedNoRedirect = c.Vlads_Tweaks.notOccludedNoRedirect;

        dLog = c.Misc.debugLogging;
        oLog = c.Misc.occlusionLogging;
        eLog = c.Misc.environmentLogging;
        pLog = c.Misc.performanceLogging;
        rays = c.Misc.raytraceParticles;
    }
}
