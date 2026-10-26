package com.vomiter.kubejseasynpc.objective;

import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import net.minecraft.world.entity.ai.goal.Goal;

public final class ScriptGoalBuilder {
    private BooleanSupplier canUse;
    private BooleanSupplier canContinue;
    private BooleanSupplier interruptable = () -> true;
    private BooleanSupplier everyTick = () -> false;
    private Runnable start = () -> {};
    private Runnable tick = () -> {};
    private Runnable stop = () -> {};
    private EnumSet<Goal.Flag> flags = EnumSet.noneOf(Goal.Flag.class);

    private ScriptGoalBuilder() {}
    public static ScriptGoalBuilder create() { return new ScriptGoalBuilder(); }
    public ScriptGoalBuilder canUse(BooleanSupplier callback) { canUse = Objects.requireNonNull(callback); return this; }
    public ScriptGoalBuilder canContinueToUse(BooleanSupplier callback) { canContinue = Objects.requireNonNull(callback); return this; }
    public ScriptGoalBuilder isInterruptable(BooleanSupplier callback) { interruptable = Objects.requireNonNull(callback); return this; }
    public ScriptGoalBuilder requiresUpdateEveryTick(BooleanSupplier callback) { everyTick = Objects.requireNonNull(callback); return this; }
    public ScriptGoalBuilder start(Runnable callback) { start = Objects.requireNonNull(callback); return this; }
    public ScriptGoalBuilder tick(Runnable callback) { tick = Objects.requireNonNull(callback); return this; }
    public ScriptGoalBuilder stop(Runnable callback) { stop = Objects.requireNonNull(callback); return this; }
    public ScriptGoalBuilder flags(List<String> names) {
        EnumSet<Goal.Flag> parsed = EnumSet.noneOf(Goal.Flag.class);
        for (String name : Objects.requireNonNull(names)) {
            parsed.add(Goal.Flag.valueOf(Objects.requireNonNull(name).toUpperCase(Locale.ROOT)));
        }
        flags = parsed;
        return this;
    }
    public Goal build() {
        return new ScriptGoal(Objects.requireNonNull(canUse, "canUse callback is required"),
            canContinue, interruptable, everyTick, start, tick, stop, flags.clone());
    }

    private static final class ScriptGoal extends Goal {
        private final BooleanSupplier canUse;
        private final BooleanSupplier canContinue;
        private final BooleanSupplier interruptable;
        private final BooleanSupplier everyTick;
        private final Runnable start;
        private final Runnable tick;
        private final Runnable stop;
        private ScriptGoal(BooleanSupplier canUse, BooleanSupplier canContinue,
                           BooleanSupplier interruptable, BooleanSupplier everyTick,
                           Runnable start, Runnable tick, Runnable stop, EnumSet<Flag> flags) {
            this.canUse = canUse;
            this.canContinue = canContinue;
            this.interruptable = interruptable;
            this.everyTick = everyTick;
            this.start = start;
            this.tick = tick;
            this.stop = stop;
            setFlags(flags);
        }
        @Override public boolean canUse() { return canUse.getAsBoolean(); }
        @Override public boolean canContinueToUse() { return canContinue == null ? canUse() : canContinue.getAsBoolean(); }
        @Override public boolean isInterruptable() { return interruptable.getAsBoolean(); }
        @Override public boolean requiresUpdateEveryTick() { return everyTick.getAsBoolean(); }
        @Override public void start() { start.run(); }
        @Override public void tick() { tick.run(); }
        @Override public void stop() { stop.run(); }
    }
}
