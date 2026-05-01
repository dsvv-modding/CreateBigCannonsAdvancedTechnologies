package com.dsvv.cbcat.cannon.heavy_autocannon.qf_breech;

import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBlockEntity;
import com.dsvv.cbcat.cannon.heavy_autocannon.IHeavyAutocannonBreechBE;
import com.dsvv.cbcat.cannon.heavy_autocannon.breech.HeavyAutocannonBreechBlock;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.HeavyAutocannonCartridgeItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box.HeavyAutocannonAmmoContainerItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.charges.HeavyAutocannonPowderCharge;
import com.dsvv.cbcat.registry.ItemRegister;
import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.AnimatedAutocannon;
import rbasamoyai.createbigcannons.config.CBCConfigs;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.dsvv.cbcat.debugUtils.DebugUtils.displayCustomClientMessage;
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

    private final Deque<ItemStack> inputBuffer = new LinkedList<>();
    private ItemStack outputBuffer = ItemStack.EMPTY;

    private ItemStack cartridge = ItemStack.EMPTY;
    //private ItemStack projectile = ItemStack.EMPTY;
    //private int charges = 0;
    //private int saveCharges = 0;

    public HeavyAutocannonQuickFireBreechBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        this.allTick(this.getLevel());
    }

    @Override
    public void tickFromContraption(Level level, PitchOrientedContraptionEntity poce, BlockPos localPos) {
        super.tickFromContraption(level, poce, localPos);
        this.allTick(level);

        if (openProgress < openLimit && isOpen)
            openProgress++;
        else if (openProgress > 0 && !isOpen)
            openProgress--;

        if (!level.isClientSide && this.updateInstance) {
            this.updateInstance = false;
            Contraption contraption = poce.getContraption();
            writeAndSyncSingleBlockData(this, contraption.getBlocks().get(localPos), poce, contraption);
        }
    }

    private void allTick(Level level) {
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

    /*public void setMaxCharges(int maxCharges) {
        this.saveCharges = maxCharges;
    }

    public void addCharge() {
        this.charges++;
    }

    public boolean isSaveLoad() {
        return (saveCharges >= charges && cartridge.isEmpty() ^ projectile.isEmpty());
    }

    public void setProjectile(ItemStack projectile) {
        this.projectile = projectile;
    }

    public float getCharge() {
        return charges;
    }*/

    public void setCartridge(ItemStack cartridge) {
        this.cartridge = cartridge;
    }

    public boolean getOpen() { return openProgress >= openLimit && isOpen; }

    public boolean willBeOpen() { return isOpen; }

    public void toggleOpen() { isOpen = !isOpen; }

    public void shoot(ItemStack eject) {
        this.cartridge = eject;
        //this.projectile = ItemStack.EMPTY;
        //this.charges = 0;
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        this.fireRate = tag.getInt("FiringRate");
        this.firingCooldown = tag.getInt("Cooldown");
        this.animateTicks = tag.getInt("AnimateTicks");
        this.outputBuffer = tag.contains("Output") ? ItemStack.of(tag.getCompound("Output")) : ItemStack.EMPTY;
        this.isOpen = tag.getBoolean("Open");
        this.openProgress = tag.getFloat("OpenProgress");
        this.cartridge = ItemStack.of(tag.getCompound("Cartridge"));

        this.inputBuffer.clear();
        ListTag inputTag = tag.getList("Input", Tag.TAG_COMPOUND);
        for (int i = 0; i < inputTag.size(); ++i) {
            this.inputBuffer.add(ItemStack.of(inputTag.getCompound(i)));
        }
        //this.projectile = ItemStack.of(tag.getCompound("Projectile"));
        //this.saveCharges = tag.getInt("SaveCharges");
        //his.charges = tag.getInt("Charges");

        if (!clientPacket) return;
        this.updateInstance = tag.contains("UpdateInstance");
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.putInt("FiringRate", this.fireRate);
        tag.putInt("Cooldown", this.firingCooldown);
        tag.putInt("AnimateTicks", this.animateTicks);
        if (this.outputBuffer != null && !this.outputBuffer.isEmpty()) tag.put("Output", this.outputBuffer.save(new CompoundTag()));
        tag.putBoolean("Open", isOpen);
        tag.putFloat("OpenProgress", openProgress);
        tag.put("Cartridge", this.cartridge.save(new CompoundTag()));

        if (!this.inputBuffer.isEmpty()) {
            tag.put("Input", this.inputBuffer.stream()
                    .map(s -> s.save(new CompoundTag()))
                    .collect(Collectors.toCollection(ListTag::new)));
        }
        //tag.put("Projectile", projectile.getOrCreateTag());
        //tag.putInt("SaveCharges", saveCharges);
        //tag.putInt("Charges", charges);

        if (!clientPacket) return;
        if (this.updateInstance) tag.putBoolean("UpdateInstance", true);
    }

    /*public boolean isInputFull() { return this.inputBuffer.size() >= this.getQueueLimit() || !this.magazine.isEmpty(); }
    public boolean isOutputFull() { return !this.outputBuffer.isEmpty(); }

    public ItemStack insertOutput(ItemStack stack) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        if (this.isOutputFull()) return stack;
        this.outputBuffer = stack;
        return ItemStack.EMPTY;
    }*/

    public ItemStack extractNextInput() {
        //displayCustomClientMessage(cartridge.toString());
        ItemStack cart = cartridge.copy();
        cartridge = ItemStack.EMPTY;
        return cart;//!projectile.isEmpty() ? projectile : cartridge;
        /*if (!this.inputBuffer.isEmpty()) return this.inputBuffer.poll();
        if (this.magazine.isEmpty()) return ItemStack.EMPTY;
        int totalCount = HeavyAutocannonAmmoContainerItem.getTotalAmmoCount(this.magazine);
        if (totalCount == 0) return ItemStack.EMPTY;
        if (totalCount == 1) this.updateInstance = true;
        return HeavyAutocannonAmmoContainerItem.pollItemFromContainer(this.magazine);*/
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> list = super.getDrops();
        for (ItemStack s : this.inputBuffer) {
            if (!s.isEmpty()) list.add(s.copy());
        }
        if (!this.outputBuffer.isEmpty()) list.add(this.outputBuffer.copy());
        if (this.cartridge != ItemStack.EMPTY) list.add(this.cartridge.copy());
        //if (this.projectile != ItemStack.EMPTY) list.add(projectile);
        //if (charges > 0) list.add(new ItemStack(ItemRegister.HEAVY_AUTOCANNON_POWDER_CHARGE, charges));
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