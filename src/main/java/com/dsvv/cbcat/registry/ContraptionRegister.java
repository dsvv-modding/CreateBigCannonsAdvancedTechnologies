package com.dsvv.cbcat.registry;

import com.dsvv.cbcat.cannon.heavy_autocannon.contraption.MountedHeavyAutocannonContraption;
//import com.dsvv.cbcat.cannon.rockets.contraptionA.MountedRocketContraption;
import com.dsvv.cbcat.cannon.twin_autocannon.contraption.MountedTwinAutocannonContraption;
import com.simibubi.create.api.contraption.ContraptionType;
import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.CBCCannonContraptionTypes;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.CannonContraptionTypeRegistry;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.ICannonContraptionType;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.simibubi.create.AllContraptionTypes.BY_LEGACY_NAME;

public class ContraptionRegister
{
    public static final Holder.Reference<ContraptionType> MOUNTED_TWIN_AUTOCANNON = register("cbc_at.mounted_twin_autocannon", MountedTwinAutocannonContraption::new);
    public static final Holder.Reference<ContraptionType> MOUNTED_HEAVY_AUTOCANNON = register("cbc_at.mounted_heavy_autocannon", MountedHeavyAutocannonContraption::new);
    //public static final ContraptionType MOUNTED_ROCKET = ContraptionType.register("cbc_at.mounted_rocket", MountedRocketContraption::new);

    private static Holder.Reference<ContraptionType> register(String name, Supplier<? extends Contraption> factory) {
        ContraptionType type = new ContraptionType(factory);
        BY_LEGACY_NAME.put(name, type);

        return Registry.registerForHolder(CreateBuiltInRegistries.CONTRAPTION_TYPE, CreateBigCannons.resource(name), type);
    }

    public enum CBCATContraptionTypes implements ICannonContraptionType
    {
        TWIN_AUTOCANNON,
        MANUAL_TWIN_AUTOCANNON,
        HEAVY_AUTOCANNON,
        ROCKET;
        private static final Map<ResourceLocation, CBCATContraptionTypes> BY_ID =
                Arrays.stream(values()).collect(Collectors.toMap(CBCATContraptionTypes::getId, Function.identity()));

        private final ResourceLocation id = CreateBigCannons.resource(this.name().toLowerCase(Locale.ROOT));

        CBCATContraptionTypes() {
            CannonContraptionTypeRegistry.register(this.id, this);
        }

        @Override public ResourceLocation getId() { return this.id; }

        @Nullable
        public static CBCATContraptionTypes byId(ResourceLocation loc) { return BY_ID.get(loc); }
    }
    public static void register() {
    }
}
