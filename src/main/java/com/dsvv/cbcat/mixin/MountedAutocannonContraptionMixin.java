package com.dsvv.cbcat.mixin;

import com.dsvv.cbcat.cannon.autocannon.SpecialAutocannonBarrel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedAutocannonContraption;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlock;
import rbasamoyai.createbigcannons.utils.CBCUtils;

@Mixin(MountedAutocannonContraption.class)
public abstract class MountedAutocannonContraptionMixin extends AbstractMountedCannonContraption
{
    @Unique
    private float volumeMultiplier = 1;

    @Redirect( method = "collectCannonBlocks(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z", at = @At( value = "INVOKE",
    target = "Lrbasamoyai/createbigcannons/cannons/autocannon/AutocannonBlock;isComplete(Lnet/minecraft/world/level/block/state/BlockState;)Z"), remap = false)
    public boolean isCompleteRewrite(AutocannonBlock barrel, BlockState state, Level level, BlockPos pos) {
        if (barrel instanceof SpecialAutocannonBarrel specialBarrel)
            volumeMultiplier *= specialBarrel.getVolumeMultiplier();
        return barrel.isComplete(state);
    }

    @Redirect( method = "fireShot(Lnet/minecraft/server/level/ServerLevel;Lrbasamoyai/createbigcannons/cannon_control/contraption/PitchOrientedContraptionEntity;)V",
        at = @At( value = "INVOKE", target = "Lrbasamoyai/createbigcannons/utils/CBCUtils;playBlastLikeSoundOnServer(Lnet/minecraft/server/level/ServerLevel;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFF)V"), remap = false)
    public void playSoundRedirect(ServerLevel level, double x, double y, double z, SoundEvent event, SoundSource source, float volume, float pitch, float airAbsorption) {
        CBCUtils.playBlastLikeSoundOnServer(level, x, y, z, event, source, volume * volumeMultiplier, pitch * volumeMultiplier, airAbsorption);
    }

    @Inject(method = "writeNBT(Lnet/minecraft/core/HolderLookup$Provider;Z)Lnet/minecraft/nbt/CompoundTag;", at = @At("TAIL"), remap = false)
    public void writeAdditionalNBT(HolderLookup.Provider registries, boolean spawnPacket, CallbackInfoReturnable<CompoundTag> cir) {
        cir.getReturnValue().putFloat("volumeMultiplier", volumeMultiplier);
    }

    @Inject(method = "readNBT(Lnet/minecraft/world/level/Level;Lnet/minecraft/nbt/CompoundTag;Z)V", at = @At("TAIL"), remap = false)
    public void readAdditionalNBT(Level level, CompoundTag tag, boolean clientData, CallbackInfo ci) {
        volumeMultiplier = tag.getFloat("volumeMultiplier");
    }
}