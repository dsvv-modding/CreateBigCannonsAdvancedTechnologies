package com.dsvv.cbcat.cannon.medium_rocketpod.breech;

import com.dsvv.cbcat.cannon.medium_rocketpod.IMediumRocketPodBreechBE;
import com.dsvv.cbcat.cannon.medium_rocketpod.MediumRocketPodBlockEntity;
import com.dsvv.cbcat.cannon.medium_rocketpod.munitions.AbstractMediumRocketItem;
import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock.writeAndSyncSingleBlockData;

public class MediumRocketPodBreechBlockEntity extends MediumRocketPodBlockEntity implements IMediumRocketPodBreechBE {

    protected static final int[] FIRE_RATES = new int[] {
            160,  // 120
            120,  //  80
            96,  //  60
            80,  //  48
            60,  //  40
            48,  //  30
            40,  //  24
            30,  //  20
            24,  //  15
            20,  //  12
            16,   //  10
            12,   //   8
            10,   //   6
            8,   //   5
            6    //   4
    };

    private int fireRate = 7;
    private int firingCooldown;
    //private int animateTicks = 2;
    private boolean updateInstance = true;

    private IItemHandler inventory;
    //private final Deque<ItemStack> inputBuffer = new LinkedList<>();
    private final ItemStack[] inputBuffer;
    private ItemStack outputBuffer = ItemStack.EMPTY;
    //private ItemStack magazine = ItemStack.EMPTY;

    public MediumRocketPodBreechBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inputBuffer = new ItemStack[getQueueLimit()];
        Arrays.fill(inputBuffer, ItemStack.EMPTY);
    }

    public int getQueueLimit() { return 4; }

    public Deque<ItemStack> getInputBuffer() {
        Deque<ItemStack> result = new LinkedList<>();
        for (int i = 0; i < this.inputBuffer.length; i++)
            if (!inputBuffer[i].isEmpty())
                result.add(inputBuffer[i]);
        return result;
    }
    public boolean isSlotUsed(int index) { return index >= 0 && index < this.inputBuffer.length && !this.inputBuffer[index].isEmpty(); }
    public ItemStack getFromSlot(int index) { return index >= 0 && index < this.inputBuffer.length ? this.inputBuffer[index] : ItemStack.EMPTY; }
    public ItemStack getOutputBuffer() { return this.outputBuffer; }
    public void setOutputBuffer(ItemStack stack) { this.outputBuffer = stack; }

    public boolean addToInputBuffer(ItemStack stack) {
        if (stack.getItem() instanceof AbstractMediumRocketItem && !isInputFull()) {
            for (int i = 0; i < inputBuffer.length; i++)
                if (this.inputBuffer[i].isEmpty()) {
                    this.inputBuffer[i] = stack.copy();
                    this.inputBuffer[i].setCount(1);
                    this.updateInstance = true;
                    return true;
                }
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        this.allTick();
    }

    @Override
    public void tickFromContraption(Level level, PitchOrientedContraptionEntity poce, BlockPos localPos) {
        super.tickFromContraption(level, poce, localPos);
        this.allTick();
        if (level.isClientSide && poce.getContraption().getBlockEntityClientSide(localPos) instanceof MediumRocketPodBreechBlockEntity cbe) {
            cbe.allTick();
        }
        if (!level.isClientSide && this.updateInstance) {
            this.updateInstance = false;
            Contraption contraption = poce.getContraption();
            writeAndSyncSingleBlockData(this, contraption.getBlocks().get(localPos), poce, contraption);
        }
    }

    private void allTick() {
        if (this.fireRate < 0 || this.fireRate > 15) this.fireRate = 0;
        if (this.firingCooldown < 0) this.firingCooldown = 0;
        if (this.firingCooldown > 0) this.firingCooldown--;

        /*if (this.animateTicks < 5) ++this.animateTicks;
        if (this.animateTicks < 0) this.animateTicks = 0;*/
    }

    public void setFireRate(int power) { this.fireRate = Mth.clamp(power, 0, 15); }
    public int getFireRate() { return this.fireRate; }
    public int getActualFireRate() {
        if (this.fireRate < 1 || this.fireRate > 15) return 0;
        int cooldown = FIRE_RATES[this.fireRate - 1];
        return 1200 / cooldown;
    }
    public boolean canFire() { return this.getFireRate() > 0 && this.firingCooldown <= 0; }

    public void handleFiring() {
        if (this.fireRate > 0 && this.fireRate <= FIRE_RATES.length) {
            this.firingCooldown = FIRE_RATES[this.fireRate - 1];
        }
        this.updateInstance = true;
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        this.fireRate = tag.getInt("FiringRate");
        this.firingCooldown = tag.getInt("Cooldown");
        this.outputBuffer = tag.contains("Output") ? ItemStack.of(tag.getCompound("Output")) : ItemStack.EMPTY;

        Arrays.fill(this.inputBuffer, ItemStack.EMPTY);
        ListTag inputTag = tag.getList("Input", Tag.TAG_COMPOUND);
        for (int i = 0; i < inputTag.size(); ++i) {
            this.inputBuffer[i] = ItemStack.of(inputTag.getCompound(i));
        }

        if (!clientPacket) return;
        this.updateInstance = tag.contains("UpdateInstance");
        if (!this.isVirtual()) this.requestModelDataUpdate();
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.putInt("FiringRate", this.fireRate);
        tag.putInt("Cooldown", this.firingCooldown);
        if (this.outputBuffer != null && !this.outputBuffer.isEmpty()) tag.put("Output", this.outputBuffer.save(new CompoundTag()));

        tag.put("Input", Arrays.stream(this.inputBuffer)
                    .map(s -> s.save(new CompoundTag()))
                    .collect(Collectors.toCollection(ListTag::new)));

        if (!clientPacket) return;
        if (this.updateInstance) tag.putBoolean("UpdateInstance", true);
    }

    public boolean isInputFull() {
        for (int i = 0; i < this.inputBuffer.length; i++)
            if (this.inputBuffer[i].isEmpty())
                return false;
        return true;
    }
    public boolean isOutputFull() { return !this.outputBuffer.isEmpty(); }

    public ItemStack insertOutput(ItemStack stack) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        if (this.isOutputFull()) return stack;
        this.outputBuffer = stack;
        return ItemStack.EMPTY;
    }

    public ItemStack extractNextInput() {
        for (int i = 0; i < this.inputBuffer.length; i++)
            if (!this.inputBuffer[i].isEmpty()) {
                ItemStack result = this.inputBuffer[i];
                this.inputBuffer[i] = ItemStack.EMPTY;
                requestModelDataUpdate();
                return result;
            }
        return ItemStack.EMPTY;
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> list = super.getDrops();
        for (ItemStack s : this.inputBuffer) {
            if (!s.isEmpty()) list.add(s.copy());
        }
        if (!this.outputBuffer.isEmpty()) list.add(this.outputBuffer.copy());
        return list;
    }

    @Override
    protected AABB createRenderBoundingBox() {
        Direction dir = this.getBlockState().getValue(MediumRocketPodBreechBlock.FACING);
        Direction ammoContainerDir = dir.getAxis().isHorizontal() ? dir.getClockWise(Direction.Axis.Y) : Direction.EAST;
        return super.createRenderBoundingBox().expandTowards(new Vec3(dir.getOpposite().step()).add(new Vec3(ammoContainerDir.step())));
    }

    public IItemHandler createItemHandler() {
        return this.inventory == null ? this.inventory = new MediumRocketPodInventoryHandler(this) : this.inventory;
    }

    @Override
    public boolean isManual() {
        return false;
    }
}
