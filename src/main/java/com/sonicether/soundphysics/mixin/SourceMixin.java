package com.sonicether.soundphysics.mixin;

import com.sonicether.soundphysics.config.ConfigManager;
import com.sonicether.soundphysics.SoundPhysics;
import net.minecraft.client.sound.Source;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Source.class)
public class SourceMixin {

    @Shadow
    @Final
    private int pointer;

    private Vec3d pos;

    @Inject(method = "setPosition", at = @At("HEAD"))
    private void SoundPosStealer(Vec3d poss, CallbackInfo ci) {
        this.pos = poss;
    }

    @Inject(method = "play", at = @At("HEAD"))
    private void OnPlaySoundInjector(CallbackInfo ci) {
        SoundPhysics.onPlaySound(pos.x, pos.y, pos.z, pointer);
        SoundPhysics.checkErrorLog("onplayinjector");
    }
    @ModifyArg(method = "setAttenuation", at = @At(value = "INVOKE", target = "org/lwjgl/openal/AL10.alSourcef (IIF)V", ordinal = 0, remap = false), index = 2)
    private float AttenuationHijack(int pointer2, int param_id, float attenuation) {
        if (param_id != 4131) throw new IllegalArgumentException("Tried modifying wrong field. No attenuation here.");
        return  attenuation / (float)(ConfigManager.getConfig().General.attenuationFactor);
    }

}
