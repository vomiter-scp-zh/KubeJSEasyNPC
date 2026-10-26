package com.vomiter.kubejseasynpc.handler.attribute;

import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.api.handler.EasyNPCAttributeHandler;
import de.markusbordihn.easynpc.data.attribute.InventoryAttributeType;
import de.markusbordihn.easynpc.data.attribute.InventoryAttributes;

public final class InventoryAttributeHandlerJS extends NPCComponent {
    public InventoryAttributeHandlerJS(NPCHandle npc) { super(npc); }
    public boolean setBoolean(InventoryAttributeType type, boolean value) {
        return EasyNPCAttributeHandler.setInventoryAttribute(entity(), type, value);
    }
    public InventoryAttributes getValues() {
        return entity().getEasyNPCAttributeData().getEntityAttributes().getInventoryAttributes();
    }
}
