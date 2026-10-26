package com.vomiter.kubejseasynpc.event;

import de.markusbordihn.easynpc.data.action.ActionContext;
import de.markusbordihn.easynpc.data.state.StateEntry;
import de.markusbordihn.easynpc.entity.easynpc.EasyNPC;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

/** Typed state payload: nullable bean getters avoid Rhino 2001's null Map value bug. */
public final class NPCStateEventJS extends NPCEventJS<NPCStateEventJS.Details> {
    public NPCStateEventJS(EasyNPC<?> source, ActionContext context, ResourceLocation id,
                           @Nullable StateEntry previous, @Nullable StateEntry current) {
        super(source, context, new Details(id, previous, current));
    }

    @Override
    public Details getDetails() { return super.getDetails(); }

    public static final class Details {
        private final ResourceLocation stateId;
        @Nullable private final StateEntry previous;
        @Nullable private final StateEntry current;

        private Details(ResourceLocation stateId, @Nullable StateEntry previous,
                        @Nullable StateEntry current) {
            this.stateId = stateId;
            this.previous = previous;
            this.current = current;
        }
        public ResourceLocation getStateId() { return stateId; }
        @Nullable
        public StateEntry getPrevious() { return previous; }
        @Nullable
        public StateEntry getCurrent() { return current; }
    }
}
