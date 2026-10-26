package com.vomiter.kubejseasynpc.handler.attribute;

import com.vomiter.kubejseasynpc.util.ScriptValidation;
import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.api.handler.EasyNPCAttributeHandler;
import de.markusbordihn.easynpc.data.attribute.MovementAttributeType;
import de.markusbordihn.easynpc.data.attribute.MovementAttributes;
import de.markusbordihn.easynpc.data.attribute.NavigationType;

public final class MovementAttributeHandlerJS extends NPCComponent {
    public MovementAttributeHandlerJS(NPCHandle npc) { super(npc); }
    public boolean setBoolean(MovementAttributeType type, boolean value) {
        return EasyNPCAttributeHandler.setMovementAttribute(entity(), type, value);
    }
    public MovementAttributes getValues() {
        return entity().getEasyNPCAttributeData().getEntityAttributes().getMovementAttributes();
    }
    public boolean setNumber(MovementAttributeType type, double value) {
        ScriptValidation.finite(value, "attribute value");
        return EasyNPCAttributeHandler.setMovementAttribute(entity(), type, value);
    }
    public boolean setNavigationType(NavigationType type) {
        return EasyNPCAttributeHandler.setNavigationType(entity(), type);
    }
}
