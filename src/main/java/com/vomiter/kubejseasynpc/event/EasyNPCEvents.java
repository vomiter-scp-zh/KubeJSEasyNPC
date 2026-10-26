package com.vomiter.kubejseasynpc.event;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface EasyNPCEvents {
    EventGroup GROUP = EventGroup.of("EasyNPCEvents");
    EventHandler SPAWNED = GROUP.server("spawned", () -> NPCEventJS.class);
    EventHandler REMOVED = GROUP.server("removed", () -> NPCEventJS.class);
    EventHandler DIALOG_OPENED = GROUP.server("dialogOpened", () -> NPCEventJS.class);
    EventHandler ACTION_EXECUTED = GROUP.server("actionExecuted", () -> NPCEventJS.class);
    EventHandler STATE_CHANGED = GROUP.server("stateChanged", () -> NPCStateEventJS.class);
    EventHandler SCRIPT_ACTION = GROUP.server("scriptAction", () -> NPCEventJS.class);
}
