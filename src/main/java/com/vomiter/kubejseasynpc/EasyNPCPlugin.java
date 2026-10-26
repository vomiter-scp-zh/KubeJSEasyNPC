package com.vomiter.kubejseasynpc;

import com.vomiter.kubejseasynpc.event.EasyNPCEvents;
import com.vomiter.kubejseasynpc.event.EasyNPCStartupEvents;
import com.vomiter.kubejseasynpc.event.RegisterObjectiveEventJS;
import com.vomiter.kubejseasynpc.objective.ScriptGoalBuilder;
import com.vomiter.kubejseasynpc.objective.ScriptObjectiveBuilder;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;

public class EasyNPCPlugin extends KubeJSPlugin {
    @Override
    public void registerEvents() {
        EasyNPCEvents.GROUP.register();
        EasyNPCStartupEvents.GROUP.register();
    }

    @Override
    public void initStartup() {
        RegisterObjectiveEventJS.postRegistration();
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        if (event.getType() == ScriptType.STARTUP) {
            event.add("ScriptObjectiveBuilder", ScriptObjectiveBuilder.class);
        }
        if (event.getType() == ScriptType.STARTUP || event.getType() == ScriptType.SERVER) {
            event.add("ScriptGoalBuilder", ScriptGoalBuilder.class);
        }
        if (event.getType() == ScriptType.SERVER) {
            event.add("EasyNPC", EasyNPCBindings.class);
        }
    }
}
