package com.dsvv.cbcat.cartridge;

import com.dsvv.cbcat.cluster_munition.FuzedClusterProjectile;
import com.dsvv.cbcat.cluster_munition.FuzedClusterProjectileBlock;
import com.dsvv.cbcat.cluster_munition.FuzedClusterProjectileBlockEntity;
import com.dsvv.cbcat.registry.BlockRegister;
import com.dsvv.cbcat.registry.DataComponentRegistry;
import com.dsvv.cbcat.registry.EntityRegister;
import com.dsvv.cbcat.registry.ExtraDataRegister;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


public class ClusterProjectileCartridgeBlock extends FuzedClusterProjectileBlock implements IProjectileCartridgeBlock, IBE<FuzedClusterProjectileBlockEntity>, BigCannonMunitionBlock
{
    private boolean used;
    private boolean multipleCharges = true;

    private String name;

    public ClusterProjectileCartridgeBlock(Properties properties, String name)
    {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
        used = false;
        this.name = name;
    }

    public ClusterProjectileCartridgeBlock(Properties properties, String name, boolean multipleCharges)
    {
        this(properties, name);
        this.multipleCharges = multipleCharges;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        Direction facing = context.getClickedFace();
        Player player = context.getPlayer();
        boolean flag = player != null && player.isShiftKeyDown();

        BlockState clickedState = context.getLevel().getBlockState(context.getClickedPos().relative(facing.getOpposite()));
        if (clickedState.getBlock() instanceof BigCannonBlock cblock
                && cblock.getFacing(clickedState).getAxis() == facing.getAxis()
                && !flag) {
            facing = facing.getOpposite();
        } else if (clickedState.getBlock() instanceof ProjectileBlock
                && clickedState.getValue(ProjectileBlock.FACING).getAxis() == facing.getAxis()
                && !flag) {
            facing = facing.getOpposite();
        }
        return this.defaultBlockState().setValue(FACING, facing);
    }

    public boolean isValidAddition(List<StructureBlockInfo> total, StructureBlockInfo self, int index, Direction dir)
    {
        return this.canBeIgnited(self, dir) && total.size() == 1 && total.get(0) == self && index == 0;
    }

    public boolean canBeIgnited(StructureBlockInfo data, Direction dir)
    {
        return data.state().getValue(FACING) == dir;
    }

    /*public StructureBlockInfo getHandloadingInfo(ItemStack stack, BlockPos localPos, Direction cannonOrientation)
    {
        BlockState state = this.defaultBlockState().setValue(FACING, cannonOrientation);
        CompoundTag tag = new CompoundTag();
        if(stack.hasTag())
        {
            tag = stack.getTag().getCompound("BlockEntityTag").copy();
            tag.remove("x");
            tag.remove("y");
            tag.remove("z");
            tag.putInt("Power", stack.getOrCreateTag().getInt("Power"));
            return new StructureBlockInfo(localPos, state, tag);
        }
        tag.putInt("Power", stack.getOrCreateTag().getInt("Power"));
        return new StructureBlockInfo(localPos, state, tag);
    }*/

    public int getMaximumPowerLevels()
    {
        return CBCMunitionPropertiesHandlers.BIG_CARTRIDGE.getPropertiesOf(this).maxPowerLevels() - 1;
    }

    public boolean isBaseFuze() {
        return CBCMunitionPropertiesHandlers.COMMON_SHELL_BIG_CANNON_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
    }

    public EntityType<FuzedClusterProjectile> getAssociatedEntityType() {
        return EntityRegister.CLUSTER_PROJECTILE.get();
    }

    @Override
    public float getChargePower(StructureBlockInfo structureBlockInfo)
    {
        return multipleCharges ? 1.5f : 7.5f;
    }

    @Override
    public float getChargePower(ItemStack itemStack) {
        return multipleCharges ? 1.5f : 7.5f;
    }

    @Override
    public float getStressOnCannon(StructureBlockInfo structureBlockInfo) {
        return multipleCharges ? 0.5f : 1f;
    }

    @Override
    public float getStressOnCannon(ItemStack itemStack) {
        return multipleCharges ? 0.5f : 1f;
    }

    @Override
    public float getSpread(StructureBlockInfo structureBlockInfo) {
        return multipleCharges ? 4 : 10;
    }

    @Override
    public float getRecoil(StructureBlockInfo structureBlockInfo) {
        return multipleCharges ? 1 : 2;
    }

    @Override
    public void consumePropellant(BigCannonBehavior behavior)
    {
        used = true;
    }

    public boolean allowsMultipleCharges() { return multipleCharges; }

    public static ItemStack getChargedHighExplosiveWithImpactFuze() {
        ArrayList<ItemStack> fuzes = new ArrayList();
        ItemStack fuze = CBCItems.IMPACT_FUZE.asStack();
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        ItemStack stack = BlockRegister.CLUSTER_CARTRIDGE_BLOCK.asStack();
        stack.set(CBCDataComponents.POWER, 1);
        stack.set(DataComponentRegistry.CLUSTER_FUZES, ItemContainerContents.fromItems(fuzes));
        stack.set(DataComponentRegistry.CLUSTER_PROJECTILE, ExtraDataRegister.clusterPartsReverse(EntityRegister.HA_HE_PROJECTILE.get()));
        return stack;
    }

