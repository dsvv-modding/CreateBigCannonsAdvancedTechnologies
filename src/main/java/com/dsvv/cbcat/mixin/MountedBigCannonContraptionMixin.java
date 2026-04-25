package com.dsvv.cbcat.mixin;

import com.dsvv.cbcat.base.CustomPropellantContext;
import com.dsvv.cbcat.base.IBigCannonBlockPhysics;
import com.dsvv.cbcat.cartridge.IProjectileCartridgeBlock;
import com.dsvv.cbcat.config.CBCATConfigs;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterial;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.effects.particles.explosions.CannonBlastWaveEffectParticleData;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCannonPropellantBlock;

import java.util.ArrayList;
import java.util.List;

@Mixin(MountedBigCannonContraption.class)
public abstract class MountedBigCannonContraptionMixin extends AbstractMountedCannonContraption
{
    @Shadow(remap = false)
    private BigCannonMaterial cannonMaterial;
    @Shadow(remap = false)
    public boolean hasFired;

    @Shadow(remap = false) public abstract void fail(BlockPos localPos, Level level, PitchOrientedContraptionEntity entity, @Nullable BlockEntity failed, int charges);

    @Unique
    private CustomPropellantContext propelCtx;

    @Inject(method = "fireShot(Lnet/minecraft/server/level/ServerLevel;Lrbasamoyai/createbigcannons/cannon_control/contraption/PitchOrientedContraptionEntity;)V",
        at = @At("HEAD"), remap = false)
    public void createCustomPropellantContext(ServerLevel level, PitchOrientedContraptionEntity entity, CallbackInfo ci) {
        propelCtx = new CustomPropellantContext();
        boolean allowsMultipleCharges = true;

        BlockPos currentPos = this.startPos.immutable();
        List<StructureBlockInfo> projectileBlocks = new ArrayList<>();
        AbstractBigCannonProjectile projectile = null;

        while (this.presentBlockEntities.get(currentPos) instanceof IBigCannonBlockEntity cbe) {
            BigCannonBehavior behavior = cbe.cannonBehavior();
            StructureBlockInfo containedBlockInfo = behavior.block();
            StructureBlockInfo cannonInfo = this.blocks.get(currentPos);
            if (cannonInfo == null) break;

            Block block = containedBlockInfo.state().getBlock();

            if (containedBlockInfo.state().isAir()) {
            } else if (block instanceof IProjectileCartridgeBlock projCart) {
                allowsMultipleCharges = projCart.allowsMultipleCharges();
                if(!allowsMultipleCharges && propelCtx.chargesUsed > 0)
                    propelCtx.isDoomedToFail();
                propelCtx.addPropellant(projCart, containedBlockInfo, initialOrientation);
                projectileBlocks.add(containedBlockInfo);
                projectile = projCart.getProjectile(level, projectileBlocks);
                propelCtx.chargesUsed += projectile.addedChargePower();
            } else if (block instanceof BigCannonPropellantBlock cpropel && !(block instanceof ProjectileBlock)) {
                propelCtx.addPropellant(cpropel, containedBlockInfo, this.initialOrientation);
                if (!allowsMultipleCharges)
                    propelCtx.isDoomedToFail();
            } else if (block instanceof ProjectileBlock<?> projBlock && projectile == null) {
                projectileBlocks.add(containedBlockInfo);
                projectile = projBlock.getProjectile(level, projectileBlocks);
                propelCtx.chargesUsed += projectile.addedChargePower();
            }

            if(cannonInfo.state().getBlock() instanceof IBigCannonBlockPhysics cBlock)
                propelCtx.addBarrel(cBlock);

            currentPos = currentPos.relative(this.initialOrientation);
        }
    }
    @ModifyExpressionValue(method = "fireShot(Lnet/minecraft/server/level/ServerLevel;Lrbasamoyai/createbigcannons/cannon_control/contraption/PitchOrientedContraptionEntity;)V",
        at = @At(value = "FIELD", target = "Lrbasamoyai/createbigcannons/cannon_control/contraption/MountedBigCannonContraption$PropellantContext;chargesUsed:F", opcode = Opcodes.GETFIELD), remap = false)
    public float getPropellant(float original) {
        return !CBCATConfigs.SERVER.bigCannons.disablePhysicRework.get() ? propelCtx.chargesUsed : original;
    }

