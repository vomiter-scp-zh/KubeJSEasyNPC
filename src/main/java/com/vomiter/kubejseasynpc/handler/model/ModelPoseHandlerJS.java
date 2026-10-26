package com.vomiter.kubejseasynpc.handler.model;

import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.api.pose.ModelPoseAPI;
import de.markusbordihn.easynpc.data.model.ModelPose;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;

public final class ModelPoseHandlerJS extends NPCComponent {
    public ModelPoseHandlerJS(NPCHandle npc) { super(npc); }
    public boolean setId(ResourceLocation id) { return ModelPoseAPI.setPose(entity(), id); }
    public boolean setName(String name) { return ModelPoseAPI.setPose(entity(), name); }
    public boolean setVanilla(Pose pose) { return ModelPoseAPI.setVanillaPose(entity(), pose); }
    public void reset() { ModelPoseAPI.resetPose(entity()); }
    public String getCurrentName() { return ModelPoseAPI.getCurrentPoseName(entity()); }
    public ModelPose getCurrentMode() { return ModelPoseAPI.getCurrentPoseMode(entity()); }
    public Set<ResourceLocation> getAvailable() { return ModelPoseAPI.getAvailablePoses(entity()); }
}