    public static ItemStack getChargedHighExplosiveFragmentationWithImpactFuze() {
        ArrayList<ItemStack> fuzes = new ArrayList();
        ItemStack fuze = CBCItems.IMPACT_FUZE.asStack();
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        ItemStack stack = BlockRegister.CLUSTER_CARTRIDGE_BLOCK.asStack();
        stack.set(CBCDataComponents.POWER, 1);
        stack.set(DataComponentRegistry.CLUSTER_FUZES, ItemContainerContents.fromItems(fuzes));
        stack.set(DataComponentRegistry.CLUSTER_PROJECTILE, ExtraDataRegister.clusterPartsReverse(EntityRegister.HA_HEF_PROJECTILE.get()));
        return stack;
    }

    public static ItemStack getChargedHighExplosiveAntiTankWithImpactFuze() {
        ArrayList<ItemStack> fuzes = new ArrayList();
        ItemStack fuze = CBCItems.IMPACT_FUZE.asStack();
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        ItemStack stack = BlockRegister.CLUSTER_CARTRIDGE_BLOCK.asStack();
        stack.set(CBCDataComponents.POWER, 1);
        stack.set(DataComponentRegistry.CLUSTER_FUZES, ItemContainerContents.fromItems(fuzes));
        stack.set(DataComponentRegistry.CLUSTER_PROJECTILE, ExtraDataRegister.clusterPartsReverse(EntityRegister.HA_HEAT_PROJECTILE.get()));
        return stack;
    }

    public static ItemStack getChargedSmokeWithImpactFuze() {
        ArrayList<ItemStack> fuzes = new ArrayList();
        ItemStack fuze = CBCItems.IMPACT_FUZE.asStack();
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        ItemStack stack = BlockRegister.CLUSTER_CARTRIDGE_BLOCK.asStack();
        stack.set(CBCDataComponents.POWER, 1);
        stack.set(DataComponentRegistry.CLUSTER_FUZES, ItemContainerContents.fromItems(fuzes));
        stack.set(DataComponentRegistry.CLUSTER_PROJECTILE, ExtraDataRegister.clusterPartsReverse(EntityRegister.HA_SMOKE_PROJECTILE.get()));
        return stack;
    }

    public static ItemStack getCaselessHighExplosiveWithImpactFuze() {
        ArrayList<ItemStack> fuzes = new ArrayList();
        ItemStack fuze = CBCItems.IMPACT_FUZE.asStack();
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        ItemStack stack = BlockRegister.CLUSTER_CASELESS_BLOCK.asStack();
        stack.set(CBCDataComponents.POWER, 3);
        stack.set(DataComponentRegistry.CLUSTER_FUZES, ItemContainerContents.fromItems(fuzes));
        stack.set(DataComponentRegistry.CLUSTER_PROJECTILE, ExtraDataRegister.clusterPartsReverse(EntityRegister.HA_HE_PROJECTILE.get()));
        return stack;
    }

    public static ItemStack getCaselessHighExplosiveFragmentationWithImpactFuze() {
        ArrayList<ItemStack> fuzes = new ArrayList();
        ItemStack fuze = CBCItems.IMPACT_FUZE.asStack();
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        ItemStack stack = BlockRegister.CLUSTER_CASELESS_BLOCK.asStack();
        stack.set(CBCDataComponents.POWER, 3);
        stack.set(DataComponentRegistry.CLUSTER_FUZES, ItemContainerContents.fromItems(fuzes));
        stack.set(DataComponentRegistry.CLUSTER_PROJECTILE, ExtraDataRegister.clusterPartsReverse(EntityRegister.HA_HEF_PROJECTILE.get()));
        return stack;
    }

    public static ItemStack getCaselessHighExplosiveAntiTankWithImpactFuze() {
        ArrayList<ItemStack> fuzes = new ArrayList();
        ItemStack fuze = CBCItems.IMPACT_FUZE.asStack();
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        ItemStack stack = BlockRegister.CLUSTER_CASELESS_BLOCK.asStack();
        stack.set(CBCDataComponents.POWER, 3);
        stack.set(DataComponentRegistry.CLUSTER_FUZES, ItemContainerContents.fromItems(fuzes));
        stack.set(DataComponentRegistry.CLUSTER_PROJECTILE, ExtraDataRegister.clusterPartsReverse(EntityRegister.HA_HEAT_PROJECTILE.get()));
        return stack;
    }

    public static ItemStack getCaselessSmokeWithImpactFuze() {
        ArrayList<ItemStack> fuzes = new ArrayList();
        ItemStack fuze = CBCItems.IMPACT_FUZE.asStack();
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        fuzes.add(fuze.copy());
        ItemStack stack = BlockRegister.CLUSTER_CASELESS_BLOCK.asStack();
        stack.set(CBCDataComponents.POWER, 3);
        stack.set(DataComponentRegistry.CLUSTER_FUZES, ItemContainerContents.fromItems(fuzes));
        stack.set(DataComponentRegistry.CLUSTER_PROJECTILE, ExtraDataRegister.clusterPartsReverse(EntityRegister.HA_SMOKE_PROJECTILE.get()));
        return stack;
    }
}