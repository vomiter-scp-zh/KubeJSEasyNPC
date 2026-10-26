package com.vomiter.kubejseasynpc.handler.action;

import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.api.action.EasyNPCActionHandler;
import de.markusbordihn.easynpc.data.action.ActionContext;
import de.markusbordihn.easynpc.data.action.MoveActionData;
import net.minecraft.core.BlockPos;

public final class MovementActionHandlerJS extends NPCComponent {
    public MovementActionHandlerJS(NPCHandle npc) { super(npc); }
    public MoveActionData createData() { return MoveActionData.DEFAULT; }
    public boolean moveTo(BlockPos target) {
        return EasyNPCActionHandler.moveTo(entity(), target);
    }
    public boolean moveToWithData(BlockPos target, MoveActionData data) {
        return EasyNPCActionHandler.moveTo(entity(), target, data);
    }
    public boolean moveToWithArrival(BlockPos target, MoveActionData data, Runnable onArrival) {
        return EasyNPCActionHandler.moveTo(entity(), target, data, onArrival);
    }
    public boolean moveToWithContext(BlockPos target, MoveActionData data, ActionContext context, Runnable onArrival) {
        checkContext(context);
        return EasyNPCActionHandler.moveTo(entity(), target, data, context, onArrival);
    }
}
