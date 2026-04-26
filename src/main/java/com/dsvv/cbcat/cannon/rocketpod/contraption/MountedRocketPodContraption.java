package com.dsvv.cbcat.cannon.rocketpod.contraption;

import com.dsvv.cbcat.base.ICarriageAdjustableFireRate;
import com.dsvv.cbcat.cannon.rocketpod.*;
import com.dsvv.cbcat.cannon.rocketpod.breech.RocketPodBreechBlockEntity;
import com.dsvv.cbcat.cannon.rocketpod.munitions.AbstractRocket;
import com.dsvv.cbcat.cannon.rocketpod.munitions.AbstractRocketItem;
import com.dsvv.cbcat.registry.ContraptionRegister;
import com.simibubi.create.api.contraption.ContraptionType;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.StructureTransform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_control.ControlPitchContraption;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.ICannonContraptionType;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.ItemCannon;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannons.ItemCannonBehavior;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterialProperties;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.effects.particles.plumes.AutocannonPlumeParticleData;
import rbasamoyai.createbigcannons.index.CBCAutocannonMaterials;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.munitions.autocannon.config.AutocannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.network.ClientboundAnimateCannonContraptionPacket;
import rbasamoyai.createbigcannons.remix.GetItemStorage;
import rbasamoyai.createbigcannons.utils.CBCUtils;
import rbasamoyai.ritchiesprojectilelib.RitchiesProjectileLib;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock.writeAndSyncSingleBlockData;

public class MountedRocketPodContraption extends AbstractMountedCannonContraption implements ItemCannon, GetItemStorage, ICarriageAdjustableFireRate
{
    private AutocannonMaterial cannonMaterial;

    public boolean assemble(Level level, BlockPos pos) throws AssemblyException {
        if (!this.collectCannonBlocks(level, pos)) {
            return false;
        } else {
            this.bounds = this.createBoundsFromExtensionLengths();
            return !this.blocks.isEmpty();
        }
    }

    private boolean collectCannonBlocks(Level level, BlockPos pos) throws AssemblyException {
        BlockState startState = level.getBlockState(pos);

        if (!(startState.getBlock() instanceof RocketPodBlock startCannon)) {
            return false;
        }
        if (!startCannon.isComplete(startState)) {
            throw hasIncompleteCannonBlocks(pos);
        }
        AutocannonMaterial material = startCannon.getAutocannonMaterial();
        boolean isStartBreech = startCannon.isBreechMechanism(startState);
        List<StructureTemplate.StructureBlockInfo> cannonBlocks = new ArrayList();
        cannonBlocks.add(new StructureTemplate.StructureBlockInfo(pos, startState, this.getBlockEntityNBT(level, pos)));
        int cannonLength = 1;
        Direction cannonFacing = startCannon.getFacing(startState);
        Direction positive = Direction.get(Direction.AxisDirection.POSITIVE, cannonFacing.getAxis());
        Direction negative = positive.getOpposite();
        BlockPos start = pos;
        BlockState nextState = level.getBlockState(pos.relative(positive));
        boolean positiveBreech = false;

        while (nextState.getBlock() instanceof RocketPodBlock cBlock && this.isConnectedToCannon(level, nextState, start.relative(positive), positive, material)) {
            start = start.relative(positive);
            if (!cBlock.isComplete(nextState))
                throw hasIncompleteCannonBlocks(start);
            cannonBlocks.add(new StructureTemplate.StructureBlockInfo(start, nextState, this.getBlockEntityNBT(level, start)));
            this.frontExtensionLength++;
            cannonLength++;
            positiveBreech = cBlock.isBreechMechanism(nextState);
            if (positiveBreech && isStartBreech)
                throw invalidCannon();
            if (positiveBreech && cBlock.getFacing(nextState) != negative)
                throw incorrectBreechDirection(start);
            nextState = level.getBlockState(start.relative(positive));
            if (cannonLength > getMaxCannonLength())
                throw cannonTooLarge();
            if (positiveBreech)
                break;
        }

        BlockPos positiveEndPos = positiveBreech ? start : start.relative(negative);
        start = pos;
        nextState = level.getBlockState(pos.relative(negative));
        boolean negativeBreech = false;

        while (nextState.getBlock() instanceof RocketPodBlock cBlock && this.isConnectedToCannon(level, nextState, start.relative(negative), negative, material)) {
            start = start.relative(negative);
            if (!cBlock.isComplete(nextState)) throw hasIncompleteCannonBlocks(start);
            cannonBlocks.add(new StructureTemplate.StructureBlockInfo(start, nextState, this.getBlockEntityNBT(level, start)));
            this.backExtensionLength++;
            cannonLength++;
            negativeBreech = cBlock.isBreechMechanism(nextState);
            if (negativeBreech && isStartBreech)
                throw invalidCannon();
            if (negativeBreech && cBlock.getFacing(nextState) != positive)
                throw incorrectBreechDirection(start);
            nextState = level.getBlockState(start.relative(negative));
            if (cannonLength > getMaxCannonLength())
                throw cannonTooLarge();
            if (negativeBreech)
                break;
        }

        BlockPos negativeEndPos = negativeBreech ? start : start.relative(positive);
        if (cannonLength < 2 || (positiveBreech && negativeBreech))
            throw invalidCannon();
        this.startPos = !positiveBreech && !negativeBreech ? pos : (negativeBreech ? negativeEndPos : positiveEndPos);
        BlockState breechState = level.getBlockState(this.startPos);
        if (!(breechState.getBlock() instanceof IRocketPodBreech))
            throw invalidCannon();

        this.initialOrientation = breechState.getValue(BlockStateProperties.FACING);
        this.anchor = pos;
        this.startPos = this.startPos.subtract(pos);

        for(StructureTemplate.StructureBlockInfo blockInfo : cannonBlocks) {
            BlockPos localPos = blockInfo.pos().subtract(pos);
            StructureTemplate.StructureBlockInfo localBlockInfo = new StructureTemplate.StructureBlockInfo(localPos, blockInfo.state(), blockInfo.nbt());
            this.blocks.put(localPos, localBlockInfo);
            if (blockInfo.nbt() != null) {
                BlockEntity be = BlockEntity.loadStatic(localPos, blockInfo.state(), blockInfo.nbt(), level.registryAccess());
                this.presentBlockEntities.put(localPos, be);
            }
        }

        StructureTemplate.StructureBlockInfo startInfo = this.blocks.get(this.startPos);

        this.cannonMaterial = material;

        return true;
    }

