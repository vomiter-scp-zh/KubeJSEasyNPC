package com.vomiter.kubejseasynpc.event;

import com.vomiter.kubejseasynpc.NPCHandle;
import com.vomiter.kubejseasynpc.objective.EasyNPCObjectives;
import com.vomiter.kubejseasynpc.objective.ScriptObjectiveBuilder;
import de.markusbordihn.easynpc.data.objective.ObjectiveDataEntry;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import java.util.function.BiFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.goal.Goal;

/** Registration window fired once by KubeJSPlugin.initStartup, after startup scripts load. */
public final class RegisterObjectiveEventJS extends EventJS {
    private boolean active = true;

    private RegisterObjectiveEventJS() {}

    public void register(ResourceLocation id, BiFunction<NPCHandle, ObjectiveDataEntry, Goal> factory) {
        checkActive();
        EasyNPCObjectives.register(id, factory);
    }

    public void registerConfigured(ResourceLocation id, ScriptObjectiveBuilder definition) {
        checkActive();
        EasyNPCObjectives.registerConfigured(id, definition);
    }

    public boolean isRegistered(ResourceLocation id) {
        return EasyNPCObjectives.isRegistered(id);
    }

    private void checkActive() {
        if (!active) throw new IllegalStateException("Objective registration is only available during EasyNPCStartupEvents.registerObjective");
    }

    public static void postRegistration() {
        RegisterObjectiveEventJS event = new RegisterObjectiveEventJS();
        try {
            EasyNPCStartupEvents.REGISTER_OBJECTIVE.post(ScriptType.STARTUP, event);
        } finally {
            event.active = false;
        }
    }
}
