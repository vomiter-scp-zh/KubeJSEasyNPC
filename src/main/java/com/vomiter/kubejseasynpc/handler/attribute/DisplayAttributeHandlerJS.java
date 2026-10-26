package com.vomiter.kubejseasynpc.handler.attribute;

import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.api.handler.EasyNPCAttributeHandler;
import de.markusbordihn.easynpc.data.display.DisplayAttributeType;
import de.markusbordihn.easynpc.data.type.ValueType;

public final class DisplayAttributeHandlerJS extends NPCComponent {
    public DisplayAttributeHandlerJS(NPCHandle npc) { super(npc); }
    public boolean setBoolean(DisplayAttributeType type, boolean value) {
        requireType(type, ValueType.BOOLEAN);
        return EasyNPCAttributeHandler.setDisplayAttribute(entity(), type, value);
    }
    public boolean setInteger(DisplayAttributeType type, int value) {
        requireType(type, ValueType.INTEGER);
        return EasyNPCAttributeHandler.setDisplayAttribute(entity(), type, value);
    }
    public boolean setText(DisplayAttributeType type, String value) {
        requireType(type, ValueType.STRING);
        return EasyNPCAttributeHandler.setDisplayAttribute(entity(), type, value);
    }
    private static void requireType(DisplayAttributeType type, ValueType expected) {
        if (type.getValueType() != expected) {
            throw new IllegalArgumentException("Display attribute " + type + " requires " + type.getValueType());
        }
    }
}