    private boolean isConnectedToCannon(LevelAccessor level, BlockState state, BlockPos pos, Direction connection, AutocannonMaterial material) {
        return true;
        /*RocketPodBlock cBlock = (RocketPodBlock) state.getBlock();
        if (cBlock.getAutocannonMaterialInLevel(level, state, pos) != material) return false;
        return level.getBlockEntity(pos) instanceof IRocketPodBlockEntity cbe
                && level.getBlockEntity(pos.relative(connection.getOpposite())) instanceof IRocketPodBlockEntity cbe1
                && cbe.cannonBehavior().isConnectedTo(connection.getOpposite())
                && cbe1.cannonBehavior().isConnectedTo(connection);*/
    }

    public void addBlocksToWorld(Level world, StructureTransform transform) {
        Map<BlockPos, StructureTemplate.StructureBlockInfo> modifiedBlocks = new HashMap();

        for(Map.Entry<BlockPos, StructureTemplate.StructureBlockInfo> entry : this.blocks.entrySet()) {
            StructureTemplate.StructureBlockInfo info = entry.getValue();
            BlockState newState = info.state();
            boolean modified = true;
            if (newState.hasProperty(RocketPodBarrelBlock.ASSEMBLED) && newState.getValue(RocketPodBarrelBlock.ASSEMBLED)) {
                newState = newState.setValue(RocketPodBarrelBlock.ASSEMBLED, false);
                modified = true;
            }

            CompoundTag infoNbt = info.nbt();
            if (infoNbt != null) {
                if (infoNbt.contains("AnimateTicks")) {
                    infoNbt.remove("AnimateTicks");
                    modified = true;
                }

                if (infoNbt.contains("RenderedBlocks")) {
                    infoNbt.remove("RenderedBlocks");
                    modified = true;
                }
            }

            if (modified) {
                modifiedBlocks.put(info.pos(), new StructureTemplate.StructureBlockInfo(info.pos(), newState, infoNbt));
            }
        }

        this.blocks.putAll(modifiedBlocks);
        super.addBlocksToWorld(world, transform);
    }

