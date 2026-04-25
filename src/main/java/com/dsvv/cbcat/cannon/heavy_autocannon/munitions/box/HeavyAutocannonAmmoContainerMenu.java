package com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.HeavyAutocannonAmmoItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.HeavyAutocannonAmmoType;
import com.dsvv.cbcat.registry.MenuRegister;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.ItemStackServerData;
import rbasamoyai.createbigcannons.base.SimpleValueContainer;
import rbasamoyai.createbigcannons.index.CBCDataComponents;

import javax.annotation.Nullable;

public class HeavyAutocannonAmmoContainerMenu extends AbstractContainerMenu implements SimpleValueContainer {
    private static final ResourceLocation TRACER_SLOT = CreateBigCannons.resource("item/tracer_slot");

    public static HeavyAutocannonAmmoContainerMenu getServerMenuForItemStack(int id, Inventory playerInv, ItemStack stack, boolean isCreative) {
        IHeavyAutocannonAmmoContainerContainer ct = new HeavyAutocannonAmmoContainerItemContainer(stack);
        return new HeavyAutocannonAmmoContainerMenu(MenuRegister.HEAVY_AUTOCANNON_AMMO_BOX.get(), id, playerInv, ct, new ItemStackServerData(stack, CBCDataComponents.TRACER_SPACING), isCreative, true);
    }

    public static HeavyAutocannonAmmoContainerMenu getServerMenuForBlockEntity(int id, Inventory playerInv, HeavyAutocannonAmmoContainerBlockEntity be, boolean isCreative) {
        return new HeavyAutocannonAmmoContainerMenu(MenuRegister.HEAVY_AUTOCANNON_AMMO_BOX.get(), id, playerInv, be, new HeavyAutocannonAmmoContainerServerData(be), isCreative, false);
    }

    public static HeavyAutocannonAmmoContainerMenu getClientMenu(MenuType<HeavyAutocannonAmmoContainerMenu> type, int id, Inventory playerInv, RegistryFriendlyByteBuf buf) {
        boolean isCreative = buf.readBoolean();
        ContainerData data = new SimpleContainerData(1);
        data.set(0, buf.readVarInt());
        boolean isBlock = buf.readBoolean();
        IHeavyAutocannonAmmoContainerContainer ct;
        if (isBlock) {
            BlockPos pos = buf.readBlockPos();
            BlockEntity be = playerInv.player.level().getBlockEntity(pos);
            ct = new HeavyAutocannonAmmoContainerBlockEntityContainerWrapper(be instanceof HeavyAutocannonAmmoContainerBlockEntity abe ? abe : null, pos);
        } else {
            ct = new HeavyAutocannonAmmoContainerItemContainer(ItemStack.STREAM_CODEC.decode(buf));
        }
        return new HeavyAutocannonAmmoContainerMenu(type, id, playerInv, ct, data, isCreative, !isBlock);
    }

    private final boolean isCreative;
    private final IHeavyAutocannonAmmoContainerContainer container;
    private final ContainerData data;
    private final Inventory playerInv;
    private final boolean isItem;

    protected HeavyAutocannonAmmoContainerMenu(MenuType<? extends HeavyAutocannonAmmoContainerMenu> type, int id, Inventory playerInv,
                                          IHeavyAutocannonAmmoContainerContainer ct, ContainerData data, boolean isCreative, boolean isItem) {
        super(type, id);

        this.addSlot(new HeavyAutocannonAmmoContainerMenuSlot(ct, IHeavyAutocannonAmmoContainerContainer.AMMO_SLOT, 32, 26, isCreative));
        this.addSlot(new HeavyAutocannonAmmoContainerMenuSlot(ct, IHeavyAutocannonAmmoContainerContainer.TRACER_SLOT, 59, 26, isCreative) {
            @Nullable
            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, TRACER_SLOT);
            }
        });

        int add = isCreative ? 18 : 8;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInv, row * 9 + col + 9, col * 18 + add, row * 18 + 105));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInv, i, i * 18 + add, 163));
        }

        this.addDataSlots(data);
        this.data = data;
        this.container = ct;
        this.playerInv = playerInv;
        this.isCreative = isCreative;
        this.isItem = isItem;

        this.container.startOpen(playerInv.player);
    }

    @Override public boolean stillValid(Player player) { return this.container.stillValid(player); }

    public int getValue() { return this.data.get(0); }
    public boolean isCreativeContainer() { return this.isCreative; };

    @Override public void setValue(int value) { this.data.set(0, value); }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (index < 2) {
                if (!this.moveItemStackTo(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!(itemStack2.getItem() instanceof HeavyAutocannonAmmoItem ammoItem)) return ItemStack.EMPTY;
                HeavyAutocannonAmmoType ammoType = ammoItem.getType();
                if (ammoType == HeavyAutocannonAmmoType.NONE) return ItemStack.EMPTY;

                HeavyAutocannonAmmoType ctType = this.container.getAmmoType();
                int buf = ctType == HeavyAutocannonAmmoType.NONE
                        ? ammoType.getCapacity()
                        : Math.max(ctType.getCapacity() - this.container.getTotalCount(), 0);
                if (buf < 1) return ItemStack.EMPTY;

                int insertIndex = ammoItem.isTracer(itemStack2) ? 1 : 0;
                Slot insertSlot = this.slots.get(insertIndex);
                if (insertSlot != null) insertSlot.safeInsert(itemStack2);
            }

            if (itemStack2.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();

            if (itemStack2.getCount() == itemStack.getCount()) return ItemStack.EMPTY;
            slot.onTake(player, itemStack2);
        }
        return itemStack;
    }

    public boolean isFilled() { return this.container.getAmmoType() != HeavyAutocannonAmmoType.NONE; }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (slotId == this.playerInv.selected + 29 && clickType != ClickType.THROW && this.isItem) return;
        super.clicked(slotId, button, clickType, player);
    }

    public IHeavyAutocannonAmmoContainerContainer getContainer() { return this.container; }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }
}
