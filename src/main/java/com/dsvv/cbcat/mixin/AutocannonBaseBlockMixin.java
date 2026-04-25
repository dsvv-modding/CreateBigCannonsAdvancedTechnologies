package com.dsvv.cbcat.mixin;

import com.dsvv.cbcat.base.LeavesCannonBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBaseBlock;

@Mixin(AutocannonBaseBlock.class) //Currently not used, maybe in the future
public abstract class AutocannonBaseBlockMixin extends DirectionalBlock {

    protected AutocannonBaseBlockMixin(Properties p_52591_) {
        super(p_52591_);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof LeavesCannonBlockEntity leavesBE))
            return super.useItemOn(stack, state, level, pos, player, hand, hit);
        if (stack.is(Tags.Items.TOOLS_SHEAR) && !leavesBE.getLeavesItemStack().isEmpty()) {
            ItemStack leaves = leavesBE.getLeavesItemStack();
            leaves.setCount(1);
            ItemEntity dropEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), leaves);
            level.addFreshEntity(dropEntity);
            leavesBE.setLeavesItemStack(ItemStack.EMPTY);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        } else if (leavesBE.getLeavesItemStack().isEmpty() && stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof LeavesBlock) {
            leavesBE.setLeavesItemStack(stack.copy());
            stack.setCount(stack.getCount() - 1);
            player.setItemInHand(hand, stack);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }
}
