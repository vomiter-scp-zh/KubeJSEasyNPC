package com.vomiter.kubejseasynpc.handler;

import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.api.handler.EasyNPCObjectiveHandler;
import de.markusbordihn.easynpc.data.objective.ObjectiveDataEntry;
import de.markusbordihn.easynpc.data.objective.ObjectiveType;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public final class EasyNPCObjectiveHandlerJS extends NPCComponent {
    public EasyNPCObjectiveHandlerJS(NPCHandle npc) { super(npc); }
    public boolean add(ObjectiveType type) {
        return EasyNPCObjectiveHandler.addObjective(entity(), type);
    }
    public boolean addEntry(ObjectiveDataEntry entry) {
        return EasyNPCObjectiveHandler.addObjective(entity(), entry);
    }
    public ObjectiveDataEntry createEntry(ObjectiveType type) {
        return new ObjectiveDataEntry(type);
    }
    public ObjectiveDataEntry createCustomEntry(ResourceLocation id) {
        return new ObjectiveDataEntry(id);
    }
    @Nullable
    public ObjectiveDataEntry get(String id) { return EasyNPCObjectiveHandler.getObjective(entity(), id); }
    public boolean has(String id) { return get(id) != null; }
    public boolean hasType(ObjectiveType type) { return EasyNPCObjectiveHandler.hasObjective(entity(), type); }
    public boolean remove(String id) { return EasyNPCObjectiveHandler.removeObjective(entity(), id); }
    public boolean setPriority(String id, int priority) { return EasyNPCObjectiveHandler.setPriority(entity(), id, priority); }
    public Set<ObjectiveDataEntry> getAll() { return EasyNPCObjectiveHandler.getObjectives(entity()); }
}
