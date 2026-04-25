package com.dsvv.cbcat.cannon.heavy_autocannon.breech;

import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBlockEntity;
import com.dsvv.cbcat.cannon.heavy_autocannon.IHeavyAutocannonBreechBE;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box.HeavyAutocannonAmmoContainerItem;
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
import net.neoforged.neoforge.items.IItemHandler;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.AnimatedAutocannon;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class HeavyAutocannonBreechBlockEntity extends HeavyAutocannonBlockEntity implements AnimatedAutocannon, IHeavyAutocannonBreechBE
{
    protected static final int[] FIRE_RATES = new int[] {
            240, // 120
            160, //  80
            120, //  60
            96,  //  48
            80,  //  40
            60,  //  30
            48,  //  24
            40,  //  20
            30,  //  15
            24,  //  12
            20,  //  10
            16,  //   8
            12,  //   6
            10,  //   5
            8    //   4
    };

    private int fireRate = 7;
    private int firingCooldown;
    private int animateTicks = 2;
    private boolean updateInstance = true;

    private IItemHandler inventory;
    private final Deque<ItemStack> inputBuffer = new LinkedList<>();
    private ItemStack outputBuffer = ItemStack.EMPTY;
    private ItemStack magazine = ItemStack.EMPTY;

    public HeavyAutocannonBreechBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public int getQueueLimit() { return 5; }

    public Deque<ItemStack> getInputBuffer() { return this.inputBuffer; }
    public ItemStack getOutputBuffer() { return this.outputBuffer; }
    public void setOutputBuffer(ItemStack stack) { this.outputBuffer = stack; }

    public void setMagazine(ItemStack stack) {
        this.magazine = stack;
        this.updateInstance = true;
    }

    public ItemStack getMagazine() { return this.magazine; }

    @Override
    public void tick() {
        super.tick();
        this.allTick();
    }

    @Override
    public void tickFromContraption(Level level, PitchOrientedContraptionEntity poce, BlockPos localPos) {
        super.tickFromContraption(level, poce, localPos);
        this.allTick();
        if (level.isClientSide && poce.getContraption().getBlockEntityClientSide(localPos) instanceof HeavyAutocannonBreechBlockEntity cbe) {
            cbe.allTick();
        }
    }

    private void allTick() {
        if (this.fireRate < 0 || this.fireRate > 15) this.fireRate = 0;
        if (this.firingCooldown < 0) this.firingCooldown = 0;
        if (this.firingCooldown > 0) this.firingCooldown--;

        if (this.animateTicks < 5) ++this.animateTicks;
        if (this.animateTicks < 0) this.animateTicks = 0;
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
            this.animateTicks = 0;
        }
    }

    public void handleFiring(ItemStack eject) {
        handleFiring();
    }

    public float getAnimateOffset(float partialTicks) {
        float t = ((float) this.animateTicks + partialTicks) * 1.2f;
        if (t <= 0 || t >= 4.8f) return 0;
        float f = t < 1 ? t : (4.8f - t) / 3.8f;
        return Mth.sin(f * Mth.HALF_PI);
    }

    @Override public void incrementAnimationTicks() { ++this.animateTicks; }
    @Override public int getAnimationTicks() { return this.animateTicks; }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registry, boolean clientPacket) {
        super.read(tag, registry, clientPacket);
        this.fireRate = tag.getInt("FiringRate");
        this.firingCooldown = tag.getInt("Cooldown");
        this.animateTicks = tag.getInt("AnimateTicks");
        this.outputBuffer = tag.contains("Output") ? ItemStack.parseOptional(registry, tag.getCompound("Output")) : ItemStack.EMPTY;

        this.inputBuffer.clear();
        ListTag inputTag = tag.getList("Input", Tag.TAG_COMPOUND);
        for (int i = 0; i < inputTag.size(); ++i) {
            this.inputBuffer.add(ItemStack.parseOptional(registry, inputTag.getCompound(i)));
        }
        this.magazine = tag.contains("Magazine") ? ItemStack.parseOptional(registry, tag.getCompound("Magazine")) : ItemStack.EMPTY;

        if (!clientPacket) return;
        this.updateInstance = tag.contains("UpdateInstance");
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registry, boolean clientPacket) {
        super.write(tag, registry, clientPacket);
        tag.putInt("FiringRate", this.fireRate);
        tag.putInt("Cooldown", this.firingCooldown);
        tag.putInt("AnimateTicks", this.animateTicks);
        if (this.outputBuffer != null && !this.outputBuffer.isEmpty()) tag.put("Output", this.outputBuffer.saveOptional(registry));

        if (!this.inputBuffer.isEmpty()) {
            tag.put("Input", this.inputBuffer.stream()
                    .map(s -> s.save(registry))
                    .collect(Collectors.toCollection(ListTag::new)));
        }
        if (this.magazine != null && !this.magazine.isEmpty()) tag.put("Magazine", this.magazine.save(registry));

        if (!clientPacket) return;
        if (this.updateInstance) tag.putBoolean("UpdateInstance", true);
    }

    public boolean isInputFull() { return this.inputBuffer.size() >= this.getQueueLimit() || !this.magazine.isEmpty(); }
    public boolean isOutputFull() { return !this.outputBuffer.isEmpty(); }

    public ItemStack insertOutput(ItemStack stack) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        if (this.isOutputFull()) return stack;
        this.outputBuffer = stack;
        return ItemStack.EMPTY;
    }

    public ItemStack extractNextInput() {
        if (!this.inputBuffer.isEmpty()) return this.inputBuffer.poll();
        if (this.magazine.isEmpty()) return ItemStack.EMPTY;
        int totalCount = HeavyAutocannonAmmoContainerItem.getTotalAmmoCount(this.magazine);
        if (totalCount == 0) return ItemStack.EMPTY;
        if (totalCount == 1) this.updateInstance = true;
        return HeavyAutocannonAmmoContainerItem.pollItemFromContainer(this.magazine);
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> list = super.getDrops();
        for (ItemStack s : this.inputBuffer) {
            if (!s.isEmpty()) list.add(s.copy());
        }
        if (!this.outputBuffer.isEmpty()) list.add(this.outputBuffer.copy());
        if (!this.magazine.isEmpty()) list.add(this.magazine.copy());
        return list;
    }

    @Override
    protected AABB createRenderBoundingBox() {
        Direction dir = this.getBlockState().getValue(HeavyAutocannonBreechBlock.FACING);
        Direction ammoContainerDir = dir.getAxis().isHorizontal() ? dir.getClockWise(Direction.Axis.Y) : Direction.EAST;
        return super.createRenderBoundingBox().expandTowards(new Vec3(dir.getOpposite().step()).add(new Vec3(ammoContainerDir.step())));
    }

    public IItemHandler createItemHandler() {
        return this.inventory == null ? this.inventory = new HeavyAutocannonInventoryHandler(this) : this.inventory;
    }

    @Override
    public boolean isManual() {
        return false;
    }
}