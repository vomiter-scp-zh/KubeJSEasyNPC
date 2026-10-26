package com.vomiter.kubejseasynpc.handler;

import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.api.handler.EasyNPCPauseHandler;

public final class EasyNPCPauseHandlerJS extends NPCComponent {
    public EasyNPCPauseHandlerJS(NPCHandle npc) { super(npc); }
    public boolean pause() { return EasyNPCPauseHandler.pause(entity()); }
    public boolean resume() { return EasyNPCPauseHandler.resume(entity()); }
    public boolean isPaused() { return EasyNPCPauseHandler.isPaused(entity()); }
}
