package com.vomiter.kubejseasynpc.handler.attribute;

import com.vomiter.kubejseasynpc.util.ScriptValidation;
import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.api.handler.EasyNPCAttributeHandler;
import de.markusbordihn.easynpc.data.attribute.CombatAttributeType;
import de.markusbordihn.easynpc.data.attribute.CombatAttributes;

public final class CombatAttributeHandlerJS extends NPCComponent {
    public CombatAttributeHandlerJS(NPCHandle npc) { super(npc); }
    public boolean setBoolean(CombatAttributeType type, boolean value) {
        return EasyNPCAttributeHandler.setCombatAttribute(entity(), type, value);
    }
    public CombatAttributes getValues() {
        return entity().getEasyNPCAttributeData().getEntityAttributes().getCombatAttributes();
    }
    public boolean setNumber(CombatAttributeType type, double value) {
        ScriptValidation.finite(value, "attribute value");
        return EasyNPCAttributeHandler.setCombatAttribute(entity(), type, value);
    }
}
