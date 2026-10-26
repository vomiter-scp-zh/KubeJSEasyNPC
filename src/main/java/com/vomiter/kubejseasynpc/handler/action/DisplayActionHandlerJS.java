package com.vomiter.kubejseasynpc.handler.action;

import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.api.action.EasyNPCActionHandler;

public final class DisplayActionHandlerJS extends NPCComponent {
    public DisplayActionHandlerJS(NPCHandle npc) { super(npc); }
    public boolean setOpacity(int opacity) { return EasyNPCActionHandler.setOpacity(entity(), opacity); }
    public int getOpacity() { return EasyNPCActionHandler.getOpacity(entity()); }
}
