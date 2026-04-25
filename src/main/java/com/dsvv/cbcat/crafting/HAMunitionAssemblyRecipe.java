package com.dsvv.cbcat.crafting;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectileItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.HeavyAutocannonCartridgeItem;
import com.dsvv.cbcat.registry.RecipeRegister;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.FuzedItemMunition;

public class HAMunitionAssemblyRecipe extends CustomRecipe
{
    public HAMunitionAssemblyRecipe() { super(CraftingBookCategory.MISC); }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        ItemStack cartridge = ItemStack.EMPTY;
        ItemStack round = ItemStack.EMPTY;
        ItemStack[] powder = new ItemStack[] {ItemStack.EMPTY, ItemStack.EMPTY};
        Boolean strong = null;

        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof HeavyAutocannonCartridgeItem) {
                if (!cartridge.isEmpty() || HeavyAutocannonCartridgeItem.hasProjectile(stack)) return false;
                cartridge = stack;
            } else if (ItemStack.isSameItem(stack, Items.GUNPOWDER.getDefaultInstance())) {
                if (strong != null && strong) return false;
                if (!powder[0].isEmpty()) powder[1] = stack;
                else powder[0] = stack;
                strong = false;
            } else if (ItemStack.isSameItem(stack, CBCItems.NITROPOWDER.asStack())) {
                if (strong != null && !strong) return false;
                if (!powder[0].isEmpty()) powder[1] = stack;
                else powder[0] = stack;
                strong = true;
            } else if (stack.getItem() instanceof AbstractHeavyAutocannonProjectileItem) {
                if (!round.isEmpty()) return false;
                round = stack;
            }else
            {
                return false;
            }
        }
        return !cartridge.isEmpty() && !powder[0].isEmpty() && !powder[1].isEmpty() && !round.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput container, HolderLookup.Provider access) {
        ItemStack cartridge = ItemStack.EMPTY;
        ItemStack round = ItemStack.EMPTY;
        ItemStack[] powder = new ItemStack[] {ItemStack.EMPTY, ItemStack.EMPTY};
        Boolean strong = null;

        for (int i = 0; i < container.size(); ++i) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof HeavyAutocannonCartridgeItem) {
                if (!cartridge.isEmpty() || HeavyAutocannonCartridgeItem.hasProjectile(stack)) return ItemStack.EMPTY;
                cartridge = stack;
            } else if (ItemStack.isSameItem(stack, Items.GUNPOWDER.getDefaultInstance())) {
                if (strong != null && strong) return ItemStack.EMPTY;
                if (!powder[0].isEmpty()) powder[1] = stack;
                else powder[0] = stack;
                strong = false;
            } else if (ItemStack.isSameItem(stack, CBCItems.NITROPOWDER.asStack())) {
                if (strong != null && !strong) return ItemStack.EMPTY;
                if (!powder[0].isEmpty()) powder[1] = stack;
                else powder[0] = stack;
                strong = true;
            } else if (stack.getItem() instanceof AbstractHeavyAutocannonProjectileItem) {
                if (!round.isEmpty()) return ItemStack.EMPTY;
                round = stack;
            } else
            {
                return ItemStack.EMPTY;
            }
        }

        if (cartridge.isEmpty() || powder[0].isEmpty() || powder[1].isEmpty() || round.isEmpty()) return ItemStack.EMPTY;
        ItemStack result = ((AbstractHeavyAutocannonProjectileItem) round.getItem()).getCreativeTabCartridgeItem(strong);
        result.setCount(1);
        /*ItemStack fuzeCopy = ((AbstractHeavyAutocannonProjectileItem)round.getItem()).getFuze(round);
        fuzeCopy.setCount(1);
        CompoundTag tag = new CompoundTag();//result.getOrCreateTag();
        if (result.getItem() instanceof FuzedItemMunition && !fuzeCopy.isEmpty()) {
            CompoundTag projectileTag = round.getOrCreateTag();
            projectileTag.put("Fuze", fuzeCopy.save(new CompoundTag()));
        }
        tag.put("Projectile", round.save(new CompoundTag()));
        tag.putBoolean("Strong", strong);*/
        return result;
    }

    @Override public boolean canCraftInDimensions(int width, int height) { return width * height >= 2; }
    @Override public RecipeSerializer<?> getSerializer() { return RecipeRegister.HA_MUNITION_ASSEMBLY.getSerializer(); }
}