    public void fireShot(ServerLevel level, PitchOrientedContraptionEntity entity) {
        if (this.startPos == null || this.cannonMaterial == null || !(this.presentBlockEntities.get(this.startPos) instanceof IRocketPodBreechBE breech) || !breech.canFire())
            return;
        boolean isManual = breech.isManual();
        /*HeavyAutocannonQuickFireBreechBlockEntity qfbreech = null;
        if (isManual)
            qfbreech = (HeavyAutocannonQuickFireBreechBlockEntity) breech;*/
        /*ItemStack foundProjectile = breech.extractNextInput();
        RocketItem round = null;
        AbstractRocketItem projectileItem = null;
        if (foundProjectile.getItem() instanceof RocketItem rocketItem)
            round = rocketItem;
        else if (foundProjectile.getItem() instanceof AbstractRocketItem) {
            //displayCustomClientMessage("Up to this point");
            projectileItem = foundProjectile.getItem();
        }

        else
            return;*/
        ItemStack foundProjectile = breech.extractNextInput();
        if (!(foundProjectile.getItem() instanceof AbstractRocketItem round)) return;
        //displayCustomClientMessage("Ammo Accepted");
        ControlPitchContraption controller = entity.getController();
        Vec3 ejectPos = entity.toGlobalVector(Vec3.atCenterOf(this.startPos.relative(Direction.DOWN)), 0.0F);
        Vec3 centerPos = entity.toGlobalVector(Vec3.atCenterOf(BlockPos.ZERO), 0.0F);
        /*ItemStack ejectStack = null;

        ejectStack = round != null ? round.getSpentItem(foundProjectile) : ItemStack.EMPTY;
        if (!ejectStack.isEmpty() && breech instanceof RocketPodBreechBlockEntity) {
            ItemEntity ejectEntity = new ItemEntity(level, ejectPos.x, ejectPos.y, ejectPos.z, ejectStack);
            Vec3 eject = ejectPos.subtract(centerPos).normalize();
            ejectEntity.setDeltaMovement(eject.scale(0.5));
            ejectEntity.setPickUpDelay(20);
            level.addFreshEntity(ejectEntity);
        }*/

        AutocannonMaterialProperties properties = this.cannonMaterial.properties();
        AutocannonProjectilePropertiesComponent roundProperties = round.getAutocannonProperties(foundProjectile);
        boolean canFail = !(Boolean) CBCConfigs.server().failure.disableAllFailure.get();
        float speed = properties.baseSpeed() * 0.5f;//isManual ? properties.baseSpeed() * 0.75f + qfbreech.getCharge() : properties.baseSpeed();
        float spread = properties.baseSpread();
        boolean canSquib = roundProperties == null || roundProperties.canSquib();
        canSquib |= canFail;
        BlockPos currentPos = this.startPos.relative(this.initialOrientation);
        int barrelTravelled = 0;
        boolean squib = false;

        while(true) {
            Object vec1 = this.presentBlockEntities.get(currentPos);
            if (!(vec1 instanceof IRocketPodBlockEntity)) {
                break;
            }

            IRocketPodBlockEntity autocannon = (IRocketPodBlockEntity)vec1;
            ItemCannonBehavior behavior = autocannon.cannonBehavior();
            if (behavior.canLoadItem(foundProjectile)) {
                ++barrelTravelled;
                if (barrelTravelled <= properties.maxSpeedIncreases()) {
                    speed += properties.speedIncreasePerBarrel() * 0.25f;
                }

                spread -= properties.spreadReductionPerBarrel() * 0.825f;
                spread = Math.max(spread, 0.0F);
                if (canSquib && barrelTravelled > Math.floor(properties.maxBarrelLength() * 1.5f)) {
                    StructureTemplate.StructureBlockInfo oldInfo = this.blocks.get(currentPos);
                    if (oldInfo == null) {
                        return;
                    }

                    behavior.tryLoadingItem(foundProjectile);
                    CompoundTag tag = (this.presentBlockEntities.get(currentPos)).saveWithFullMetadata(level.registryAccess());
                    tag.remove("x");
                    tag.remove("y");
                    tag.remove("z");
                    StructureTemplate.StructureBlockInfo squibInfo = new StructureTemplate.StructureBlockInfo(currentPos, oldInfo.state(), tag);
                    this.blocks.put(currentPos, squibInfo);
                    Vec3 squibPos = entity.toGlobalVector(Vec3.atCenterOf(currentPos), 0.0F);
                    level.playSound(null, squibPos.x, squibPos.y, squibPos.z, oldInfo.state().getSoundType().getBreakSound(), SoundSource.BLOCKS, 10.0F, 0.0F);
                    squib = true;
                    break;
                }

                currentPos = currentPos.relative(this.initialOrientation);
            } else {
                behavior.removeItem();
                if (canFail) {
                    Vec3 failurePoint = entity.toGlobalVector(Vec3.atCenterOf(currentPos), 0.0F);
                    level.explode(null, failurePoint.x, failurePoint.y, failurePoint.z, 2.0F, Level.ExplosionInteraction.NONE);

                    for(int j = 0; j < 10; ++j) {
                        BlockPos pos = currentPos.relative(this.initialOrientation, j);
                        this.blocks.remove(pos);
                    }

                    if (controller != null) {
                        controller.disassemble();
                    }

                    return;
                }
            }
        }

        breech.handleFiring(); //breech.handleFiring(ejectStack);
        if (squib) {
            return;
        }

        /*for(BlockPos pos : this.recoilSpringPositions) {
            Object var51 = this.presentBlockEntities.get(pos);
            if (var51 instanceof HeavyAutocannonRecoilSpringBlockEntity) {
                HeavyAutocannonRecoilSpringBlockEntity spring = (HeavyAutocannonRecoilSpringBlockEntity)var51;
                spring.handleFiring();
            }
        }*/

        NetworkPlatform.sendToClientTracking(ClientboundAnimateCannonContraptionPacket.entity(entity), entity);
        Vec3 spawnPos = entity.toGlobalVector(Vec3.atCenterOf(currentPos.relative(this.initialOrientation)), 0.0F);
        Vec3 vec1 = spawnPos.subtract(centerPos).normalize();
        spawnPos = spawnPos.subtract(vec1.scale(1.5F));
        Vec3 particlePos = spawnPos;
        float recoilMagnitude = properties.baseRecoil();
        //boolean isTracer = CBCConfigs.server().munitions.allAutocannonProjectilesAreTracers.get() || (round != null && round.isTracer(foundProjectile));
        AbstractRocket projectile = round.getAutocannonProjectile(foundProjectile, level);
        if (projectile != null) {
            projectile.setPos(spawnPos);
            projectile.setChargePower((float)barrelTravelled);
            //projectile.setTracer(true);
            projectile.setLifetime(properties.projectileLifetime());
            projectile.shoot(vec1.x, vec1.y, vec1.z, speed, spread);
            projectile.xRotO = projectile.getXRot();
            projectile.yRotO = projectile.getYRot();
            projectile.addUntouchableEntity(entity, 1);
            Entity vehicle = entity.getVehicle();
            if (vehicle != null && CBCEntityTypes.CANNON_CARRIAGE.is(vehicle)) {
                projectile.addUntouchableEntity(vehicle, 1);
            }

            level.addFreshEntity(projectile);
            if (roundProperties != null) {
                recoilMagnitude = (float)((double)recoilMagnitude + roundProperties.addedRecoil());
            }
        }

        recoilMagnitude *= CBCConfigs.server().cannons.autocannonRecoilScale.getF() * 2.0F;
        if (controller != null) {
            controller.onRecoil(vec1.scale((-recoilMagnitude)), entity);
        }

        Vec3 particleVel = vec1.scale(1.25F);

        for(ServerPlayer player : level.players()) {
            if (entity.getControllingPassenger() != player) {
                level.sendParticles(player, new AutocannonPlumeParticleData(1.0F), true, particlePos.x, particlePos.y, particlePos.z, 0, particleVel.x, particleVel.y, particleVel.z, 1.0F);
            }
        }

        /*if (round.getType() == AutocannonAmmoType.MACHINE_GUN) {
            CBCUtils.playBlastLikeSoundOnServer(level, spawnPos.x, spawnPos.y, spawnPos.z, CBCSoundEvents.FIRE_MACHINE_GUN.getMainEvent(), SoundSource.BLOCKS, 10.0F * this.volumeMultiplier, 0.75F * this.volumeMultiplier, 3.0F);
        } else {*/
        CBCUtils.playBlastLikeSoundOnServer(level, spawnPos.x, spawnPos.y, spawnPos.z, CBCSoundEvents.FIRE_AUTOCANNON.getMainEvent(), SoundSource.BLOCKS, 15.0F/* * this.volumeMultiplier*/, 1.05f/* * this.volumeMultiplier*/, 3.0F);
        //}

        if (projectile != null && CBCConfigs.server().munitions.projectilesCanChunkload.get()) {
            ChunkPos cpos1 = new ChunkPos(BlockPos.containing(projectile.position()));
            RitchiesProjectileLib.queueForceLoad(level, cpos1.x, cpos1.z);
        }
    }

