package com.dsvv.cbcat.registry;

import com.dsvv.cbcat.cannon.heavy_autocannon.contraption.MountedHeavyAutocannonContraption;
import com.dsvv.cbcat.cannon.twin_autocannon.contraption.MountedTwinAutocannonContraption;
import com.simibubi.create.content.contraptions.ContraptionType;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.CannonContraptionTypeRegistry;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.ICannonContraptionType;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ContraptionRegister
{
    public static final ContraptionType MOUNTED_TWIN_AUTOCANNON = ContraptionType.register("cbc_at.mounted_twin_autocannon", MountedTwinAutocannonContraption::new);
    public static final ContraptionType MOUNTED_HEAVY_AUTOCANNON = ContraptionType.register("cbc_at.mounted_heavy_autocannon", MountedHeavyAutocannonContraption::new);
    //public static final ContraptionType MOUNTED_ROCKET = ContraptionType.register("cbc_at.mounted_rocket", MountedRocketContraption::new);


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
