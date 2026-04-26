package com.dsvv.cbcat.mixin;

import com.dsvv.cbcat.cannon.heavy_autocannon.breech.HeavyAutocannonBreechBlockEntity;
import com.dsvv.cbcat.cannon.heavy_autocannon.contraption.MountedHeavyAutocannonContraption;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box.HeavyAutocannonAmmoContainerItem;
import com.dsvv.cbcat.cannon.twin_autocannon.TwinAutocannonBreechBlockEntity;
import com.dsvv.cbcat.cannon.twin_autocannon.contraption.MountedTwinAutocannonContraption;
import com.simibubi.create.content.kinetics.mechanicalArm.AllArmInteractionPointTypes;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.CannonMountPoint;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerItem;

@Mixin(CannonMountPoint.class)
public abstract class CannonMountPointMixin extends AllArmInteractionPointTypes.DepositOnlyArmInteractionPoint{

    public CannonMountPointMixin(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
        super(type, level, pos, state);
    }

    @Inject(method = "getInsertedResultAndDoSomething(Lnet/minecraft/world/item/ItemStack;ZLrbasamoyai/createbigcannons/cannon_control/contraption/AbstractMountedCannonContraption;Lrbasamoyai/createbigcannons/cannon_control/contraption/PitchOrientedContraptionEntity;)Lnet/minecraft/world/item/ItemStack;",
    at = @At("HEAD"), cancellable = true, remap = false)
    public void insertIntoNewAutocannon(ItemStack stack, boolean simulate, AbstractMountedCannonContraption cannon, PitchOrientedContraptionEntity poce, CallbackInfoReturnable<ItemStack> cir) {
        if (poce.getContraption() instanceof MountedTwinAutocannonContraption twinAutocannon)
            cir.setReturnValue(twinAutocannonInsert(stack, simulate, twinAutocannon, poce));
        if (poce.getContraption() instanceof MountedHeavyAutocannonContraption heavyAutocannon)
            cir.setReturnValue(heavyAutocannonInsert(stack, simulate, heavyAutocannon, poce));
    }

    private static ItemStack twinAutocannonInsert(ItemStack stack, boolean simulate, MountedTwinAutocannonContraption autocannon, PitchOrientedContraptionEntity poce) {
        if (!(stack.getItem() instanceof AutocannonAmmoContainerItem)) return stack;
        BlockEntity be = autocannon.presentBlockEntities.get(autocannon.getStartPos());
        if (!(be instanceof TwinAutocannonBreechBlockEntity breech)) return stack;

        ItemStack oldContainer = breech.getMagazine();
        if (oldContainer.getItem() instanceof AutocannonAmmoContainerItem
                && AutocannonAmmoContainerItem.getTotalAmmoCount(oldContainer) > 0) return stack;

        if (simulate) return ItemStack.EMPTY;
        breech.setMagazine(stack);
        return oldContainer.isEmpty() ? ItemStack.EMPTY : oldContainer.copy();
    }

    private static ItemStack heavyAutocannonInsert(ItemStack stack, boolean simulate, MountedHeavyAutocannonContraption autocannon, PitchOrientedContraptionEntity poce) {
        if (!(stack.getItem() instanceof HeavyAutocannonAmmoContainerItem)) return stack;
        BlockEntity be = autocannon.presentBlockEntities.get(autocannon.getStartPos());
        if (!(be instanceof HeavyAutocannonBreechBlockEntity breech)) return stack;

        ItemStack oldContainer = breech.getMagazine();
        if (oldContainer.getItem() instanceof HeavyAutocannonAmmoContainerItem
                && HeavyAutocannonAmmoContainerItem.getTotalAmmoCount(oldContainer) > 0) return stack;

        if (simulate) return ItemStack.EMPTY;
        breech.setMagazine(stack);
        return oldContainer.isEmpty() ? ItemStack.EMPTY : oldContainer.copy();
    }
}
