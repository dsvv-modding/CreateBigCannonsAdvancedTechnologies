package com.dsvv.cbcat.cluster_munition;

import com.dsvv.cbcat.registry.*;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedProjectileBlock;

import java.util.ArrayList;
import java.util.List;

public class FuzedClusterProjectileBlock extends FuzedProjectileBlock<FuzedClusterProjectileBlockEntity, FuzedClusterProjectile> {

    public FuzedClusterProjectileBlock(Properties properties) {
        super(properties);
        this.codec = simpleCodec(this::fromSelf);
    }

    private final MapCodec<? extends DirectionalBlock> codec;

    private FuzedClusterProjectileBlock fromSelf(Properties properties) {
        return new FuzedClusterProjectileBlock(properties);
    }

    @Override protected MapCodec<? extends DirectionalBlock> codec() { return this.codec; }

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
        projectile.setSecondaryFuze(getSecondaryFuzesFromBlocks(projectileBlocks, level.registryAccess()));
        projectile.setProjectile(getProjectileFromBlocks(projectileBlocks, level.registryAccess()));
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
        ItemStack[] fuzes = clusterBE.getSecondaryFuzes();
        String projectile = clusterBE.getProjectile();
        result.setSecondaryFuze(fuzes);
        result.setProjectile(projectile);
        return result;
    }

    /*@Override
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
    }*/

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        ItemStack result = super.getCloneItemStack(level, pos, state);
        if (!(level.getBlockEntity(pos) instanceof FuzedClusterProjectileBlockEntity clusterBE))
            return result;
        if (clusterBE.getProjectile() != null)
            result.set(DataComponentRegistry.CLUSTER_PROJECTILE, clusterBE.getProjectile());
        if (!clusterBE.getFuze().isEmpty())
            result.set(CBCDataComponents.FUZE, ItemContainerContents.fromItems(Lists.newArrayList(clusterBE.getFuze())));
        if (clusterBE.getSecondaryFuzes().length > 0)
            result.set(DataComponentRegistry.CLUSTER_FUZES, ItemContainerContents.fromItems(Lists.newArrayList(clusterBE.getSecondaryFuzes())));
        /*CompoundTag baseTag = result.getOrCreateTag();
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
        tag.putString("Projectile", projectile);*/
        return result;
    }

    protected static ItemStack[] getSecondaryFuzesFromBlocks(List<StructureBlockInfo> blocks, HolderLookup.Provider registries) {
        if (blocks.isEmpty()) return new ItemStack[]{ ItemStack.EMPTY };
        StructureBlockInfo info = blocks.get(0);
        if (info.nbt() == null) return new ItemStack[]{ ItemStack.EMPTY };
        Tag tag = info.nbt().getCompound("components").get("cbc_at:cluster_fuzes");
        ItemContainerContents contents = ItemContainerContents.CODEC
                .parse(registries.createSerializationContext(NbtOps.INSTANCE), tag)
                .resultOrPartial()
                .orElse(ItemContainerContents.EMPTY);
        if (contents.getSlots() <= 0)
            return new ItemStack[] { ItemStack.EMPTY };
        ItemStack[] resultStack = new ItemStack[contents.getSlots()];
        for (int i = 0; i < resultStack.length; i++)
            resultStack[i] = contents.getStackInSlot(i);
        return resultStack;
        //BlockEntity load = BlockEntity.loadStatic(info.pos(), info.state(), info.nbt(), level.registryAccess());
        //return load instanceof FuzedClusterProjectileBlockEntity clusterBE ? clusterBE.getFuzes() : new ItemStack[]{ ItemStack.EMPTY };
    }

    protected static String getProjectileFromBlocks(List<StructureBlockInfo> blocks, HolderLookup.Provider registries) {
        if (blocks.isEmpty()) return "";
        StructureBlockInfo info = blocks.get(0);
        if (info.nbt() == null) return "";
        Tag tag = info.nbt().getCompound("components").get("cbc_at:cluster_projectile");
        return Codec.STRING
                .parse(registries.createSerializationContext(NbtOps.INSTANCE), tag)
                .resultOrPartial()
                .orElse("");
    }

    public static ItemStack getHighExplosiveWithImpactFuze() {
        ArrayList<ItemStack> fuzes = new ArrayList();
        ItemStack fuze = CBCItems.IMPACT_FUZE.asStack();
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        ItemStack stack = BlockRegister.CLUSTER_BLOCK.asStack();
        stack.set(DataComponentRegistry.CLUSTER_FUZES, ItemContainerContents.fromItems(fuzes));
        stack.set(DataComponentRegistry.CLUSTER_PROJECTILE, ExtraDataRegister.clusterPartsReverse(ItemRegister.HA_HE_ITEM.get()));
        return stack;
    }

    public static ItemStack getHighExplosiveFragmentationWithImpactFuze() {
        ArrayList<ItemStack> fuzes = new ArrayList();
        ItemStack fuze = CBCItems.IMPACT_FUZE.asStack();
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        ItemStack stack = BlockRegister.CLUSTER_BLOCK.asStack();
        stack.set(DataComponentRegistry.CLUSTER_FUZES, ItemContainerContents.fromItems(fuzes));
        stack.set(DataComponentRegistry.CLUSTER_PROJECTILE, ExtraDataRegister.clusterPartsReverse(ItemRegister.HA_HEF_ITEM.get()));
        return stack;
    }

    public static ItemStack getHighExplosiveAntiTankWithImpactFuze() {
        ArrayList<ItemStack> fuzes = new ArrayList();
        ItemStack fuze = CBCItems.IMPACT_FUZE.asStack();
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        ItemStack stack = BlockRegister.CLUSTER_BLOCK.asStack();
        stack.set(DataComponentRegistry.CLUSTER_FUZES, ItemContainerContents.fromItems(fuzes));
        stack.set(DataComponentRegistry.CLUSTER_PROJECTILE, ExtraDataRegister.clusterPartsReverse(ItemRegister.HA_HEAT_ITEM.get()));
        return stack;
    }

    public static ItemStack getSmokeWithImpactFuze() {
        ArrayList<ItemStack> fuzes = new ArrayList();
        ItemStack fuze = CBCItems.IMPACT_FUZE.asStack();
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        ItemStack stack = BlockRegister.CLUSTER_BLOCK.asStack();
        stack.set(DataComponentRegistry.CLUSTER_FUZES, ItemContainerContents.fromItems(fuzes));
        stack.set(DataComponentRegistry.CLUSTER_PROJECTILE, ExtraDataRegister.clusterPartsReverse(ItemRegister.HA_SMOKE_ITEM.get()));
        return stack;
    }
}