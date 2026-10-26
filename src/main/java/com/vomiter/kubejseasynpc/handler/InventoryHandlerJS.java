package com.vomiter.kubejseasynpc.handler;

import com.vomiter.kubejseasynpc.util.ScriptValidation;
import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.entity.easynpc.data.InventoryDataCapable;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

/** NPC storage slots, distinct from inventory behavior attributes and equipment. */
public final class InventoryHandlerJS extends NPCComponent {
    public InventoryHandlerJS(NPCHandle npc) { super(npc); }

    private SimpleContainer inventory() {
        InventoryDataCapable<?> data = entity().getEasyNPCInventoryData();
        if (data == null || data.getEasyNPCInventory() == null) {
            throw new IllegalStateException("NPC does not support inventory storage");
        }
        return data.getEasyNPCInventory();
    }
    public boolean isSupported() {
        InventoryDataCapable<?> data = entity().getEasyNPCInventoryData();
        return data != null && data.getEasyNPCInventory() != null;
    }
    public int getSize() { return inventory().getContainerSize(); }
    public boolean isEmpty() { return inventory().isEmpty(); }
    public ItemStack getItem(int slot) {
        SimpleContainer inventory = inventory();
        ScriptValidation.index(slot, inventory.getContainerSize(), "inventory slot");
        return inventory.getItem(slot).copy();
    }
    public List<ItemStack> getItems() {
        SimpleContainer inventory = inventory();
        List<ItemStack> items = new ArrayList<>(inventory.getContainerSize());
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) items.add(inventory.getItem(slot).copy());
        return items;
    }
    public void setItem(int slot, ItemStack stack) {
        SimpleContainer inventory = inventory();
        ScriptValidation.index(slot, inventory.getContainerSize(), "inventory slot");
        int limit = Math.min(inventory.getMaxStackSize(), stack.getMaxStackSize());
        if (!stack.isEmpty() && stack.getCount() > limit) {
            throw new IllegalArgumentException("Stack exceeds slot limit " + limit + "; use insert for overflow handling");
        }
        inventory.setItem(slot, stack.copy());
    }
    /** Does not consume or mutate the argument. Returns the uninserted remainder. */
    public ItemStack insert(ItemStack stack) {
        return inventory().addItem(stack.copy()).copy();
    }
    public ItemStack extract(int slot, int count) {
        SimpleContainer inventory = inventory();
        ScriptValidation.index(slot, inventory.getContainerSize(), "inventory slot");
        if (count <= 0) throw new IllegalArgumentException("Extraction count must be positive");
        return inventory.removeItem(slot, count).copy();
    }
    public void clear() {
        SimpleContainer inventory = inventory();
        inventory.clearContent();
        inventory.setChanged();
    }
}
