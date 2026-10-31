package com.vomiter.kubejseasynpc;

import com.vomiter.kubejseasynpc.event.EasyNPCEvents;
import com.vomiter.kubejseasynpc.event.EasyNPCStartupEvents;
import com.vomiter.kubejseasynpc.event.RegisterObjectiveEventJS;
import com.vomiter.kubejseasynpc.objective.ScriptGoalBuilder;
import com.vomiter.kubejseasynpc.objective.ScriptObjectiveBuilder;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.ScriptType;

public class EasyNPCPlugin implements KubeJSPlugin {
    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(EasyNPCEvents.GROUP);
        registry.register(EasyNPCStartupEvents.GROUP);
    }

    @Override
    public void initStartup() {
        RegisterObjectiveEventJS.postRegistration();
    }

    @Override
    public void registerBindings(BindingRegistry event) {
        if (event.type() == ScriptType.STARTUP) {
            event.add("ScriptObjectiveBuilder", ScriptObjectiveBuilder.class);
        }
        if (event.type() == ScriptType.STARTUP || event.type() == ScriptType.SERVER) {
            event.add("ScriptGoalBuilder", ScriptGoalBuilder.class);
        }
        if (event.type() == ScriptType.SERVER) {
            event.add("EasyNPC", EasyNPCBindings.class);
        }
    }
}
