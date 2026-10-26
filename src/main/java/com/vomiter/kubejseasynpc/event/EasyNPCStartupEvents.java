package com.vomiter.kubejseasynpc.event;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface EasyNPCStartupEvents {
    EventGroup GROUP = EventGroup.of("EasyNPCStartupEvents");
    EventHandler REGISTER_OBJECTIVE = GROUP.startup("registerObjective", () -> RegisterObjectiveEventJS.class);
}
