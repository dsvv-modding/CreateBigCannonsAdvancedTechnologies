package com.dsvv.cbcat.cannon.heavy_autocannon.qf_breech;

import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBlockEntity;
import com.dsvv.cbcat.cannon.heavy_autocannon.IHeavyAutocannonBreechBE;
import com.dsvv.cbcat.cannon.heavy_autocannon.breech.HeavyAutocannonBreechBlock;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.HeavyAutocannonCartridgeItem;
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
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.AnimatedAutocannon;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock.writeAndSyncSingleBlockData;

public class HeavyAutocannonQuickFireBreechBlockEntity extends HeavyAutocannonBlockEntity implements AnimatedAutocannon, IHeavyAutocannonBreechBE
{
    private int fireRate = 7;
    private int firingCooldown;
    private int animateTicks = 2;
    private boolean updateInstance = true;
    private boolean isOpen = false;
    private float openProgress = 0;
    private final int openLimit = 3;

    private ItemStack cartridge = ItemStack.EMPTY;

    public HeavyAutocannonQuickFireBreechBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
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

        if (openProgress < openLimit && isOpen)
            openProgress++;
        else if (openProgress > 0 && !isOpen)
            openProgress--;

        if (level.isClientSide && poce.getContraption().getBlockEntityClientSide(localPos) instanceof HeavyAutocannonQuickFireBreechBlockEntity cbe) {
            cbe.allTick();
            cbe.openProgress = this.openProgress;
        }
    }

    private void allTick() {
        if (this.fireRate < 0 || this.fireRate > 15) this.fireRate = 0;

        if (this.animateTicks < 5) ++this.animateTicks;
        if (this.animateTicks < 0) this.animateTicks = 0;
    }

    public void setFireRate(int power) { this.fireRate = Mth.clamp(power, 0, 15); }

    @Override
    public int getActualFireRate() {
        return fireRate > 0 ? 150 : 0;
    }

    public boolean canFire() { return openProgress <= 0 && this.fireRate > 0 && this.firingCooldown <= 0 && HeavyAutocannonCartridgeItem.hasProjectile(cartridge); }

    public void handleFiring() {
        if (this.fireRate > 0) {
            this.animateTicks = 0;
            //shoot();
        }
    }

    public void handleFiring(ItemStack eject) {
        if (this.fireRate > 0) {
            this.animateTicks = 0;
            shoot(eject);
        }
    }

    public float getAnimateOffset(float partialTicks) {
        float t = ((float) this.animateTicks + partialTicks) * 1.2f;
        if (t <= 0 || t >= 4.8f) return 0;
        float f = t < 1 ? t : (4.8f - t) / 3.8f;
        return Mth.sin(f * Mth.HALF_PI);
    }

    @Override public void incrementAnimationTicks() { ++this.animateTicks; }
    @Override public int getAnimationTicks() { return this.animateTicks; }

    public void setCartridge(ItemStack cartridge) {
        this.cartridge = cartridge;
    }

    public boolean getOpen() { return openProgress >= openLimit && isOpen; }

    public boolean willBeOpen() { return isOpen; }

    public void toggleOpen() { isOpen = !isOpen; }

    public void shoot(ItemStack eject) {
        this.cartridge = eject;
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider provider, boolean clientPacket) {
        super.read(tag, provider, clientPacket);
        this.fireRate = tag.getInt("FiringRate");
        this.firingCooldown = tag.getInt("Cooldown");
        this.animateTicks = tag.getInt("AnimateTicks");
        this.isOpen = tag.getBoolean("Open");
        this.openProgress = tag.getFloat("OpenProgress");
        this.cartridge = ItemStack.parseOptional(provider, tag.getCompound("Cartridge"));

        if (!clientPacket) return;
        this.updateInstance = tag.contains("UpdateInstance");
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider provider, boolean clientPacket) {
        super.write(tag, provider, clientPacket);
        tag.putInt("FiringRate", this.fireRate);
        tag.putInt("Cooldown", this.firingCooldown);
        tag.putInt("AnimateTicks", this.animateTicks);
        tag.putBoolean("Open", isOpen);
        tag.putFloat("OpenProgress", openProgress);
        tag.put("Cartridge", this.cartridge.saveOptional(provider));

        if (!clientPacket) return;
        if (this.updateInstance) tag.putBoolean("UpdateInstance", true);
    }

    public ItemStack extractNextInput() {
        ItemStack cart = cartridge.copy();
        cartridge = ItemStack.EMPTY;
        return cart;
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> list = super.getDrops();
        if (this.cartridge != ItemStack.EMPTY) list.add(this.cartridge.copy());
        return list;
    }

    @Override
    protected AABB createRenderBoundingBox() {
        Direction dir = this.getBlockState().getValue(HeavyAutocannonBreechBlock.FACING);
        Direction ammoContainerDir = dir.getAxis().isHorizontal() ? dir.getClockWise(Direction.Axis.Y) : Direction.EAST;
        return super.createRenderBoundingBox().expandTowards(new Vec3(dir.getOpposite().step()).add(new Vec3(ammoContainerDir.step())));
    }

    @Override
    public boolean isManual() {
        return true;
    }

    public boolean isLoaded() { return cartridge != ItemStack.EMPTY; }

    public float getOpenProgress(float partialTicks) {
        return openProgress / (2f * openLimit);
    }
}