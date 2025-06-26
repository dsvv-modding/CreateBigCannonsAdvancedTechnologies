package com.dsvv.cbcat.cluster_munition;

import com.dsvv.cbcat.registry.BlockEntityRegister;
import com.dsvv.cbcat.registry.BlockRegister;
import com.dsvv.cbcat.registry.EntityRegister;
import com.dsvv.cbcat.registry.ExtraDataRegister;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedProjectileBlock;

import java.util.List;

import static com.dsvv.cbcat.debugUtils.DebugUtils.displayCustomClientMessage;

public class FuzedClusterProjectileBlock extends FuzedProjectileBlock<FuzedClusterProjectileBlockEntity, FuzedClusterProjectile> {

    public FuzedClusterProjectileBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isBaseFuze() {
        return false;
    }

    @Override
    public EntityType getAssociatedEntityType() {
        return EntityRegister.CLUSTER_PROJECTILE.get();
    }

    @Override
    public Class getBlockEntityClass() {
        return FuzedClusterProjectileBlockEntity.class;
    }

    @Override
    public BlockEntityType getBlockEntityType() {
        return BlockEntityRegister.FUZED_CLUSTER_PROJECTILE_BLOCK_ENTITY.get();
    }

    @Override
    public AbstractBigCannonProjectile getProjectile(Level level, List projectileBlocks) {
        FuzedClusterProjectile projectile = (FuzedClusterProjectile) super.getProjectile(level, projectileBlocks);
        projectile.setSecondaryFuze(getSecondaryFuzesFromBlocks(projectileBlocks));
        projectile.setProjectile(getProjectileFromBlocks(projectileBlocks));
        return projectile;
    }

    @Override
    public AbstractBigCannonProjectile getProjectile(Level level, ItemStack itemStack) {
        FuzedClusterProjectile result = (FuzedClusterProjectile) super.getProjectile(level, itemStack);
        ItemStack[] fuzes = ((FuzedClusterProjectileBlockItem) itemStack.getItem()).getFuzesFromStack(itemStack);
        String projectile = ((FuzedClusterProjectileBlockItem) itemStack.getItem()).getProjectileFromStack(itemStack);
        result.setSecondaryFuze(fuzes);
        result.setProjectile(projectile);
        return result;
    }

    @Override
    public AbstractBigCannonProjectile getProjectile(Level level, BlockPos pos, BlockState state) {
        FuzedClusterProjectile result = (FuzedClusterProjectile) super.getProjectile(level, pos, state);
        FuzedClusterProjectileBlockEntity clusterBE = (FuzedClusterProjectileBlockEntity)level.getBlockEntity(pos);
        ItemStack[] fuzes = clusterBE.getFuzes();
        String projectile = clusterBE.getProjectile();
        result.setSecondaryFuze(fuzes);
        result.setProjectile(projectile);
        return result;
    }

    @Override
    public StructureBlockInfo getHandloadingInfo(ItemStack stack, BlockPos localPos, Direction cannonOrientation) {
        BlockState state = this.defaultBlockState().setValue(FACING, cannonOrientation);
        CompoundTag baseTag = stack.getOrCreateTag();
        if (baseTag.contains("BlockEntityTag")) {
            CompoundTag tag = baseTag.getCompound("BlockEntityTag").copy();
            tag.remove("x");
            tag.remove("y");
            tag.remove("z");
            return new StructureBlockInfo(localPos, state, tag);
        }
        return new StructureBlockInfo(localPos, state, baseTag);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack result = super.getCloneItemStack(level, pos, state);
        if (!(level.getBlockEntity(pos) instanceof FuzedClusterProjectileBlockEntity clusterBE))
            return result;
        CompoundTag baseTag = result.getOrCreateTag();
        if (!baseTag.contains("BlockEntityTag"))
            baseTag.put("BlockEntityTag", new CompoundTag());
        CompoundTag tag = baseTag.getCompound("BlockEntityTag");
        String projectile = clusterBE.getProjectile();
        ItemStack[] fuzes = clusterBE.getFuzes();
        ListTag fuzeTags = new ListTag();
        for (int i = 0; i < fuzes.length; i++) {
            fuzeTags.add(fuzes[i].save(new CompoundTag()));
            //displayCustomClientMessage(fuzes[i].toString());
        }
        tag.put("SecondaryFuzes", fuzeTags);
        tag.putString("Projectile", projectile);
        return result;
    }

