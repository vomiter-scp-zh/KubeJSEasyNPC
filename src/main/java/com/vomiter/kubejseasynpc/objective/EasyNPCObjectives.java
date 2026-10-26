package com.vomiter.kubejseasynpc.objective;

import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.api.objective.EasyNPCObjectiveRegistry;
import de.markusbordihn.easynpc.data.objective.ObjectiveDataEntry;
import de.markusbordihn.easynpc.data.objective.ObjectiveGoalFactory;
import java.util.Objects;
import java.util.function.BiFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.goal.Goal;

public final class EasyNPCObjectives {
    private EasyNPCObjectives() {}
    public static void register(ResourceLocation id, BiFunction<NPCHandle, ObjectiveDataEntry, Goal> factory) {
        registerConfigured(id, ScriptObjectiveBuilder.create().goal(factory));
    }
    public static synchronized void registerConfigured(ResourceLocation id, ScriptObjectiveBuilder definition) {
        Objects.requireNonNull(id, "objective ID");
        ObjectiveGoalFactory factory = Objects.requireNonNull(definition, "objective definition").buildFactory();
        if (EasyNPCObjectiveRegistry.isRegistered(id)) {
            throw new IllegalArgumentException("Objective already registered: " + id + "; restart the game/server to change startup registrations");
        }
        EasyNPCObjectiveRegistry.register(id, factory);
        if (EasyNPCObjectiveRegistry.get(id) != factory) {
            throw new IllegalStateException("Could not register objective: " + id);
        }
    }
    public static boolean isRegistered(ResourceLocation id) {
        return EasyNPCObjectiveRegistry.isRegistered(Objects.requireNonNull(id));
    }
}
