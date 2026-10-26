package com.vomiter.kubejseasynpc.objective;

import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.data.objective.ObjectiveDataEntry;
import de.markusbordihn.easynpc.data.objective.ObjectiveGoalFactory;
import de.markusbordihn.easynpc.entity.easynpc.EasyNPC;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;

public final class ScriptObjectiveBuilder {
    private BiFunction<NPCHandle, ObjectiveDataEntry, Goal> goal;
    private BiFunction<NPCHandle, ObjectiveDataEntry, Goal> target;
    private Predicate<NPCHandle> compatible = npc -> true;
    private int priority = ObjectiveDataEntry.DEFAULT_PRIORITY;
    private boolean travel = true;

    private ScriptObjectiveBuilder() {}
    public static ScriptObjectiveBuilder create() { return new ScriptObjectiveBuilder(); }
    public ScriptObjectiveBuilder goal(BiFunction<NPCHandle, ObjectiveDataEntry, Goal> factory) {
        goal = Objects.requireNonNull(factory); return this;
    }
    public ScriptObjectiveBuilder target(BiFunction<NPCHandle, ObjectiveDataEntry, Goal> factory) {
        target = Objects.requireNonNull(factory); return this;
    }
    public ScriptObjectiveBuilder isCompatible(Predicate<NPCHandle> predicate) {
        compatible = Objects.requireNonNull(predicate); return this;
    }
    public ScriptObjectiveBuilder defaultPriority(int value) {
        if (value < 0) throw new IllegalArgumentException("priority must be non-negative");
        priority = value; return this;
    }
    public ScriptObjectiveBuilder hasTravelObjective(boolean value) { travel = value; return this; }

    ObjectiveGoalFactory buildFactory() {
        if (goal == null && target == null) throw new IllegalStateException("A goal or target factory is required");
        // Snapshot the definition: later builder changes cannot alter registered factories.
        BiFunction<NPCHandle, ObjectiveDataEntry, Goal> goalFactory = goal;
        BiFunction<NPCHandle, ObjectiveDataEntry, Goal> targetFactory = target;
        Predicate<NPCHandle> predicate = compatible;
        int defaultPriority = priority;
        boolean hasTravel = travel;
        return new ObjectiveGoalFactory() {
            @Override public Goal createGoal(ObjectiveDataEntry data, EasyNPC<?> npc) {
                return goalFactory == null ? null : goalFactory.apply(handle(npc), data);
            }
            @Override public Goal createTarget(ObjectiveDataEntry data, EasyNPC<?> npc) {
                return targetFactory == null ? null : targetFactory.apply(handle(npc), data);
            }
            @Override public boolean isCompatible(EasyNPC<?> npc) { return predicate.test(handle(npc)); }
            @Override public int getDefaultPriority() { return defaultPriority; }
            @Override public boolean hasTravelObjective() { return hasTravel; }
        };
    }
    private static NPCHandle handle(EasyNPC<?> npc) {
        ServerLevel level = Objects.requireNonNull(npc.getEntityServerLevel(), "server-side NPC required");
        return new NPCHandle(level.getServer(), npc.getEntityUUID());
    }
}
