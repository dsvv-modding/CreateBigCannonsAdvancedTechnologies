package com.dsvv.cbcat.cannon.heavy_autocannon.recoil_spring;

import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.AnimatedAutocannon;

import java.util.HashMap;
import java.util.Map;

public class HeavyAutocannonRecoilSpringBlockEntity extends HeavyAutocannonBlockEntity implements AnimatedAutocannon
{
    public Map<BlockPos, BlockState> toAnimate = new HashMap<>();

    private int animateTicks = 5;

    public HeavyAutocannonRecoilSpringBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
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
        if (level.isClientSide && poce.getContraption().getBlockEntityClientSide(localPos) instanceof HeavyAutocannonRecoilSpringBlockEntity cbe) {
            cbe.allTick();
        }
    }

    private void allTick() {
        if (this.animateTicks < 5) ++this.animateTicks;
        if (this.animateTicks < 0) this.animateTicks = 0;
    }

    public void handleFiring() {
        this.animateTicks = 0;
    }

    public float getAnimateOffset(float partialTicks) {
        float t = ((float) this.animateTicks + partialTicks) * 1.2f;
        if (t <= 0 || t >= 4.8f) return 1;
        float f = t < 1 ? t : (4.8f - t) / 3.8f;
        return Mth.cos(f * Mth.HALF_PI);
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider provider, boolean clientPacket) {
        super.write(tag, provider, clientPacket);

        tag.putInt("AnimateTicks", this.animateTicks);

        ListTag renderedList = new ListTag();
        for (Map.Entry<BlockPos, BlockState> entry : this.toAnimate.entrySet()) {
            CompoundTag block = new CompoundTag();
            block.put("Pos", NbtUtils.writeBlockPos(entry.getKey()));
            block.put("Block", NbtUtils.writeBlockState(entry.getValue()));
            renderedList.add(block);
        }

        tag.put("RenderedBlocks", renderedList);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider provider, boolean clientPacket) {
        super.read(tag, provider, clientPacket);

        this.animateTicks = tag.getInt("AnimateTicks");

        this.toAnimate.clear();
        ListTag renderedList = tag.getList("RenderedBlocks", Tag.TAG_COMPOUND);
        for (int i = 0; i < renderedList.size(); ++i) {
            CompoundTag block = renderedList.getCompound(i);
            this.toAnimate.put(NbtUtils.readBlockPos(block, "Pos").get(),
                    NbtUtils.readBlockState(this.blockHolderGetter(), block.getCompound("Block")));
        }
    }

    @Override public void incrementAnimationTicks() { ++this.animateTicks; }
    @Override public int getAnimationTicks() { return this.animateTicks; }
}
