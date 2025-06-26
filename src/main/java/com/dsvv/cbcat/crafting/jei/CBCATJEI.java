package com.dsvv.cbcat.crafting.jei;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractFuzedHeavyAutocannonProjectileItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectileItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.HeavyAutocannonCartridgeItem;
import com.dsvv.cbcat.cartridge.FuzedProjectileCartridgeBlockItem;
import com.dsvv.cbcat.cartridge.ProjectileCartridge;
import com.dsvv.cbcat.cartridge.ProjectileCartridgeBlock;
import com.dsvv.cbcat.cartridge.ProjectileCartridgeBlockItem;
import com.dsvv.cbcat.cluster_munition.FuzedClusterProjectileBlockItem;
import com.dsvv.cbcat.registry.BlockRegister;
import com.dsvv.cbcat.registry.ExtraDataRegister;
import com.dsvv.cbcat.registry.ItemRegister;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.common.Tags;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCItems;

import java.util.ArrayList;
import java.util.List;

import static com.dsvv.cbcat.CreateBigCannons_AdvancedTechnology.MOD_ID;

@JeiPlugin
public class CBCATJEI implements IModPlugin {
    private static final ResourceLocation PLUGIN_ID = new ResourceLocation(MOD_ID, "jei_plugin");
    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IModPlugin.super.registerRecipes(registration);
        registration.addRecipes(RecipeTypes.CRAFTING, getMunitionAssemblyRecipes());
        registration.addRecipes(RecipeTypes.CRAFTING, getClusterMunitionAssemblyRecipes());
        registration.addRecipes(RecipeTypes.CRAFTING, getCaselessMunitionAssemblyRecipes());
    }

    public List<CraftingRecipe> getMunitionAssemblyRecipes() {
        List<CraftingRecipe> recipes = new ArrayList<>();

        List<Item> projectileItems = new ArrayList<>();
        List<Item> powder = new ArrayList<>();
        List<Item> results = new ArrayList<>();

        BuiltInRegistries.ITEM.stream().forEach( i -> {
            if (i instanceof AbstractHeavyAutocannonProjectileItem)
                projectileItems.add(i);
            else if (i instanceof HeavyAutocannonCartridgeItem && !i.equals(ItemRegister.HEAVY_AUTOCANNON_EMPTY_CARTRIDGE.get()))
                results.add(i);
            else if (i.equals(Items.GUNPOWDER) || i.equals(CBCItems.NITROPOWDER.get()))
                powder.add(i);
        });

        Ingredient projectiles = Ingredient.of(projectileItems.toArray(new Item[]{}));
        Ingredient propellant = Ingredient.of(powder.toArray(new Item[]{}));

        for (Item result : results) {
            if (result instanceof HeavyAutocannonCartridgeItem cartridgeItem)
                recipes.add(new ShapelessRecipe(new ResourceLocation(MOD_ID, result.toString() + "_recipe"), "", CraftingBookCategory.MISC, cartridgeItem.getDefaultInstance(), NonNullList.of(
                    Ingredient.EMPTY, projectiles, propellant, propellant, Ingredient.of(ItemRegister.HEAVY_AUTOCANNON_EMPTY_CARTRIDGE))));
        }
        return recipes;
    }

    public List<CraftingRecipe> getClusterMunitionAssemblyRecipes() {
        List<CraftingRecipe> recipes = new ArrayList<>();

        List<Item> projectileItems = new ArrayList<>();
        List<Item> secondaryProjectileItems = new ArrayList<>();
        List<Item> slabItems = new ArrayList<>();
        List<Item> powder = new ArrayList<>();
        List<Item> results = new ArrayList<>();

        BuiltInRegistries.ITEM.stream().forEach( i -> {
            if (i instanceof AbstractFuzedHeavyAutocannonProjectileItem) {
                projectileItems.add(i);
                secondaryProjectileItems.add(i);
            } else if (i instanceof FuzedClusterProjectileBlockItem && !(((BlockItem)i).getBlock() instanceof ProjectileCartridge))
                results.add(i);
            else if (i.equals(Items.GUNPOWDER))
                powder.add(i);
            else if (i instanceof BlockItem blockItem && blockItem.getBlock() instanceof SlabBlock slabBlock && new ItemStack(slabBlock).is(ItemTags.WOODEN_SLABS))
                slabItems.add(i);
        });

        secondaryProjectileItems.add(Items.AIR);

        Ingredient projectiles = Ingredient.of(projectileItems.toArray(new Item[]{}));
        Ingredient secondaryProjectiles = Ingredient.of(secondaryProjectileItems.toArray(new Item[]{}));
        Ingredient slabs = Ingredient.of(slabItems.toArray(new Item[]{}));

        for (Item result : results) {
            if (result instanceof FuzedClusterProjectileBlockItem clusterItem) {
                recipes.add(new ShapelessRecipe(new ResourceLocation(MOD_ID, result + "_recipe"), "", CraftingBookCategory.MISC, clusterItem.getDefaultInstance(), NonNullList.of(
                        Ingredient.EMPTY, projectiles, projectiles, projectiles, projectiles, Ingredient.of(Items.GUNPOWDER), slabs)));
                recipes.add(new ShapelessRecipe(new ResourceLocation(MOD_ID, result + "_recipe"), "", CraftingBookCategory.MISC, clusterItem.getDefaultInstance(), NonNullList.of(
                        Ingredient.EMPTY, projectiles, projectiles, projectiles, projectiles, secondaryProjectiles, Ingredient.of(Items.GUNPOWDER), slabs)));
                recipes.add(new ShapelessRecipe(new ResourceLocation(MOD_ID, result + "_recipe"), "", CraftingBookCategory.MISC, clusterItem.getDefaultInstance(), NonNullList.of(
                        Ingredient.EMPTY, projectiles, projectiles, projectiles, projectiles, secondaryProjectiles, secondaryProjectiles, Ingredient.of(Items.GUNPOWDER), slabs)));
                recipes.add(new ShapelessRecipe(new ResourceLocation(MOD_ID, result + "_recipe"), "", CraftingBookCategory.MISC, clusterItem.getDefaultInstance(), NonNullList.of(
                        Ingredient.EMPTY, projectiles, projectiles, projectiles, projectiles, secondaryProjectiles, secondaryProjectiles, secondaryProjectiles, Ingredient.of(Items.GUNPOWDER), slabs)));
            }
        }
        return recipes;
    }

    public List<CraftingRecipe> getCaselessMunitionAssemblyRecipes() {
        List<CraftingRecipe> recipes = new ArrayList<>();

        List<Item> results = new ArrayList<>();

        BuiltInRegistries.ITEM.stream().forEach( i -> {
            if (i instanceof BlockItem bItem) {
                if (bItem.getBlock() instanceof ProjectileCartridge && !(BlockRegister.GRAPESHOT_CASELESS_BLOCK.is(i) || BlockRegister.GRAPESHOT_CARTRIDGE_BLOCK.is(i)))
                    results.add(i);
            }
        });

        for (Item result : results) {
            String resultName = ExtraDataRegister.getCartridgeReverse(result);
            if (resultName.contains("caseless"))
                continue;
            recipes.add(new ShapelessRecipe(new ResourceLocation(MOD_ID, result + "_recipe"), "", CraftingBookCategory.MISC, ExtraDataRegister.getCartridge(resultName).asItem().getDefaultInstance(), NonNullList.of(
                    Ingredient.EMPTY, Ingredient.of(CBCBlocks.POWDER_CHARGE), Ingredient.of(ExtraDataRegister.getProjectileReverse(resultName)))));
            recipes.add(new ShapelessRecipe(new ResourceLocation(MOD_ID, result + "_caseless_recipe"), "", CraftingBookCategory.MISC, ExtraDataRegister.getCartridge(resultName + " caseless").asItem().getDefaultInstance(), NonNullList.of(
                    Ingredient.EMPTY, Ingredient.of(CBCItems.NITROPOWDER), Ingredient.of(CBCItems.NITROPOWDER), Ingredient.of(CBCItems.NITROPOWDER), Ingredient.of(ExtraDataRegister.getProjectileReverse(resultName)))));
        }
        return recipes;
    }
}