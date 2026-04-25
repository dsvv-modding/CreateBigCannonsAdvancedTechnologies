package com.dsvv.cbcat.cannon.medium_rocketpod.munitions;

import com.dsvv.cbcat.base.IAbstractAutocannonProjectileMixin;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectile;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.core.Position;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

import java.util.function.Predicate;

public abstract class AbstractMediumFuzedRocket<T extends AbstractHeavyAutocannonProjectile> extends AbstractMediumRocket<T> {

    protected ItemStack fuze = ItemStack.EMPTY;

    protected AbstractMediumFuzedRocket(EntityType<? extends AbstractMediumRocket> type, Level level, double thrust, double maxVelocity, EntityEntry<T> autocannonProjectileEntry) {
        super(type, level, thrust, maxVelocity, autocannonProjectileEntry);
    }

    protected void detonate(Position position) {
        if (projectile == null && autocannonProjectileEntry != null)
            projectile = autocannonProjectileEntry.create(level());
        if (projectile != null)
            ((IAbstractAutocannonProjectileMixin)projectile).detonateProjectile(position);
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

    public void setFuze(ItemStack fuze) { this.fuze = fuze.copy(); }

    public ItemStack getFuze() { return fuze.getItem() instanceof FuzeItem ? fuze.copy() : ItemStack.EMPTY; }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.fuze != null && !this.fuze.isEmpty()) tag.put("Fuze", this.fuze.save(level().registryAccess()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.fuze = tag.contains("Fuze", Tag.TAG_COMPOUND) ? ItemStack.parseOptional(level().registryAccess(), tag.getCompound("Fuze")) : ItemStack.EMPTY;
    }

    protected final boolean canDetonate(Predicate<FuzeItem> cons) {
        return !this.level().isClientSide && this.level().hasChunkAt(this.blockPosition()) && !this.isRemoved()
                && this.fuze.getItem() instanceof FuzeItem fuzeItem && cons.test(fuzeItem);
    }
}
