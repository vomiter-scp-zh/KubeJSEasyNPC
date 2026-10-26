package com.vomiter.kubejseasynpc.handler;

import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.api.handler.EasyNPCShoulderHandler;
import net.minecraft.server.level.ServerPlayer;

public final class EasyNPCShoulderHandlerJS extends NPCComponent {
    public EasyNPCShoulderHandlerJS(NPCHandle npc) { super(npc); }
    public boolean sitOn(ServerPlayer player) { checkPlayer(player); return EasyNPCShoulderHandler.sitOnShoulder(entity(), player); }
    public boolean isMounted() { checkThread(); return EasyNPCShoulderHandler.isOnShoulder(npc.getUuid()); }
    public long getRemountAllowedGameTime() { return EasyNPCShoulderHandler.getRemountAllowedGameTime(entity()); }
}