    protected static ItemStack[] getSecondaryFuzesFromBlocks(List<StructureBlockInfo> blocks) {
        if (blocks.isEmpty()) return new ItemStack[]{ ItemStack.EMPTY };
        StructureBlockInfo info = blocks.get(0);
        if (info.nbt() == null) return new ItemStack[]{ ItemStack.EMPTY };
        BlockEntity load = BlockEntity.loadStatic(info.pos(), info.state(), info.nbt());
        return load instanceof FuzedClusterProjectileBlockEntity clusterBE ? clusterBE.getFuzes() : new ItemStack[]{ ItemStack.EMPTY };
    }

    protected static String getProjectileFromBlocks(List<StructureBlockInfo> blocks) {
        if (blocks.isEmpty()) return "";
        StructureBlockInfo info = blocks.get(0);
        if (info.nbt() == null) return "";
        BlockEntity load = BlockEntity.loadStatic(info.pos(), info.state(), info.nbt());
        return load instanceof FuzedClusterProjectileBlockEntity clusterBE ? clusterBE.getProjectile() : "";
    }

    public static ItemStack getHighExplosiveWithImpactFuze() {
        ListTag fuzes = new ListTag();
        CompoundTag fuze = CBCItems.IMPACT_FUZE.asStack().save(new CompoundTag());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        ItemStack cluster = BlockRegister.CLUSTER_BLOCK.asStack();
        CompoundTag baseTag = cluster.getOrCreateTag();
        if (!baseTag.contains("BlockEntityTag"))
            baseTag.put("BlockEntityTag", new CompoundTag());
        CompoundTag tag = baseTag.getCompound("BlockEntityTag");
        if (tag.contains("SecondaryFuzes"))
            tag.remove("SecondaryFuzes");
        tag.put("SecondaryFuzes", fuzes);
        tag.putString("Projectile", ExtraDataRegister.clusterPartsReverse(EntityRegister.HA_HE_PROJECTILE.get()));
        return cluster;
    }

    public static ItemStack getHighExplosiveFragmentationWithImpactFuze() {
        ListTag fuzes = new ListTag();
        CompoundTag fuze = CBCItems.IMPACT_FUZE.asStack().save(new CompoundTag());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        ItemStack cluster = BlockRegister.CLUSTER_BLOCK.asStack();
        CompoundTag baseTag = cluster.getOrCreateTag();
        if (!baseTag.contains("BlockEntityTag"))
            baseTag.put("BlockEntityTag", new CompoundTag());
        CompoundTag tag = baseTag.getCompound("BlockEntityTag");
        if (tag.contains("SecondaryFuzes"))
            tag.remove("SecondaryFuzes");
        tag.put("SecondaryFuzes", fuzes);
        tag.putString("Projectile", ExtraDataRegister.clusterPartsReverse(EntityRegister.HA_HEF_PROJECTILE.get()));
        return cluster;
    }

    public static ItemStack getHighExplosiveAntiTankWithImpactFuze() {
        ListTag fuzes = new ListTag();
        CompoundTag fuze = CBCItems.IMPACT_FUZE.asStack().save(new CompoundTag());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        ItemStack cluster = BlockRegister.CLUSTER_BLOCK.asStack();
        CompoundTag baseTag = cluster.getOrCreateTag();
        if (!baseTag.contains("BlockEntityTag"))
            baseTag.put("BlockEntityTag", new CompoundTag());
        CompoundTag tag = baseTag.getCompound("BlockEntityTag");
        if (tag.contains("SecondaryFuzes"))
            tag.remove("SecondaryFuzes");
        tag.put("SecondaryFuzes", fuzes);
        tag.putString("Projectile", ExtraDataRegister.clusterPartsReverse(EntityRegister.HA_HEAT_PROJECTILE.get()));
        return cluster;
    }

    public static ItemStack getSmokeWithImpactFuze() {
        ListTag fuzes = new ListTag();
        CompoundTag fuze = CBCItems.IMPACT_FUZE.asStack().save(new CompoundTag());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        ItemStack cluster = BlockRegister.CLUSTER_BLOCK.asStack();
        CompoundTag baseTag = cluster.getOrCreateTag();
        if (!baseTag.contains("BlockEntityTag"))
            baseTag.put("BlockEntityTag", new CompoundTag());
        CompoundTag tag = baseTag.getCompound("BlockEntityTag");
        if (tag.contains("SecondaryFuzes"))
            tag.remove("SecondaryFuzes");
        tag.put("SecondaryFuzes", fuzes);
        tag.putString("Projectile", ExtraDataRegister.clusterPartsReverse(EntityRegister.HA_SMOKE_PROJECTILE.get()));
        return cluster;
    }
}