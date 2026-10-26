package com.vomiter.kubejseasynpc.handler.attribute;

import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.api.handler.EasyNPCAttributeHandler;
import de.markusbordihn.easynpc.data.attribute.EnvironmentalAttributeType;
import de.markusbordihn.easynpc.data.attribute.EnvironmentalAttributes;

public final class EnvironmentalAttributeHandlerJS extends NPCComponent {
    public EnvironmentalAttributeHandlerJS(NPCHandle npc) { super(npc); }
    public boolean setBoolean(EnvironmentalAttributeType type, boolean value) {
        return EasyNPCAttributeHandler.setEnvironmentalAttribute(entity(), type, value);
    }
    public EnvironmentalAttributes getValues() {
        return entity().getEasyNPCAttributeData().getEntityAttributes().getEnvironmentalAttributes();
    }
}
