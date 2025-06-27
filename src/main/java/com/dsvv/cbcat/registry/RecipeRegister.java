package com.dsvv.cbcat.registry;

import com.dsvv.cbcat.CreateBigCannons_AdvancedTechnology;
import com.dsvv.cbcat.crafting.*;
import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.Lang;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.dsvv.cbcat.CreateBigCannons_AdvancedTechnology.REGISTRATE;

public enum RecipeRegister implements IRecipeTypeInfo
{
    HA_MUNITION_ASSEMBLY(noSerializer(HAMunitionAssemblyRecipe::new)),
    HA_MUNITION_FUZING(noSerializer(HAMunitionFuzingRecipe::new)),
    HA_MUNITION_UNFUZING(noSerializer(HAMunitionUnfuzingRecipe::new)),
    HA_MUNITION_TRACER(noSerializer(HAMunitionTracerRecipe::new)),
    HA_MUNITION_TRACER_REMOVAL(noSerializer(HAMunitionTracerRemovalRecipe::new)),
    CLUSTER_MUNITION_ASSEMBLY(noSerializer(ClusterMunitionAssemblyRecipe::new)),
    CASELESS_MUNITION_ASSEMBLY(noSerializer(CaselessMunitionAssemblyRecipe::new));

    private final ResourceLocation id;
    private final Supplier<RecipeSerializer<?>> serializerObject;
    @Nullable
    private final RecipeType<?> typeObject;
    private final NonNullSupplier<RecipeType<?>> type;

    RecipeRegister(NonNullSupplier<RecipeSerializer<?>> serializerSupplier, NonNullSupplier<RecipeType<?>> typeSupplier, boolean registerType) {
        String name = Lang.asId(name());
        id = new ResourceLocation(CreateBigCannons_AdvancedTechnology.MOD_ID, name);
        serializerObject = IndexPlatform.registerRecipeSerializer(this.id, serializerSupplier);
        if (registerType) {
            typeObject = typeSupplier.get();
            IndexPlatform.registerRecipeType(this.id, typeSupplier);
            type = typeSupplier;
        } else {
            typeObject = null;
            type = typeSupplier;
        }
    }

    RecipeRegister(NonNullSupplier<RecipeSerializer<?>> serializerSupplier) {
        String name = Lang.asId(name());
        id = CreateBigCannons.resource(name);//new ResourceLocation(CreateBigCannons_AdvancedTechnology.MOD_ID, name);
        serializerObject = IndexPlatform.registerRecipeSerializer(this.id, serializerSupplier);
        typeObject = simpleType(id);
        type = () -> typeObject;
        IndexPlatform.registerRecipeType(this.id, this.type);
    }

    RecipeRegister(ProcessingRecipeBuilder.ProcessingRecipeFactory<?> processingFactory) {
        this(() -> new ProcessingRecipeSerializer<>(processingFactory));
    }

    public static <T extends Recipe<?>> RecipeType<T> simpleType(ResourceLocation id) {
        String stringId = id.toString();
        return new RecipeType<>() {
            @Override
            public String toString() {
                return stringId;
            }
        };
    }

    @Override public ResourceLocation getId() { return this.id; }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeSerializer<?>> T getSerializer() { return (T) this.serializerObject.get(); }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeType<?>> T getType() { return (T) this.type.get(); }

    public static void register() {
        REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeRegister::buildRecipe);
    }

    public static void buildRecipe(Consumer<FinishedRecipe> cons) {
        SpecialRecipeBuilder.special(HA_MUNITION_ASSEMBLY.getSerializer()).save(cons, "ha_munition_assembly");
        SpecialRecipeBuilder.special(HA_MUNITION_FUZING.getSerializer()).save(cons, "ha_munition_fuzing");
        SpecialRecipeBuilder.special(HA_MUNITION_UNFUZING.getSerializer()).save(cons, "ha_munition_unfuzing");
        SpecialRecipeBuilder.special(HA_MUNITION_TRACER.getSerializer()).save(cons, "ha_munition_tracer");
        SpecialRecipeBuilder.special(HA_MUNITION_TRACER_REMOVAL.getSerializer()).save(cons, "ha_munition_tracer_removal");
        SpecialRecipeBuilder.special(CLUSTER_MUNITION_ASSEMBLY.getSerializer()).save(cons, "cluster_munition_assembly");
        SpecialRecipeBuilder.special(CASELESS_MUNITION_ASSEMBLY.getSerializer()).save(cons, "caseless_munition_assembly");
    }

    private static <T extends Recipe<?>> NonNullSupplier<RecipeSerializer<?>> noSerializer(Function<ResourceLocation, T> prov) {
        return () -> new SimpleRecipeSerializer<>(prov);
    }

    private static class SimpleRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {
        private final Function<ResourceLocation, T> constructor;

        public SimpleRecipeSerializer(Function<ResourceLocation, T> constructor) {
            this.constructor = constructor;
        }

        @Override
        public T fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            return this.constructor.apply(recipeId);
        }

        @Override
        public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            return this.constructor.apply(recipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, T recipe) {
        }
    }
}
