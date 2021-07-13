package com.sonicether.soundphysics.mixin;

import com.sonicether.soundphysics.SoundPhysics;
import net.minecraft.client.sound.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SoundSystem.class)
public class SoundSystemMixin {

    @Inject(method = "start", at = @At(value = "INVOKE", target = "net/minecraft/client/sound/SoundListener.init ()V"))
    private void SoundPhysicsInitInjector(CallbackInfo ci){
            SoundPhysics.init();
    }

    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At(value = "FIELD", target = "net/minecraft/client/sound/SoundSystem.sounds : Lcom/google/common/collect/Multimap;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void SoundInfoStealer(SoundInstance sound, CallbackInfo ci, WeightedSoundSet weightedSoundSet, Identifier identifier, Sound sound2, float f, float g, SoundCategory soundCategory){
        SoundPhysics.setLastSoundCategoryAndName(soundCategory, sound.getId().getPath());
    }

    @ModifyArg(method = "getAdjustedVolume", at = @At(value = "INVOKE", target = "net/minecraft/util/math/MathHelper.clamp (FFF)F"), index = 0)
    private float VolumeMultiplierInjector(float vol){
        return vol * SoundPhysics.globalVolumeMultiplier;
    }

}
