package com.dsvv.cbcat.cartridge;

import com.dsvv.cbcat.registry.BlockEntityRegister;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.block.IBE;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.*;

import javax.annotation.Nullable;
import java.util.List;

public class ProjectileCartridgeBlock extends InertProjectileBlock implements IProjectileCartridgeBlock, IBE<BigCannonProjectileBlockEntity>, BigCannonMunitionBlock
{
    private boolean used;
    private boolean multipleCharges = true;

    EntityEntry<? extends AbstractBigCannonProjectile> projectileEntityEntry;

    private String name;

    public ProjectileCartridgeBlock(Properties properties, EntityEntry<? extends AbstractBigCannonProjectile> entityType, String name)
    {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
        used = false;
        projectileEntityEntry = entityType;
        this.name = name;
        this.codec = simpleCodec(this::fromSelf);
    }

    public ProjectileCartridgeBlock(Properties properties, EntityEntry<? extends AbstractBigCannonProjectile> entityType, String name, boolean multipleCharges)
    {
        this(properties, entityType, name);
        this.multipleCharges = multipleCharges;
    }

    private final MapCodec<? extends DirectionalBlock> codec;

    private ProjectileCartridgeBlock fromSelf(Properties properties) {
        return new ProjectileCartridgeBlock(properties, this.projectileEntityEntry, name);
    }

    @Override protected MapCodec<? extends DirectionalBlock> codec() { return this.codec; }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }

    @Override public BlockEntityType<? extends BigCannonProjectileBlockEntity> getBlockEntityType() { return BlockEntityRegister.PROJECTILE_CARTRIDGE_BLOCK_ENTITY.get(); }

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
            return new StructureBlockInfo(localPos, state, tag);
        }
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
        System.out.println(stack.isEmpty());
        return stack;
    }*/

    public int getMaximumPowerLevels()
    {
        return CBCMunitionPropertiesHandlers.BIG_CARTRIDGE.getPropertiesOf(this).maxPowerLevels() - 1;
    }

    public EntityType<? extends AbstractBigCannonProjectile> getAssociatedEntityType()
    {
        return projectileEntityEntry.get();
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
