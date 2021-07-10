package com.sonicether.soundphysics.mixin;

import com.sonicether.soundphysics.config.ConfigManager;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @ModifyArg(method = {"playSound","playSoundFromEntity"}, at = @At(value = "INVOKE", target = "net/minecraft/server/PlayerManager.sendToAround (Lnet/minecraft/entity/player/PlayerEntity;DDDDLnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/network/Packet;)V"),index = 4)
    private double SoundDistanceModifierInjector(double d){
        return d * ConfigManager.getConfig().General.soundDistanceAllowance;
    }

}
