package com.vomiter.kubejseasynpc.handler.action;

import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.api.action.EasyNPCActionHandler;
import de.markusbordihn.easynpc.data.action.ActionContext;
import de.markusbordihn.easynpc.data.action.ActionDataEntry;
import de.markusbordihn.easynpc.data.action.ActionDataSet;
import de.markusbordihn.easynpc.data.action.ActionEventSet;
import de.markusbordihn.easynpc.data.action.ActionEventType;
import de.markusbordihn.easynpc.entity.easynpc.data.ActionEventDataCapable;
import java.util.List;
import java.util.UUID;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public final class ActionHandlerJS extends NPCComponent {
    public ActionHandlerJS(NPCHandle npc) { super(npc); }
    private ActionEventDataCapable<?> actionEvents() {
        ActionEventDataCapable<?> data = entity().getEasyNPCActionEventData();
        if (data == null) throw new IllegalStateException("NPC does not support action events");
        return data;
    }

    /** Detached snapshot; changing it requires setEventActions to commit. */
    public ActionDataSet getEventActions(ActionEventType type) {
        ActionDataSet actions = actionEvents().getActionDataSet(type);
        return actions == null ? new ActionDataSet() : new ActionDataSet(actions.createTag());
    }

    public boolean hasEventActions(ActionEventType type) {
        return actionEvents().hasActionEvent(type);
    }

    /** Replaces one event's actions while preserving every other event. */
    public void setEventActions(ActionEventType type, ActionDataSet actions) {
        ActionDataSet copy = new ActionDataSet(actions.createTag());
        ActionEventDataCapable<?> data = actionEvents();
        ActionEventSet current = data.getActionEventSet();
        ActionEventSet events = current == null ? new ActionEventSet() : new ActionEventSet(current.createTag());
        events.setActionEvent(type, copy);
        // Upstream updates the interaction flag, marks data changed and cancels pending actions.
        data.setActionEventSet(events);
    }

    public void addEventAction(ActionEventType type, ActionDataEntry action) {
        ActionDataSet actions = getEventActions(type);
        actions.add(action);
        setEventActions(type, actions);
    }

    public boolean removeEventAction(ActionEventType type, UUID actionId) {
        ActionDataSet actions = getEventActions(type);
        int previousSize = actions.size();
        actions.remove(actionId);
        if (actions.size() == previousSize) return false;
        setEventActions(type, actions);
        return true;
    }

    public void clearEventActions(ActionEventType type) {
        setEventActions(type, new ActionDataSet());
    }

    public void clearAllEventActions() {
        actionEvents().clearActionEventSet();
    }

    public boolean updateScoreboard(ServerPlayer player, String command) {
        checkPlayer(player);
        return EasyNPCActionHandler.updateScoreboard(entity(), player, command);
    }
    public boolean execute(ActionDataEntry action) { return executeWithContext(action, ActionContext.EMPTY); }
    public boolean executeWithContext(ActionDataEntry action, ActionContext context) {
        checkContext(context);
        return EasyNPCActionHandler.execute(entity(), action, context);
    }
    public boolean trigger(ActionEventType type) { return triggerWithContext(type, ActionContext.EMPTY); }
    public boolean triggerWithContext(ActionEventType type, ActionContext context) {
        checkContext(context);
        return EasyNPCActionHandler.trigger(entity(), type, context);
    }
    public boolean schedule(ResourceLocation sourceId, int ticks, List<ActionDataEntry> actions) {
        return EasyNPCActionHandler.schedule(entity(), sourceId, ticks, List.copyOf(actions));
    }
    public boolean cancelScheduled(ResourceLocation sourceId) { return EasyNPCActionHandler.cancelScheduled(entity(), sourceId); }
    public boolean hasScheduled(ResourceLocation sourceId) { return EasyNPCActionHandler.hasScheduled(entity(), sourceId); }
}
