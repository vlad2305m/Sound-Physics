package com.sonicether.soundphysics.config;

import com.sonicether.soundphysics.config.presets.ConfigPresets;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("CanBeFinal")
@Config(name = "sound_physics")
@Config.Gui.Background("minecraft:textures/block/note_block.png")
public class SoundPhysicsConfig implements ConfigData {

    @Comment("Enable reverb?")
    public boolean enabled = true;

    @ConfigEntry.Gui.CollapsibleObject
    public General General = new General();

    @ConfigEntry.Gui.CollapsibleObject
    public Performance Performance = new Performance();

    @ConfigEntry.Gui.CollapsibleObject
    public Material_Properties Material_Properties = new Material_Properties();

    @ConfigEntry.Gui.CollapsibleObject
    public Vlads_Tweaks Vlads_Tweaks = new Vlads_Tweaks();

    @ConfigEntry.Gui.CollapsibleObject
    public Misc Misc = new Misc();

    public static class General{
        @Comment("Affects how quiet a sound gets based on distance. Lower values mean distant sounds are louder.\n1.0 is the physically correct value.\n0.2 - 1.0 or just don't set it to 0")
        public double attenuationFactor = 1.0;
        @Comment("The global volume of simulated reverberations.\n0.1 - 2.0")
        public double globalReverbGain = 1.0;
        @Comment("The brightness of reverberation.\nHigher values result in more high frequencies in reverberation.\nLower values give a more muffled sound to the reverb.\n0.1 - 2.0")
        public double globalReverbBrightness = 1.0;
        @Comment("The global amount of sound that will be absorbed when traveling through blocks.\n 0.1 - 4.0")
        public double globalBlockAbsorption = 1.0;
        @Comment("The global amount of sound reflectance energy of all blocks.\nLower values result in more conservative reverb simulation with shorter reverb tails.\nHigher values result in more generous reverb simulation with higher reverb tails.\n0.1 - 4.0")
        public double globalBlockReflectance = 1.0;
        @Comment("Minecraft won't allow sounds to play past a certain distance;\nSoundPhysics makes that configurable by multiplying this parameter by the default distance.\nValues too high can cause polyphony issues.\n1.0 - 6.0")
        public double soundDistanceAllowance = 4.0;
        @Comment("Represents how aggressively air absorbs high frequencies over distance.\nA value of 1.0 is physically correct for air with normal humidity and temperature.\nHigher values mean air will absorb more high frequencies with distance.\nA value of 0.0 disables this effect.")
        public double airAbsorption = 1.0;
        @Comment("How much sound is filtered when the player is underwater.\n0.0 means no filter. 1.0 means fully filtered.\n0.0 - 1.0")
        public double underwaterFilter = 0.8;
    }

    public static class Performance{
        @Comment("If true, rain sound sources won't trace for sound occlusion.\nThis can help performance during rain.")
        public boolean skipRainOcclusionTracing = true;
        @Comment("The number of rays to trace to determine reverberation for each sound source.\nMore rays provides more consistent tracing results but takes more time to calculate.\nDecrease this value if you experience lag spikes when sounds play.")
        @ConfigEntry.BoundedDiscrete(max = 768, min = 8)
        public int environmentEvaluationRays = 224;
        @Comment("The number of rays bounces to trace to determine reverberation for each sound source.\nMore bounces provides more echo and sound ducting but takes more time to calculate.\nDecrease this value if you experience lag spikes when sounds play. Capped by max distance.")
        @ConfigEntry.BoundedDiscrete(max = 32, min = 2)
        public int environmentEvaluationRayBounces = 12;
        @Comment("If true, enables a simpler technique for determining when the player and a sound source share airspace.\nMight sometimes miss recognizing shared airspace, but it's faster to calculate.")
        public boolean simplerSharedAirspaceSimulation = false;
    }

    public static class Material_Properties {
        @Comment("Sound reflectivity for blocks.\n0.0 - 1.0")
        public Map<String, MaterialData> reflectivityMap = null;

        @Comment("Makes blocks use name (like block.minecraft.stone) instead of sound group to determine material")
        public List<String> blockWhiteList = new ArrayList<>();
    }

    public static class Vlads_Tweaks {
        @Comment("The amount at which this is capped. 10 * block_occlusion is the theoretical limit")
        public double maxDirectOcclusionFromBlocks = 10;
        @Comment("Calculate direct occlusion as the minimum of 9 rays from vertices of a block")
        public boolean _9RayDirectOcclusion = true;
        @Comment("Whether to try calculating where the sound should come from based on reflections")
        public boolean soundDirectionEvaluation = true;
        @Comment("How much the sound direction depends on reflected sounds.\nRequires \"Re-calculate sound direction\" to be enabled.\n0.0 is no reflected sources, 1.0 is 100% reflected sources.\n0.5 is approximately physically accurate.")
        public double directRaysDirEvalMultiplier = 0.5;
        @Comment("Randomness of the perceived direction of incoming sounds\n0.0 means sounds come straight from the source.\n1.0 means sounds come from completely random directions\n0.0 - 1.0")
        public double maxDirVariance = 0.0;
        @Comment("Skip redirecting non-occluded sounds (the ones you can see directly).\nCan be inaccurate in some situations, especially when \"Re-calculate sound direction\" is enabled.")
        public boolean notOccludedNoRedirect = false;
    }

    public static class Misc {
        @Comment("General debug logging")
        public boolean debugLogging = false;
        @Comment("Occlusion tracing information logging")
        public boolean occlusionLogging = false;
        @Comment("Environment evaluation information logging")
        public boolean environmentLogging = false;
        @Comment("Performance information logging")
        public boolean performanceLogging = false;
        @Comment("Particles on traced blocks (structure_void is a block)")
        public boolean raytraceParticles = false;
    }

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
    @Comment("Soft presets. Some of these can be applied one after another to stack effects onto a base profile.\nSet \"Config has changed\" to true before saving!")
    public ConfigPresets preset = ConfigPresets.THEDOCRUBY;
}
