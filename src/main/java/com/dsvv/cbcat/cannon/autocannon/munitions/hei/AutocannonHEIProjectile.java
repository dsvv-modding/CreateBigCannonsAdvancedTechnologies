package com.dsvv.cbcat.cannon.autocannon.munitions.hei;

import net.minecraft.core.Position;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;
import rbasamoyai.createbigcannons.munitions.ShellExplosion;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.flak.FlakAutocannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class AutocannonHEIProjectile extends AbstractAutocannonProjectile
{
    private ItemStack fuze = ItemStack.EMPTY;

    public AutocannonHEIProjectile(EntityType<? extends AbstractAutocannonProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    @Nonnull
    public EntityDamagePropertiesComponent getDamageProperties() {
        return new EntityDamagePropertiesComponent(
                9,
                false,
                true,
                false,
                1
        );
    }

    @Override
    @Nonnull
    protected BallisticPropertiesComponent getBallisticProperties() {
        return new BallisticPropertiesComponent(
                -0.025,
                0.01,
                false,
                1f,
                1f,
                0.4f,
                0.7f
        );
    }

    @Override
    public void tick() {
        super.tick();
        if (this.canDetonate(fz -> fz.onProjectileTick(this.fuze, this))) {
            this.detonate(this.position());
            this.removeNextTick = true;
        }
    }

    @Override
    protected void expireProjectile() {
        if (this.fuze.getItem() instanceof FuzeItem fuzeItem && fuzeItem.onProjectileExpiry(this.fuze, this))
            this.detonate(this.position());
        super.expireProjectile();
    }

    @Override
    protected boolean onImpact(HitResult hitResult, ImpactResult impactResult, ProjectileContext projectileContext) {
        super.onImpact(hitResult, impactResult, projectileContext);
        if (this.canDetonate(fz -> fz.onProjectileImpact(this.fuze, this, hitResult, impactResult, false))) {
            this.detonate(hitResult.getLocation());
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean onClip(ProjectileContext ctx, Vec3 start, Vec3 end) {
        if (super.onClip(ctx, start, end)) return true;
        if (this.canDetonate(fz -> fz.onProjectileClip(this.fuze, this, start, end, ctx, false))) {
            this.detonate(start);
            return true;
        }
        return false;
    }

    protected void detonate(Position position) {
        FlakAutocannonProjectileProperties properties = CBCMunitionPropertiesHandlers.FLAK_AUTOCANNON.getPropertiesOf(CBCEntityTypes.FLAK_AUTOCANNON.get());
        ShellExplosion explosion = new ShellExplosion(this.level(), this, this.indirectArtilleryFire(false), position.x(),
                position.y(), position.z(), properties.explosion().explosivePower(), true,
                CBCConfigs.server().munitions.damageRestriction.get().explosiveInteraction());
        CreateBigCannons.handleCustomExplosion(this.level(), explosion);
    }

    public void setFuze(ItemStack fuze) { this.fuze = fuze; }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.fuze != null && !this.fuze.isEmpty()) tag.put("Fuze", this.fuze.saveOptional(this.level().registryAccess()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.fuze = tag.contains("Fuze", Tag.TAG_COMPOUND) ? ItemStack.parseOptional(this.level().registryAccess(), tag.getCompound("Fuze")) : ItemStack.EMPTY;
    }

    protected final boolean canDetonate(Predicate<FuzeItem> cons) {
        return !this.level().isClientSide && this.level().hasChunkAt(this.blockPosition()) && !this.isRemoved()
                && this.fuze.getItem() instanceof FuzeItem fuzeItem && cons.test(fuzeItem);
    }
}