    @ModifyExpressionValue(method = "fireShot(Lnet/minecraft/server/level/ServerLevel;Lrbasamoyai/createbigcannons/cannon_control/contraption/PitchOrientedContraptionEntity;)V",
            at = @At(value = "FIELD", target = "Lrbasamoyai/createbigcannons/cannon_control/contraption/MountedBigCannonContraption$PropellantContext;recoil:F", opcode = Opcodes.GETFIELD), remap = false)
    public float getRecoil(float original) {
        return !CBCATConfigs.SERVER.bigCannons.disablePhysicRework.get() ? propelCtx.recoil : original;
    }

    @ModifyExpressionValue(method = "fireShot(Lnet/minecraft/server/level/ServerLevel;Lrbasamoyai/createbigcannons/cannon_control/contraption/PitchOrientedContraptionEntity;)V",
            at = @At(value = "FIELD", target = "Lrbasamoyai/createbigcannons/cannon_control/contraption/MountedBigCannonContraption$PropellantContext;smokeScale:F", opcode = Opcodes.GETFIELD), remap = false)
    public float getSmoke(float original) {
        return  !CBCATConfigs.SERVER.bigCannons.disablePhysicRework.get() ? propelCtx.smokeScale : original;
    }

    @ModifyExpressionValue(method = "fireShot(Lnet/minecraft/server/level/ServerLevel;Lrbasamoyai/createbigcannons/cannon_control/contraption/PitchOrientedContraptionEntity;)V",
            at = @At(value = "FIELD", target = "Lrbasamoyai/createbigcannons/cannon_control/contraption/MountedBigCannonContraption$PropellantContext;stress:F", opcode = Opcodes.GETFIELD), remap = false)
    public float getStress(float original) {
        return  !CBCATConfigs.SERVER.bigCannons.disablePhysicRework.get() ? propelCtx.stress : original;
    }

    @WrapOperation(method = "fireShot(Lnet/minecraft/server/level/ServerLevel;Lrbasamoyai/createbigcannons/cannon_control/contraption/PitchOrientedContraptionEntity;)V", at = @At(value = "INVOKE", target = "Lrbasamoyai/createbigcannons/munitions/big_cannon/AbstractBigCannonProjectile;setChargePower(F)V"), remap = false)
    public void setChargePower(AbstractBigCannonProjectile instance, float v, Operation<Void> original) {
        boolean rework = !CBCATConfigs.SERVER.bigCannons.disablePhysicRework.get();
        if (rework)
            original.call(instance, propelCtx.getVelocity());
        else
            original.call(instance, v);
    }

    @WrapOperation(method = "fireShot(Lnet/minecraft/server/level/ServerLevel;Lrbasamoyai/createbigcannons/cannon_control/contraption/PitchOrientedContraptionEntity;)V", at = @At(value = "INVOKE", target = "Lrbasamoyai/createbigcannons/munitions/big_cannon/AbstractBigCannonProjectile;shoot(DDDFF)V"), remap = false)
    public void shoot(AbstractBigCannonProjectile instance, double x, double y, double z, float velocity, float spread, Operation<Void> original) {
        boolean rework = !CBCATConfigs.SERVER.bigCannons.disablePhysicRework.get();
        if (propelCtx.getDoomedToFail())
            fail(startPos, instance.level(), (PitchOrientedContraptionEntity) entity, null, (int) propelCtx.chargesUsed);
        if (rework)
            original.call(instance, x, y, z, propelCtx.getVelocity(), propelCtx.spread);
        else
            original.call(instance, x, y, z, velocity, spread);
    }

    @WrapOperation(method = "fireShot(Lnet/minecraft/server/level/ServerLevel;Lrbasamoyai/createbigcannons/cannon_control/contraption/PitchOrientedContraptionEntity;)V", at = @At(value = "NEW", target = "Lrbasamoyai/createbigcannons/effects/particles/explosions/CannonBlastWaveEffectParticleData;"), remap = false)
    public CannonBlastWaveEffectParticleData alternativeBlast(double blastRadius, Holder soundEvent, SoundSource soundSource, float volume, float pitch, float airAbsorption, float power, Operation<CannonBlastWaveEffectParticleData> original) {
        boolean rework = !CBCATConfigs.SERVER.bigCannons.disablePhysicRework.get();
        if (rework) {
            return new CannonBlastWaveEffectParticleData(blastRadius, soundEvent, soundSource, volume * propelCtx.volume, pitch * propelCtx.volume, airAbsorption, power);
        }
        else {
            return new CannonBlastWaveEffectParticleData(blastRadius, soundEvent, soundSource, volume, pitch, airAbsorption, power);
        }
    }
}