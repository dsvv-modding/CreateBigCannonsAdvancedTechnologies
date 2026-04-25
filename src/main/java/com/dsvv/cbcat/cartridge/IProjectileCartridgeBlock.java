package com.dsvv.cbcat.cartridge;

import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCannonPropellantBlock;

import java.util.List;

public interface IProjectileCartridgeBlock extends BigCannonPropellantBlock
{
    boolean isComplete(List<StructureTemplate.StructureBlockInfo> projectileBlocks, Direction initialOrientation);

    AbstractBigCannonProjectile getProjectile(Level level, List<StructureTemplate.StructureBlockInfo> projectileBlocks);

    boolean allowsMultipleCharges();
}
