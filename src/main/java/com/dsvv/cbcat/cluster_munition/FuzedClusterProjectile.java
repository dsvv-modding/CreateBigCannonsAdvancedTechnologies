package com.dsvv.cbcat.cluster_munition;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractFuzedHeavyAutocannonProjectile;
import com.dsvv.cbcat.registry.BlockRegister;
import com.dsvv.cbcat.registry.ExtraDataRegister;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public class FuzedClusterProjectile extends FuzedBigCannonProjectile {

    protected ItemStack[] secondaryFuzes = new ItemStack[] {ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY};
    protected String projectile = "";

    public FuzedClusterProjectile(EntityType<? extends FuzedBigCannonProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected void detonate(Position position) {
        spawnClusterParts(ExtraDataRegister.clusterParts(projectile));
    }

    @Override
    protected @NotNull BigCannonFuzePropertiesComponent getFuzeProperties() {
        return new BigCannonFuzePropertiesComponent(false);
    }

    @Override
    public BlockState getRenderedBlockState() {
        return BlockRegister.CLUSTER_BLOCK.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
    }

    @Override
    protected @NotNull BigCannonProjectilePropertiesComponent getBigCannonProjectileProperties() {
        return new BigCannonProjectilePropertiesComponent(
                0,
                1,
                true,
                1
        );
    }

    @Override
    public @NotNull EntityDamagePropertiesComponent getDamageProperties() {
        return new EntityDamagePropertiesComponent(
                28,
                false,
                true,
                false,
                2.5f
        );
    }

    @Override
    protected @NotNull BallisticPropertiesComponent getBallisticProperties() {
        return new BallisticPropertiesComponent(
                -0.05,
                0.02,
                false,
                1.75f,
                1,
                1,
                0.7f
        );
    }

    protected void spawnClusterParts(EntityEntry<? extends AbstractFuzedHeavyAutocannonProjectile> projectiles) {
        if (projectiles == null)
            return;
        Vec3[] directions = new Vec3[secondaryFuzes.length];
        for (int i = 0; i < directions.length; i++)
            directions[i] = this.orientation.normalize().scale(2).add(i % 2 == 0 ? 1 : -1, 0, i % 4 > 1 ? 1 : -1);

        for (int i = 0; i < directions.length; i++) {
            AbstractFuzedHeavyAutocannonProjectile entity = projectiles.create(level());
            entity.setPos(position());
            Vec3 direction = directions[i];
            entity.shoot(direction.x, direction.y, direction.z, (float) getDeltaMovement().length(), 35f);
            entity.setTracer(false);
            entity.setFuze(secondaryFuzes[i]);
            entity.setLifetime(50);
            level().addFreshEntity(entity);
        }
    }

    public void setSecondaryFuze(ItemStack[] fuzes) {
        secondaryFuzes = fuzes;
    }
    public void setProjectile(String projectile) {
        this.projectile = projectile;
    }
}