    public void animate() {
        super.animate();
        if (this.presentBlockEntities.get(this.startPos) instanceof IRocketPodBreechBE breech)
            breech.handleFiring();
        if (this.getBlockEntityClientSide(this.startPos) instanceof RocketPodBreechBlockEntity breech)
            breech.handleFiring();
        /*for (BlockPos pos : this.recoilSpringPositions) {
            if (this.getBlockEntityClientSide(pos) instanceof TwinAutocannonRecoilSpringBlockEntity spring)
                spring.handleFiring();
        }
        for (BlockPos pos : this.recoilSpringPositions) {
            if (this.presentBlockEntities.get(pos) instanceof HeavyAutocannonRecoilSpringBlockEntity spring)
                spring.handleFiring();
        }*/
    }

    public void tick(Level level, PitchOrientedContraptionEntity entity) {
        super.tick(level, entity);

        Entity controller = entity.getControllingPassenger();
        if (this.canBeTurnedByPassenger(controller)) {
            Direction dir = entity.getInitialOrientation();
            boolean flag = (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE) == (dir.getAxis() == Direction.Axis.X);
            entity.pitch = flag ? -controller.xRotO : controller.xRotO;
            entity.yaw = Mth.wrapDegrees(controller.yRotO);
            controller.setYBodyRot(controller.getYRot());
            if (CBCEntityTypes.CANNON_CARRIAGE.is(entity.getVehicle())) {
                entity.getVehicle().onPassengerTurned(entity);
            } else if (entity.getController() instanceof CannonMountBlockEntity) {
                entity.setXRot(entity.pitch);
                entity.setYRot(entity.yaw);
            }
        }

        if (CBCEntityTypes.CANNON_CARRIAGE.is(entity.getVehicle())) {
            controller = entity.getVehicle().getControllingPassenger();
        }
        if (!level.isClientSide && controller instanceof Player player) {
            String key = "";
            ControlPitchContraption controllerBlock = entity.getController();
            if (controllerBlock != null) {
                ResourceLocation loc = controllerBlock.getTypeId();
                if (loc != null) key = "." + loc.getNamespace() + "." + loc.getPath();
            }
            player.displayClientMessage(Component.translatable("block." + CreateBigCannons.MOD_ID + ".cannon_carriage.hotbar.fireRate" + key,
                    this.getReferencedFireRate()), true);
        }

        if (level instanceof ServerLevel slevel && this.canBeFiredOnController(entity.getController())) {
            this.fireShot(slevel, entity);
        }

        for (Map.Entry<BlockPos, BlockEntity> entry : this.presentBlockEntities.entrySet()) {
            if (entry.getValue() instanceof IRocketPodBlockEntity autocannon) {
                autocannon.tickFromContraption(level, entity, entry.getKey());
            }
        }

    }

