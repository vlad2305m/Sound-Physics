package com.sonicether.soundphysics.config.presets;

import com.sonicether.soundphysics.SoundPhysics;
import com.sonicether.soundphysics.config.MaterialData;
import com.sonicether.soundphysics.config.SoundPhysicsConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ConfigChanger {
    public static void changeConfig(SoundPhysicsConfig config, @Nullable Boolean enabled,
                                    @Nullable Double attenuationFactor, @Nullable Double globalReverbGain, @Nullable Double globalReverbBrightness, @Nullable Double globalBlockAbsorption, @Nullable Double globalBlockReflectance, @Nullable Double soundDistanceAllowance, @Nullable Double airAbsorption, @Nullable Double underwaterFilter,
                                    @Nullable Boolean skipRainOcclusionTracing, @Nullable Integer environmentEvaluationRays, @Nullable Integer environmentEvaluationRayBounces, @Nullable Boolean simplerSharedAirspaceSimulation,
                                    @Nullable Map<String, MaterialData> materialProperties,
                                    @Nullable Double maxDirectOcclusionFromBlocks, @Nullable Boolean _9RayDirectOcclusion, @Nullable Boolean soundDirectionEvaluation, @Nullable Double directRaysDirEvalMultiplier, @Nullable Boolean notOccludedNoRedirect
    ) {
        if (enabled != null) config.enabled = enabled;
        setGeneral(config.General, attenuationFactor, globalReverbGain, globalReverbBrightness, globalBlockAbsorption, globalBlockReflectance, soundDistanceAllowance, airAbsorption, underwaterFilter);
        setPerformance(config.Performance, skipRainOcclusionTracing, environmentEvaluationRays, environmentEvaluationRayBounces, simplerSharedAirspaceSimulation);
        setMaterial_Properties(config.Materials, materialProperties);
        setVlads_Tweaks(config.Vlads_Tweaks, maxDirectOcclusionFromBlocks, _9RayDirectOcclusion, soundDirectionEvaluation, directRaysDirEvalMultiplier, notOccludedNoRedirect);
        config.preset = ConfigPresets.LOAD_SUCCESS;
    }

    public static void setGeneral(SoundPhysicsConfig.General general, @Nullable Double attenuationFactor, @Nullable Double globalReverbGain, @Nullable Double globalReverbBrightness, @Nullable Double globalBlockAbsorption, @Nullable Double globalBlockReflectance, @Nullable Double soundDistanceAllowance, @Nullable Double airAbsorption, @Nullable Double underwaterFilter) {
        if (attenuationFactor != null) general.attenuationFactor = attenuationFactor;
        if (globalReverbGain != null) general.globalReverbGain = globalReverbGain;
        if (globalReverbBrightness != null) general.globalReverbBrightness = globalReverbBrightness;
        if (globalBlockAbsorption != null) general.globalBlockAbsorption = globalBlockAbsorption;
        if (globalBlockReflectance != null) general.globalBlockReflectance = globalBlockReflectance;
        if (soundDistanceAllowance != null) general.soundDistanceAllowance = soundDistanceAllowance;
        if (airAbsorption != null) general.airAbsorption = airAbsorption;
        if (underwaterFilter != null) general.underwaterFilter = underwaterFilter;
    }

    public static void setPerformance(SoundPhysicsConfig.Performance performance, @Nullable Boolean skipRainOcclusionTracing, @Nullable Integer environmentEvaluationRays, @Nullable Integer environmentEvaluationRayBounces, @Nullable Boolean simplerSharedAirspaceSimulation) {
        if (skipRainOcclusionTracing != null) performance.skipRainOcclusionTracing = skipRainOcclusionTracing;
        if (environmentEvaluationRays != null) performance.environmentEvaluationRays = environmentEvaluationRays;
        if (environmentEvaluationRayBounces != null) performance.environmentEvaluationRayBounces = environmentEvaluationRayBounces;
        if (simplerSharedAirspaceSimulation != null) performance.simplerSharedAirspaceSimulation = simplerSharedAirspaceSimulation;
    }

    public static void setMaterial_Properties(SoundPhysicsConfig.Materials materials, @Nullable Map<String, MaterialData> materialProperties) {
        if (materialProperties != null) materialProperties.forEach((s, newData) -> materials.materialProperties.compute(s, (k, v) -> (v == null) ?
                new MaterialData( "error",
                        newData.getReflectivity() == -1 ? 0.5 : newData.getReflectivity(),
                        newData.getAbsorption() == -1 ? 1 : newData.getAbsorption())
              : new MaterialData( v.getExample(),
                        newData.getReflectivity() == -1 ? v.getReflectivity() : newData.getReflectivity(),
                        newData.getAbsorption() == -1 ? v.getAbsorption() : newData.getAbsorption())));
    }

    public static void setVlads_Tweaks(SoundPhysicsConfig.Vlads_Tweaks vlads_tweaks, @Nullable Double maxDirectOcclusionFromBlocks, @Nullable Boolean _9RayDirectOcclusion, @Nullable Boolean soundDirectionEvaluation, @Nullable Double directRaysDirEvalMultiplier, @Nullable Boolean notOccludedNoRedirect) {
        if (maxDirectOcclusionFromBlocks != null) vlads_tweaks.maxDirectOcclusionFromBlocks = maxDirectOcclusionFromBlocks;
        if (_9RayDirectOcclusion != null) vlads_tweaks._9RayDirectOcclusion = _9RayDirectOcclusion;
        if (soundDirectionEvaluation != null) vlads_tweaks.soundDirectionEvaluation = soundDirectionEvaluation;
        if (directRaysDirEvalMultiplier != null) vlads_tweaks.directRaysDirEvalMultiplier = directRaysDirEvalMultiplier;
        if (notOccludedNoRedirect != null) vlads_tweaks.notOccludedNoRedirect = notOccludedNoRedirect;
    }
    public static String getMaterialExample(String s) {return SoundPhysics.groupMap.get(s);}
}
