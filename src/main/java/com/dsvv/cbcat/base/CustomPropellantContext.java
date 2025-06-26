package com.dsvv.cbcat.base;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCannonPropellantBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.IntegratedPropellantProjectile;
import rbasamoyai.createbigcannons.munitions.config.BigCannonPropellantCompatibilities;
import rbasamoyai.createbigcannons.munitions.config.BigCannonPropellantCompatibilityHandler;

import java.util.*;

public class CustomPropellantContext
{
    public float chargesUsed = 0;
    public float drag = 0.0f;
    public float explosionGas = 0.0f;
    public float recoil = 0;
    public float stress = 0;
    public float smokeScale = 1;
    public float spread = 0.0f;
    public float volume = 1;
    public List<StructureBlockInfo> propellantBlocks = new ArrayList<>();
    private float velocity;
    private boolean doomedToFail = false;

    public void isDoomedToFail() { doomedToFail = true; }

    public boolean getDoomedToFail() { return doomedToFail; }

    public boolean addPropellant(BigCannonPropellantBlock propellant, StructureBlockInfo info, Direction initialOrientation)
    {
        this.propellantBlocks.add(info);
        if (!safeLoad(ImmutableList.copyOf(this.propellantBlocks), initialOrientation)) return false;
        float power = Math.max(0, propellant.getChargePower(info));
        this.chargesUsed += power;
        this.explosionGas += 0.8f * power;
        this.recoil += power * 0.8f;
        this.smokeScale += power;
        this.stress += propellant.getStressOnCannon(info);
        this.spread += propellant.getSpread(info);
        return true;
    }

    public void addBarrel(IBigCannonBlockPhysics cannonBlock)
    {
        CustomPropellantContext newCtx = cannonBlock.applyBarrelPhysic(this);
        explosionGas = newCtx.explosionGas;
        drag = newCtx.drag;
        recoil = newCtx.recoil;
        stress = newCtx.stress;
        smokeScale = newCtx.smokeScale;
        spread = newCtx.spread;
        volume = newCtx.volume;
        recoil += Math.max(0, explosionGas) * 0.75f;
        velocity += Math.max(0, explosionGas);
    }

    public boolean addIntegratedPropellant(IntegratedPropellantProjectile propellant, StructureBlockInfo firstInfo, Direction initialOrientation) {
        List<StructureBlockInfo> copy = ImmutableList.<StructureBlockInfo>builder().addAll(this.propellantBlocks).add(firstInfo).build();
        if (!safeLoad(copy, initialOrientation)) return false;
        explosionGas++;
        float power = Math.max(0, propellant.getChargePower());
        this.chargesUsed += power;
        this.smokeScale += power;
        this.recoil += power * 0.8f;
        this.stress += propellant.getStressOnCannon();
        this.spread += propellant.getSpread();
        return true;
    }

    public float getVelocity()
    {
        return Math.max(0, velocity + chargesUsed - drag);
    }

    public static boolean safeLoad(List<StructureBlockInfo> propellant, Direction orientation) {
        Map<Block, Integer> allowedCounts = new HashMap<>();
        Map<Block, Integer> actualCounts = new HashMap<>();
        for (ListIterator<StructureBlockInfo> iter = propellant.listIterator(); iter.hasNext(); ) {
            int index = iter.nextIndex();
            StructureBlockInfo info = iter.next();

            Block block = info.state().getBlock();
            if (!(block instanceof BigCannonPropellantBlock cpropel) || !(cpropel.isValidAddition(info, index, orientation))) return false;
            if (actualCounts.containsKey(block)) {
                actualCounts.put(block, actualCounts.get(block) + 1);
            } else {
                actualCounts.put(block, 1);
            }
            BigCannonPropellantCompatibilities compatibilities = BigCannonPropellantCompatibilityHandler.getCompatibilities(block);
            for (Map.Entry<Block, Integer> entry : compatibilities.validPropellantCounts().entrySet()) {
                Block block1 = entry.getKey();
                int oldCount = allowedCounts.getOrDefault(block1, -1);
                int newCount = entry.getValue();
                if (newCount >= 0 && (oldCount < 0 || newCount < oldCount)) allowedCounts.put(block1, newCount);
            }
        }
        for (Map.Entry<Block, Integer> entry : actualCounts.entrySet()) {
            Block block = entry.getKey();
            if (allowedCounts.containsKey(block) && allowedCounts.get(block) < entry.getValue()) return false;
        }
        return true;
    }
}
