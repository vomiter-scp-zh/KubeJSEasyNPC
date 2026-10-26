package com.vomiter.kubejseasynpc.handler.attribute;

import com.vomiter.kubejseasynpc.util.ScriptValidation;
import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.api.handler.EasyNPCAttributeHandler;
import de.markusbordihn.easynpc.data.attribute.InteractionAttributeType;
import de.markusbordihn.easynpc.data.attribute.InteractionAttributes;
import net.minecraft.world.phys.Vec3;

public final class InteractionAttributeHandlerJS extends NPCComponent {
    public InteractionAttributeHandlerJS(NPCHandle npc) { super(npc); }
    public boolean setShoulderOffset(Vec3 offset) {
        return EasyNPCAttributeHandler.setShoulderOffset(entity(), position(offset));
    }
    public boolean setBoolean(InteractionAttributeType type, boolean value) {
        return EasyNPCAttributeHandler.setInteractionAttribute(entity(), type, value);
    }
    public InteractionAttributes getValues() {
        return entity().getEasyNPCAttributeData().getEntityAttributes().getInteractionAttributes();
    }
    public boolean setNumber(InteractionAttributeType type, double value) {
        ScriptValidation.finite(value, "attribute value");
        return EasyNPCAttributeHandler.setInteractionAttribute(entity(), type, value);
    }
}
