package com.sonicether.soundphysics.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "sound_physics")
@Config.Gui.Background("minecraft:textures/block/note_block.png")
public class SoundPhysicsConfig implements ConfigData {

    @Comment("Enable reverb?")
    public boolean enabled = true;

    @Comment("Don't forget to make this true when you change the config")
    public boolean reloadReverb = true;

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
        @Comment("Affects how quiet a sound gets based on distance. Lower values mean distant sounds are louder. 1.0 is the physically correct value.\n0.2 - 1.0")
        public float attenuationFactor = 1.0f;
        @Comment("The global volume of simulated reverberations.\n0.1 - 2.0")
        public float globalReverbGain = 1.0f;
        @Comment("The brightness of reverberation. Higher values result in more high frequencies in reverberation. Lower values give a more muffled sound to the reverb.\n0.1 - 2.0")
        public float globalReverbBrightness = 1.0f;
        @Comment("The global amount of sound that will be absorbed when traveling through blocks.\n 0.1 - 4.0")
        public float globalBlockAbsorption = 1.0f;
        @Comment("The global amount of sound reflectance energy of all blocks. Lower values result in more conservative reverb simulation with shorter reverb tails. Higher values result in more generous reverb simulation with higher reverb tails.\n0.1 - 4.0")
        public float globalBlockReflectance = 1.0f;
        @Comment("Minecraft won't allow sounds to play past a certain distance. This parameter is a multiplier for how far away a sound source is allowed to be in order for it to actually play. Values too high can cause polyphony issues.\n1.0 - 6.0")
        public float soundDistanceAllowance = 4.0f;
        @Comment("A value controlling the amount that air absorbs high frequencies with distance. A value of 1.0 is physically correct for air with normal humidity and temperature. Higher values mean air will absorb more high frequencies with distance. 0 disables this effect.\n0.0 - 5.0")
        public float airAbsorption = 1.0f;
        @Comment("How much sound is filtered when the player is underwater. 0.0 means no filter. 1.0 means fully filtered.\n0.0 - 1.0")
        public float underwaterFilter = 0.8f;
    }

    public static class Performance{
        @Comment("If true, rain sound sources won't trace for sound occlusion. This can help performance during rain.")
        public boolean skipRainOcclusionTracing = true;
        @Comment("The number of rays to trace to determine reverberation for each sound source. More rays provides more consistent tracing results but takes more time to calculate. Decrease this value if you experience lag spikes when sounds play.\n8 - 64")
        public int environmentEvaluationRays = 32;
        @Comment("If true, enables a simpler technique for determining when the player and a sound source share airspace. Might sometimes miss recognizing shared airspace, but it's faster to calculate.")
        public boolean simplerSharedAirspaceSimulation = false;
    }

    public static class Material_Properties {
        @Comment("Sound reflectivity for stone blocks.\n0.0 - 1.0")
        public float stoneReflectivity = 1.0f;
        @Comment("Sound reflectivity for wooden blocks.\n0.0 - 1.0")
        public float woodReflectivity = 0.4f;
        @Comment("Sound reflectivity for ground blocks (dirt, gravel, etc).\n0.0 - 1.0")
        public float groundReflectivity = 0.3f;
        @Comment("Sound reflectivity for foliage blocks (leaves, grass, etc.).\n0.0 - 1.0")
        public float foliageReflectivity = 0.5f;
        @Comment("Sound reflectivity for metal blocks.\n0.0 - 1.0")
        public float metalReflectivity = 1.0f;
        @Comment("Sound reflectivity for glass blocks.\n0.0 - 1.0")
        public float glassReflectivity = 0.5f;
        @Comment("Sound reflectivity for cloth blocks (carpet, wool, etc).\n0.0 - 1.0")
        public float clothReflectivity = 0.05f;
        @Comment("Sound reflectivity for sand blocks.\n0.0 - 1.0")
        public float sandReflectivity = 0.2f;
        @Comment("Sound reflectivity for snow blocks.\n0.0 - 1.0")
        public float snowReflectivity = 0.2f;
    }

    public static class Vlads_Tweaks {
        @Comment("If sound hits non-full-square side, direct block occlusion is multiplied by this.\n0.0 - ")
        public float leakyBlocksOcclusionMultiplier = 0.15f;
        @Comment("The amount at which this is capped. 10 * block_occlusion is the theoretical limit")
        public float maxDirectOcclusionFromBlocks = 10f;
        @Comment("Calculate direct occlusion as the minimum of 9 rays from vertices of a block")
        public boolean _9RayDirectOcclusion = true;
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
    }

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
    @Comment("Soft presets (preserve some settings). Press reloadReverb to apply. Presets: [DEFAULT, RESET_MATERIALS]. (LOAD_SUCCESS = null)")
    public ConfigPresets preset = ConfigPresets.LOAD_SUCCESS;

}