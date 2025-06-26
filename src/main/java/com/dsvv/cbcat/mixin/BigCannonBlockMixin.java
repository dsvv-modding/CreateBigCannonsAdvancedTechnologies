package com.dsvv.cbcat.mixin;

import com.dsvv.cbcat.base.IBigCannonBlockPhysics;
import org.spongepowered.asm.mixin.Mixin;
import rbasamoyai.createbigcannons.cannons.CannonContraptionProviderBlock;
import rbasamoyai.createbigcannons.cannons.InteractableCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.crafting.welding.WeldableBlock;

@Mixin(BigCannonBlock.class)
public interface BigCannonBlockMixin extends WeldableBlock, CannonContraptionProviderBlock, InteractableCannonBlock, IBigCannonBlockPhysics
{

}
