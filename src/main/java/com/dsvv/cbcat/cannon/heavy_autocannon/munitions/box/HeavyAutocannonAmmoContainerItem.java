package com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.HeavyAutocannonAmmoType;
import com.dsvv.cbcat.registry.BlockRegister;
import com.dsvv.cbcat.registry.MenuRegister;
import com.google.common.collect.Lists;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.index.CBCDataComponents;

import javax.annotation.Nullable;
import java.util.List;

public class HeavyAutocannonAmmoContainerItem extends BlockItem implements MenuProvider {
    public HeavyAutocannonAmmoContainerItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override public Component getDisplayName() { return this.getDescription(); }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return HeavyAutocannonAmmoContainerMenu.getServerMenuForItemStack(i, inventory, player.getMainHandItem(), this.isCreative());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player.mayBuild()) {
            ItemStack stack = player.getItemInHand(hand);
            if (player instanceof ServerPlayer splayer) {
                int spacing = getTracerSpacing(stack);
                Component screenName = stack.has(DataComponents.CUSTOM_NAME) ? stack.getHoverName() : this.getDisplayName();

                MenuRegister.HEAVY_AUTOCANNON_AMMO_BOX.open(splayer, screenName, this, buf -> {
                    buf.writeBoolean(this.isCreative());
                    buf.writeVarInt(spacing);
                    buf.writeBoolean(false);
                    ItemStack.STREAM_CODEC.encode(buf, new ItemStack(this));
                });
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }
        return super.use(level, player, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult superResult = super.useOn(context);
        if (superResult.consumesAction()) return superResult;
        return this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
    }

    public static ItemStack getMainAmmoStack(ItemStack container) {
        ItemContainerContents items = container.getOrDefault(CBCDataComponents.AMMO, ItemContainerContents.EMPTY);
        return items.copyOne();
    }

    public static ItemStack getTracerAmmoStack(ItemStack container) {
        ItemContainerContents items = container.getOrDefault(CBCDataComponents.TRACERS, ItemContainerContents.EMPTY);
        return items.copyOne();
    }

    public static int getTracerSpacing(ItemStack container) {
        return Math.max(container.getOrDefault(CBCDataComponents.TRACER_SPACING, 1), 1);
    }

    public static boolean shouldPullTracer(ItemStack container) {
        if (!container.has(CBCDataComponents.CURRENT_INDEX)) container.set(CBCDataComponents.CURRENT_INDEX, 0);
        int currentCount = Math.max(container.getOrDefault(CBCDataComponents.CURRENT_INDEX, 0), 0);
        container.set(CBCDataComponents.CURRENT_INDEX, currentCount >= getTracerSpacing(container) ? 0 : currentCount + 1);
        return currentCount == 0;
    }

    public static HeavyAutocannonAmmoType getTypeOfContainer(ItemStack container) {
        HeavyAutocannonAmmoType type = HeavyAutocannonAmmoType.of(getMainAmmoStack(container));
        if (type != HeavyAutocannonAmmoType.NONE) return type;
        return HeavyAutocannonAmmoType.of(getTracerAmmoStack(container));
    }

    public static int getTotalAmmoCount(ItemStack container) {
        return getMainAmmoStack(container).getCount() + getTracerAmmoStack(container).getCount();
    }

    public static ItemStack pollItemFromContainer(ItemStack container) {
        if (!(container.getItem() instanceof HeavyAutocannonAmmoContainerItem ctItem)) return ItemStack.EMPTY;
        ItemStack mainAmmo = getMainAmmoStack(container);
        ItemStack tracerAmmo = getTracerAmmoStack(container);
        ItemStack ret = ItemStack.EMPTY;
        boolean isCreative = ctItem.isCreative();

        if (isCreative && shouldPullTracer(container) || !isCreative && getTotalAmmoCount(container) % getTracerSpacing(container) == 0) {
            if (!tracerAmmo.isEmpty()) {
                if (isCreative) {
                    ret = tracerAmmo.copy();
                    ret.setCount(1);
                } else {
                    ret = tracerAmmo.split(1);
                    ItemContainerContents tracerData = tracerAmmo.isEmpty() ? ItemContainerContents.EMPTY
                            : ItemContainerContents.fromItems(Lists.newArrayList(tracerAmmo));
                    container.set(CBCDataComponents.TRACERS, tracerData);
                }
            } else if (!mainAmmo.isEmpty()) {
                if (isCreative) {
                    ret = mainAmmo.copy();
                    ret.setCount(1);
                } else {
                    ret = mainAmmo.split(1);
                    ItemContainerContents ammoData = mainAmmo.isEmpty() ? ItemContainerContents.EMPTY
                            : ItemContainerContents.fromItems(Lists.newArrayList(mainAmmo));
                    container.set(CBCDataComponents.AMMO, ammoData);
                }
            }
        } else {
            if (!mainAmmo.isEmpty()) {
                if (isCreative) {
                    ret = mainAmmo.copy();
                    ret.setCount(1);
                } else {
                    ret = mainAmmo.split(1);
                    ItemContainerContents ammoData = mainAmmo.isEmpty() ? ItemContainerContents.EMPTY
                            : ItemContainerContents.fromItems(Lists.newArrayList(mainAmmo));
                    container.set(CBCDataComponents.AMMO, ammoData);
                }
            } else if (!tracerAmmo.isEmpty()) {
                if (isCreative) {
                    ret = tracerAmmo.copy();
                    ret.setCount(1);
                } else {
                    ret = tracerAmmo.split(1);
                    ItemContainerContents ammoData = mainAmmo.isEmpty() ? ItemContainerContents.EMPTY
                            : ItemContainerContents.fromItems(Lists.newArrayList(mainAmmo));
                    container.set(CBCDataComponents.AMMO, ammoData);
                }
            }
        }
        return ret;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, ctx, tooltipComponents, isAdvanced);
        String infinity = "\u221E";

        ItemStack mainAmmo = getMainAmmoStack(stack);
        if (!mainAmmo.isEmpty()) {
            String mainValue = this.isCreative() ? infinity : Integer.toString(mainAmmo.getCount());
            tooltipComponents.add(Component.translatable("block.createbigcannons.autocannon_ammo_container.tooltip.main_ammo", mainValue, mainAmmo.getDisplayName()));
        }
        ItemStack tracerAmmo = getTracerAmmoStack(stack);
        if (!tracerAmmo.isEmpty()) {
            String tracerValue = this.isCreative() ? infinity : Integer.toString(tracerAmmo.getCount());
            tooltipComponents.add(Component.translatable("block.createbigcannons.autocannon_ammo_container.tooltip.tracers", tracerValue, tracerAmmo.getDisplayName()));
        }
        int spacingValue = getTracerSpacing(stack);
        tooltipComponents.add(Component.translatable("block.createbigcannons.autocannon_ammo_container.tooltip.tracer_spacing", spacingValue));
    }

    public boolean isCreative() { return BlockRegister.CREATIVE_HEAVY_AUTOCANNON_AMMO_BOX.is(this);}//CBCBlocks.CREATIVE_AUTOCANNON_AMMO_CONTAINER.is(this); }
}
