package com.sonicether.soundphysics;

import net.minecraft.util.math.Vec3d;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.EXTEfx;
import static com.sonicether.soundphysics.SoundPhysics.pC;

import static com.sonicether.soundphysics.SPLog.*;

/*
                                        !!!Documentation for OpenAL!!!
                * I am not responsible for anything that happens after you go to these links *
    - ExtEfx(aka Effects Extension) https://github.com/rtpHarry/Sokoban/blob/master/libraries/OpenAL%201.1%20SDK/docs/Effects%20Extension%20Guide.pdf or https://usermanual.wiki/Pdf/Effects20Extension20Guide.90272296/view
    - Core spec(aka OpenAL 1.1 Specification and Reference) https://www.openal.org/documentation/openal-1.1-specification.pdf
    - Core guide(aka OpenAL Programmer's Guide) http://openal.org/documentation/OpenAL_Programmers_Guide.pdf
 */

public class SPEfx {

    private static final ReverbSlot slot1 = new ReverbSlot(0.15f , 0.0f, 1.0f, 2, 0.99f, 0.8571429f, 2.5f, 0.001f, 1.26f, 0.011f, 0.994f, 0.16f);
    private static final ReverbSlot slot2 = new ReverbSlot(0.55f , 0.0f, 1.0f, 3, 0.99f, 1         , 0.2f, 0.015f, 1.26f, 0.011f, 0.994f, 0.15f);
    private static final ReverbSlot slot3 = new ReverbSlot(1.68f , 0.1f, 1.0f, 5, 0.99f, 1         , 0.0f, 0.021f, 1.26f, 0.021f, 0.994f, 0.13f);
    private static final ReverbSlot slot4 = new ReverbSlot(4.142f, 0.5f, 1.0f, 4, 0.89f, 1         , 0.0f, 0.025f, 1.26f, 0.021f, 0.994f, 0.11f);
    private static int directFilter0;

    public static void syncReverbParams()
    {
        if (slot1.initialised)
        {
            //Set the global reverb parameters and apply them to the effect and effectslot
            slot1.set();
            slot2.set();
            slot3.set();
            slot4.set();
        }
    }

    static void setupEFX()
    {
        //Get current context and device
        final long currentContext = ALC10.alcGetCurrentContext();
        final long currentDevice = ALC10.alcGetContextsDevice(currentContext);
        if (ALC10.alcIsExtensionPresent(currentDevice, "ALC_EXT_EFX")) {
            log("EFX Extension recognized.");
        } else {
            logError("EFX Extension not found on current device. Aborting.");
            return;
        }
        // Delete previous filter if it was there
        if (slot1.initialised) EXTEfx.alDeleteFilters(directFilter0);

        // Create auxiliary effect slots
        slot1.initialize();
        slot2.initialize();
        slot3.initialize();
        slot4.initialize();

        // Create filters
        directFilter0 = EXTEfx.alGenFilters();
        EXTEfx.alFilteri(directFilter0, EXTEfx.AL_FILTER_TYPE, EXTEfx.AL_FILTER_LOWPASS);
        logGeneral("directFilter0: "+directFilter0);
    }

    protected static void setEnvironment(final int sourceID,
                                         final float sendGain0,   final float sendGain1,   final float sendGain2,   final float sendGain3,
                                         final float sendCutoff0, final float sendCutoff1, final float sendCutoff2, final float sendCutoff3,
                                         final float directCutoff, final float directGain)
    {
        if (pC.off) return;
        // Set reverb send filter values and set source to send to all reverb fx slots
        slot1.applyFilter(sourceID, sendGain0, sendCutoff0);
        slot2.applyFilter(sourceID, sendGain1, sendCutoff1);
        slot3.applyFilter(sourceID, sendGain2, sendCutoff2);
        slot4.applyFilter(sourceID, sendGain3, sendCutoff3);

        EXTEfx.alFilterf(directFilter0, EXTEfx.AL_LOWPASS_GAIN, directGain);
        EXTEfx.alFilterf(directFilter0, EXTEfx.AL_LOWPASS_GAINHF, directCutoff);
        AL10.alSourcei(sourceID, EXTEfx.AL_DIRECT_FILTER, directFilter0);
        checkErrorLog("Set Environment directFilter0:");

        AL10.alSourcef(sourceID, EXTEfx.AL_AIR_ABSORPTION_FACTOR, pC.airAbsorption);
        checkErrorLog("Set Environment airAbsorption:");
    }

    public static void setSoundPos(final int sourceID, final Vec3d pos) {
        if (pC.off) return;
        //System.out.println(pos);//TO DO
        AL10.alSourcefv(sourceID, 4100, new float[]{(float) pos.x, (float) pos.y, (float) pos.z});
    }

}
