package com.dsvv.cbcat.mixin;

import com.dsvv.cbcat.base.ICarriageAdjustableFireRate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rbasamoyai.createbigcannons.base.goggles.IHaveEntityGoggleInformation;
import rbasamoyai.createbigcannons.cannon_control.ControlPitchContraption;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

@Mixin(CannonCarriageEntity.class)
public abstract class CannonCarriageEntityMixin extends Entity implements ControlPitchContraption, IHaveEntityGoggleInformation
{
    @Shadow (remap = false)
    private PitchOrientedContraptionEntity cannonContraption;

    public CannonCarriageEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "Lrbasamoyai/createbigcannons/cannon_control/carriage/CannonCarriageEntity;trySettingFireRateCarriage(I)V", at = @At("TAIL"), remap = false)
    public void trySettingFireRateCarriage(int fireRateAdjustment, CallbackInfo ci) {
        if (!this.level().isClientSide && this.cannonContraption != null && this.cannonContraption.getContraption() instanceof ICarriageAdjustableFireRate autocannon)
            autocannon.trySettingFireRateCarriage(fireRateAdjustment);
    }
}
