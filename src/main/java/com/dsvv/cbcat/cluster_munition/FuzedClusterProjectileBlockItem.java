package com.dsvv.cbcat.cluster_munition;

import com.dsvv.cbcat.cartridge.ClusterProjectileCartridgeBlock;
import com.dsvv.cbcat.cartridge.IProjectileCartridgeBlockItem;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.base.CBCTooltip;
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
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        CompoundTag baseTag = stack.getOrCreateTag();
        if (!baseTag.contains("BlockEntityTag"))
            return;
        CompoundTag tag = baseTag.getCompound("BlockEntityTag");
        ItemStack fuze = ItemStack.of(tag.getCompound("Fuze"));
        if (!fuze.isEmpty()) {
            CreateLang.builder("block").translate("createbigcannons.shell.tooltip.fuze", new Object[0]).add(Component.literal(" ")).add(fuze.getDisplayName().copy()).addTo(tooltip);
            if (fuze.getItem() instanceof FuzeItem) {
                List<Component> subTooltip = new ArrayList();
                fuze.getItem().appendHoverText(fuze, level, subTooltip, flag);
                subTooltip.replaceAll((sibling) -> Component.literal("  ").append(sibling).withStyle(ChatFormatting.GRAY));
                tooltip.addAll(subTooltip);
            }
        }
        if (getBlock() instanceof ClusterProjectileCartridgeBlock clusterCartridgeBlock) {
            IProjectileCartridgeBlockItem.appendCartridgePropellantPowerText(stack, level, tooltip, flag);
            CBCTooltip.appendMuzzleVelocityText(stack, level, tooltip, flag, clusterCartridgeBlock);
            CBCTooltip.appendPropellantStressText(stack, level, tooltip, flag, clusterCartridgeBlock);
        }
        if (tag.contains("Projectile"))
            tooltip.add(Component.translatable(tag.getString("Projectile")));
        if (tag.contains("SecondaryFuzes")) {
            Map<String, Integer> fuzeCounts = new HashMap<>();
            ListTag fuzes = tag.getList("SecondaryFuzes", 10);
            ItemStack[] fuzeItems = new ItemStack[fuzes.size()];
            int j = 0;
            for (int i = 0; i < fuzes.size(); i++) {
                ItemStack fuzeStack = ItemStack.of(fuzes.getCompound(i));
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
                fuzeItems[i].getItem().appendHoverText(fuzeItems[i], level, subTooltip, flag);
                subTooltip.replaceAll(sibling -> Component.literal("  ").append(sibling).withStyle(ChatFormatting.GRAY));
                tooltip.addAll(subTooltip);
                i++;
            }
        }
        //return result;
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        InteractionResult result = super.place(context);

        CompoundTag baseTag = context.getItemInHand().getOrCreateTag();

        if (!baseTag.contains("BlockEntityTag"))
            return result;

        CompoundTag stackTag = baseTag.getCompound("BlockEntityTag");

        if (!stackTag.contains("SecondaryFuzes") && !stackTag.contains("Projectile"))
            return result;

        ListTag fuzeList = stackTag.getList("SecondaryFuzes", 10);
        String projectile = stackTag.getString("Projectile");

        ItemStack[] clusterParts = new ItemStack[fuzeList.size()];
        for (int i = 0; i < clusterParts.length; i++)
            clusterParts[i] = ItemStack.of((CompoundTag) fuzeList.get(i));

        Level level = context.getLevel();
        if (!level.isClientSide && level.getBlockEntity(context.getClickedPos()) instanceof FuzedClusterProjectileBlockEntity clusterBE) {
            clusterBE.setFuzes(clusterParts);
            clusterBE.setProjectile(projectile);
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
        stack.getOrCreateTag().put("BlockEntityTag", new CompoundTag());
        CompoundTag tag = stack.getTag().getCompound("BlockEntityTag");
        if (!tag.contains("SecondaryFuzes"))
            return new ItemStack[] { ItemStack.EMPTY };
        ListTag fuzes = tag.getList("SecondaryFuzes", 10);
        ItemStack[] result = new ItemStack[fuzes.size()];
        for (int i = 0; i < fuzes.size(); i++)
            result[i] = ItemStack.of(fuzes.getCompound(i));
        return result;
    }

    public String getProjectileFromStack(ItemStack stack) {
        CompoundTag baseTag = stack.getOrCreateTag();
        if (!baseTag.contains("BlockEntityTag"))
            return "";
        CompoundTag tag = baseTag.getCompound("BlockEntityTag");
        if (!tag.contains("Projectile"))
            return "";
        return tag.getString("Projectile");
    }
}
