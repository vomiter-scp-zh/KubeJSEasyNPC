package com.vomiter.kubejseasynpc.handler.action;

import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.api.action.EasyNPCActionHandler;
import de.markusbordihn.easynpc.data.state.StateEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public final class StateActionHandlerJS extends NPCComponent {
    public StateActionHandlerJS(NPCHandle npc) { super(npc); }
    @Nullable
    public StateEntry get(ResourceLocation id) { return EasyNPCActionHandler.getState(entity(), id); }
    public boolean set(ResourceLocation id, StateEntry value) { return setBy(id, value, null); }
    public boolean setBy(ResourceLocation id, StateEntry value, @Nullable ServerPlayer initiator) {
        if (initiator != null) checkPlayer(initiator);
        return EasyNPCActionHandler.setState(entity(), id, value, initiator);
    }
    public StateEntry createNumber(int value) { return StateEntry.of(value); }
    public StateEntry createBoolean(boolean value) { return StateEntry.of(value); }
    public StateEntry createText(String value) {
        return StateEntry.of(value);
    }
    public boolean setNumber(ResourceLocation id, int value) { return set(id, createNumber(value)); }
    public boolean setBoolean(ResourceLocation id, boolean value) { return set(id, createBoolean(value)); }
    public boolean setText(ResourceLocation id, String value) { return set(id, createText(value)); }
}
