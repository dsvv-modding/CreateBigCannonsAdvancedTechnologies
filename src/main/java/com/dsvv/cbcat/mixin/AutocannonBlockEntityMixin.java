package com.dsvv.cbcat.mixin;

import com.dsvv.cbcat.base.LeavesCannonBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlockEntity;

@Mixin(SmartBlockEntity.class) //not used as well
public abstract class AutocannonBlockEntityMixin implements LeavesCannonBlockEntity {
    private ItemStack leavesItemStack = ItemStack.EMPTY;
    private boolean updateInstance = true;

    public ItemStack getLeavesItemStack() { return leavesItemStack; }

    public void setLeavesItemStack(ItemStack leavesItemStack) {
        this.leavesItemStack = leavesItemStack;
        this.leavesItemStack.setCount(1);
        this.updateInstance = true;
    }

    public boolean getUpdateInstance() {
        return this.updateInstance;
    }

    public void setUpdateInstance(boolean updateInstance) {
        this.updateInstance = updateInstance;
    }

    //@Inject(method = "Lcom/simibubi/create/foundation/blockEntity/SmartBlockEntity;read(Lnet/minecraft/nbt/CompoundTag;Z)V", at = @At("TAIL"), remap = false)
    @Inject(method = "read", at = @At("TAIL"), remap = false)
    protected void readLeaves(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci) {
        if ((Object)this instanceof AutocannonBlockEntity)
            this.leavesItemStack = ItemStack.parseOptional(registries, tag.getCompound("Leaves"));
    }

    //@Inject(method = "Lcom/simibubi/create/foundation/blockEntity/SmartBlockEntity;write(Lnet/minecraft/nbt/CompoundTag;Z)V", at = @At("TAIL"), remap = false)
    @Inject(method = "write", at = @At("TAIL"), remap = false)
    protected void writeLeaves(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci) {
        if ((Object)this instanceof AutocannonBlockEntity)
            tag.put("Leaves", leavesItemStack.save(registries));
    }

    //@Inject(method = "getDrops()V", at = @At(value = "TAIL", target = "Lrbasamoyai/createbigcannons/cannons/autocannon;getDrops()V", remap = false))
    //@Inject(method = "getDrops", at = @At("RETURN"), cancellable = true)
    //@ModifyReturnValue(method = "get", at = @At("RETURN"), remap = false)
    /*public List<ItemStack> getDrops() {
        List<ItemStack> list = IAutocannonBlockEntity.super.getDrops();
        if (!leavesItemStack.isEmpty())
            list.add(leavesItemStack);
        return list;
    }*/
}
