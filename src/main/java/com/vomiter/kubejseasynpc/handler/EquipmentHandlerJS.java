package com.vomiter.kubejseasynpc.handler;

import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.handler.EquipmentHandler;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public final class EquipmentHandlerJS extends NPCComponent {
    public EquipmentHandlerJS(NPCHandle npc) { super(npc); }

    public ItemStack getItem(EquipmentSlot slot) {
        return entity().getLivingEntity().getItemBySlot(slot).copy();
    }
    public boolean setItem(EquipmentSlot slot, ItemStack stack) {
        return EquipmentHandler.setEquipmentSlotItem(entity(), slot, stack.copy());
    }
    public boolean clearItem(EquipmentSlot slot) { return setItem(slot, ItemStack.EMPTY); }
}
