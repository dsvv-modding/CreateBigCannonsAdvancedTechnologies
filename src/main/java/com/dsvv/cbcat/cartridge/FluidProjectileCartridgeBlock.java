package com.dsvv.cbcat.cartridge;

import com.dsvv.cbcat.registry.ExtraDataRegister;
import com.simibubi.create.foundation.block.IBE;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.forge.munitions.fluid_shell.FluidShellBlockEntity;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.*;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.AbstractFluidShellBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidShellBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidShellProjectile;

import javax.annotation.Nullable;
import java.util.List;


public class FluidProjectileCartridgeBlock extends FluidShellBlock implements ProjectileCartridge, IBE<AbstractFluidShellBlockEntity>, BigCannonMunitionBlock, IProjectileCartridgeBlock
{
    private boolean used;
    private boolean multipleCharges = true;

    private String name;

    public FluidProjectileCartridgeBlock(Properties properties, String name)
    {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
        used = false;
        this.name = name;
    }

    public FluidProjectileCartridgeBlock(Properties properties, String name, boolean multipleCharges)
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

    public StructureBlockInfo getHandloadingInfo(ItemStack stack, BlockPos localPos, Direction cannonOrientation)
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
    }

    public ItemStack getExtractedItem(StructureBlockInfo info)
    {
        ItemStack stack = ItemStack.EMPTY;
        if(!used)
        {
            stack = ExtraDataRegister.getCartridge(name).asStack();
            stack.setTag(info.nbt());
        }
        return stack;
    }

    public int getMaximumPowerLevels()
    {
        return multipleCharges ? 1 : 3;
    }

    public boolean isBaseFuze() {
        return CBCMunitionPropertiesHandlers.COMMON_SHELL_BIG_CANNON_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
    }

    public EntityType<FluidShellProjectile> getAssociatedEntityType() {
        return CBCEntityTypes.FLUID_SHELL.get();
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
}