    @Override
    public BlockPos getSeatPos(Entity entity) {
        return entity == this.entity.getControllingPassenger() ? this.startPos.relative(this.initialOrientation.getOpposite()) : super.getSeatPos(entity);
    }

    @Override
    public boolean canBeTurnedByPassenger(Entity entity) {
        if (this.entity instanceof PitchOrientedContraptionEntity poce && poce.getController() instanceof FixedCannonMountBlockEntity)
            return false;
        return entity instanceof Player;
    }

    @Override
    public boolean canBeFiredOnController(ControlPitchContraption control) {
        return this.entity.getVehicle() != control;
    }

    @Override
    public void onRedstoneUpdate(ServerLevel level, PitchOrientedContraptionEntity entity, boolean togglePower, int firePower, ControlPitchContraption controller) {
        if (this.presentBlockEntities.get(this.startPos) instanceof IRocketPodBreechBE breech) {
            breech.setFireRate(firePower);
            writeAndSyncSingleBlockData((BlockEntity) breech, this.blocks.get(this.startPos), entity, this);
        }
    }

    public void trySettingFireRateCarriage(int fireRateAdjustment) {
        if (this.presentBlockEntities.get(this.startPos) instanceof RocketPodBreechBlockEntity breech
                && (fireRateAdjustment > 0 || breech.getFireRate() > 1)) {
            // > 0 because can't turn off carriage autocannon
            breech.setFireRate(breech.getFireRate() + fireRateAdjustment);
            writeAndSyncSingleBlockData(breech, this.blocks.get(this.startPos), entity, this);
        }
    }

