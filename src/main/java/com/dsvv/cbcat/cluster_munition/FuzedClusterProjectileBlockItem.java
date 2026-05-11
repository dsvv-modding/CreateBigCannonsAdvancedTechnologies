package com.dsvv.cbcat.cluster_munition;

import com.dsvv.cbcat.registry.DataComponentRegistry;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.context.BlockPlaceContext;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlockItem;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuzedClusterProjectileBlockItem extends ProjectileBlockItem implements IProjectileCartridgeBlockItem {
    private ItemStack[] fuzes = new ItemStack[] {ItemStack.EMPTY, ItemStack.EMPTY};
    private String projectile = "";

    public FuzedClusterProjectileBlockItem(FuzedClusterProjectileBlock pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, tooltip, flag);
        ItemContainerContents items = stack.getOrDefault(CBCDataComponents.TRACER, ItemContainerContents.EMPTY);
        ItemStack tracer = items.copyOne();
        if (!tracer.isEmpty())
            tooltip.add(Component.translatable("tooltip.createbigcannons.tracer"));
        ItemContainerContents fuzeContainer = stack.getOrDefault(CBCDataComponents.FUZE, ItemContainerContents.EMPTY);
        ItemStack fuze = fuzeContainer.copyOne();
        if (!fuze.isEmpty()) {
            CreateLang.builder("block")
                    .translate(CreateBigCannons.MOD_ID + ".shell.tooltip.fuze")
                    .add(Component.literal(" "))
                    .add(fuze.getDisplayName().copy())
                    .addTo(tooltip);
            if (fuze.getItem() instanceof FuzeItem) {
                List<Component> subTooltip = new ArrayList<>();
                fuze.getItem().appendHoverText(fuze, ctx, subTooltip, flag);
                subTooltip.replaceAll(sibling -> Component.literal("  ").append(sibling).withStyle(ChatFormatting.GRAY));
                tooltip.addAll(subTooltip);
            }
        }
        if (stack.has(DataComponentRegistry.CLUSTER_PROJECTILE))
            tooltip.add(Component.translatable(stack.get(DataComponentRegistry.CLUSTER_PROJECTILE)));
        if (stack.has(DataComponentRegistry.CLUSTER_FUZES)) {
            Map<String, Integer> fuzeCounts = new HashMap<>();
            ItemContainerContents itemContainer = stack.get(DataComponentRegistry.CLUSTER_FUZES);
            ItemStack[] fuzeItems = new ItemStack[itemContainer.getSlots()];
            int j = 0;
            for (int i = 0; i < fuzeItems.length; i++) {
                ItemStack fuzeStack = itemContainer.getStackInSlot(i);
                String name = fuzeStack.getDisplayName().getString();
                if (fuzeCounts.containsKey(name))
                    fuzeCounts.replace(name, fuzeCounts.get(name) + 1);
                else {
                    fuzeCounts.put(name, 1);
                    fuzeItems[j] = fuzeStack;
                    j++;
                }
            }
            int i = 0;
            for (String name : fuzeCounts.keySet()) {
                tooltip.add(Component.literal(fuzeCounts.get(name) + "x " + name));
                List<Component> subTooltip = new ArrayList<>();
                fuzeItems[i].getItem().appendHoverText(fuzeItems[i], ctx, subTooltip, flag);
                subTooltip.replaceAll(sibling -> Component.literal("  ").append(sibling).withStyle(ChatFormatting.GRAY));
                tooltip.addAll(subTooltip);
                i++;
            }
        }
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        InteractionResult result = super.place(context);

        if (context.getLevel().getBlockEntity(context.getClickedPos()) instanceof FuzedClusterProjectileBlockEntity clusterBE) {
            ItemStack placedItem = context.getItemInHand();
            if (placedItem.has(CBCDataComponents.FUZE))
                clusterBE.setFuze(placedItem.get(CBCDataComponents.FUZE).copyOne());

            if (placedItem.has(DataComponentRegistry.CLUSTER_PROJECTILE))
                clusterBE.setProjectile(placedItem.get(DataComponentRegistry.CLUSTER_PROJECTILE));

            if (placedItem.has(DataComponentRegistry.CLUSTER_FUZES))
            {
                ItemContainerContents fuzesContainer = placedItem.get(DataComponentRegistry.CLUSTER_FUZES);
                ItemStack[] secondaryFuzes = new ItemStack[fuzesContainer.getSlots()];
                for (int i = 0; i < secondaryFuzes.length; i++)
                    secondaryFuzes[i] = fuzesContainer.getStackInSlot(i);
                clusterBE.setSecondaryFuzes(secondaryFuzes);
            }
        }
        return result;
    }

    public void setFuzes (ItemStack[] fuzes) {
        this.fuzes = fuzes;
    }

    public ItemStack[] getFuzes() {
        return fuzes;
    }

    public void setProjectile (String projectile) {
        this.projectile = projectile;
    }

    public String getProjectile() {
        return projectile;
    }

    public ItemStack[] getFuzesFromStack(ItemStack stack) {
        if (!stack.has(DataComponentRegistry.CLUSTER_FUZES))
            return new ItemStack[] { ItemStack.EMPTY };
        ItemStack[] result = new ItemStack[stack.get(DataComponentRegistry.CLUSTER_FUZES).getSlots()];
        int index = 0;
        for(ItemStack fuzeStack : stack.get(DataComponentRegistry.CLUSTER_FUZES).nonEmptyItems())
            result[index++] = fuzeStack;
        return result;
    }

    public String getProjectileFromStack(ItemStack stack) {
        return stack.getOrDefault(DataComponentRegistry.CLUSTER_PROJECTILE, "");
    }
}
