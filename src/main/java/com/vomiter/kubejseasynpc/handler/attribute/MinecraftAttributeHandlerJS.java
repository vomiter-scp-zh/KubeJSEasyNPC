package com.vomiter.kubejseasynpc.handler.attribute;

import com.vomiter.kubejseasynpc.util.ScriptValidation;
import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.api.handler.EasyNPCAttributeHandler;
import net.minecraft.resources.ResourceLocation;

public final class MinecraftAttributeHandlerJS extends NPCComponent {
    public MinecraftAttributeHandlerJS(NPCHandle npc) { super(npc); }
    public boolean setBase(ResourceLocation id, double value) {
        ScriptValidation.finite(value, "attribute value");
        return EasyNPCAttributeHandler.setBaseAttribute(entity(), id, value);
    }
}