    public int getReferencedFireRate() {
        Object var2 = this.presentBlockEntities.get(this.startPos);
        int var10000;
        if (var2 instanceof IRocketPodBreechBE breech) {
            var10000 = breech.getActualFireRate();
        } else {
            var10000 = 0;
        }

        return var10000;
    }

    public float getWeightForStress() {
        return this.cannonMaterial == null ? (float)this.blocks.size() : (float)this.blocks.size() * this.cannonMaterial.properties().weight() * 2;
    }

    public Vec3 getInteractionVec(PitchOrientedContraptionEntity poce) {
        return poce.toGlobalVector(Vec3.atCenterOf(this.startPos), 0.0F);
    }

    public ICannonContraptionType getCannonType() {
        return ContraptionRegister.CBCATContraptionTypes.ROCKET_POD;
    }

    public CompoundTag writeNBT(HolderLookup.Provider provider, boolean clientData) {
        CompoundTag tag = super.writeNBT(provider, clientData);
        tag.putString("AutocannonMaterial", this.cannonMaterial == null ? CBCAutocannonMaterials.CAST_IRON.name().toString() : this.cannonMaterial.name().toString());
        if (this.startPos != null) {
            tag.put("StartPos", NbtUtils.writeBlockPos(this.startPos));
        }

        /*if (!this.recoilSpringPositions.isEmpty()) {
            ListTag positionsTag = new ListTag();

            for(BlockPos pos : this.recoilSpringPositions) {
                positionsTag.add(NbtUtils.writeBlockPos(pos));
            }

            tag.put("RecoilSpringPositions", positionsTag);
        }*/

        //tag.putFloat("volumeMultiplier", this.volumeMultiplier);
        return tag;
    }

    public void readNBT(Level level, CompoundTag tag, boolean clientData) {
        super.readNBT(level, tag, clientData);
        this.cannonMaterial = AutocannonMaterial.fromNameOrNull(CBCUtils.location(tag.getString("AutocannonMaterial")));
        if (this.cannonMaterial == null) {
            this.cannonMaterial = CBCAutocannonMaterials.CAST_IRON;
        }

        this.startPos = tag.contains("StartPos") ? NbtUtils.readBlockPos(tag, "StartPos").get() : null;
        /*this.recoilSpringPositions.clear();
        if (tag.contains("RecoilSpringPositions")) {
            ListTag positionTags = tag.getList("RecoilSpringPositions", 10);
            int sz = positionTags.size();

            for(int i = 0; i < sz; ++i) {
                this.recoilSpringPositions.add(NbtUtils.readBlockPos(positionTags.getCompound(i)));
            }

            this.volumeMultiplier = tag.getFloat("volumeMultiplier");
        }*/

    }

    public ContraptionType getType() {
        return ContraptionRegister.MOUNTED_ROCKET_POD.value();
    }

    public float maximumDepression(@Nonnull ControlPitchContraption controller) {
        return 36.0F;
    }

    public float maximumElevation(@Nonnull ControlPitchContraption controller) {
        return 66.0F;
    }

    @Override
    public ItemStack insertItemIntoCannon(ItemStack stack, boolean simulate) {
        if (this.getItemStorage() == null)
            return stack;
        return this.getItemStorage().insertItem(1, stack, simulate);
    }

    @Override
    public ItemStack extractItemFromCannon(boolean simulate) {
        if (this.getItemStorage() == null)
            return ItemStack.EMPTY;
        return this.getItemStorage().extractItem(0, 1, simulate);
    }

    @Nullable
    @Override
    public IItemHandler getItemStorage() {
        return this.presentBlockEntities.get(this.startPos) instanceof RocketPodBreechBlockEntity breech ? breech.createItemHandler() : null;
    }
}
