package com.vomiter.kubejseasynpc.handler.model;

import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.api.animation.ModelAnimationAPI;
import de.markusbordihn.easynpc.api.animation.ModelAnimationInfo;
import de.markusbordihn.easynpc.data.action.ModelAnimationActionData;
import de.markusbordihn.easynpc.data.model.ModelAnimationPlayback;
import de.markusbordihn.easynpc.data.model.ModelAnimationPlaybackMode;
import de.markusbordihn.easynpc.data.model.ModelAnimationTransition;
import java.util.List;

public final class ModelAnimationHandlerJS extends NPCComponent {
    public ModelAnimationHandlerJS(NPCHandle npc) { super(npc); }
    public ModelAnimationActionData createActionData(String name) {
        return new ModelAnimationActionData(name);
    }
    public ModelAnimationPlayback createPlayback(ModelAnimationPlaybackMode mode) {
        return ModelAnimationPlayback.of(mode);
    }
    public boolean isSupported() { return ModelAnimationAPI.supportsAnimations(entity()); }
    public List<ModelAnimationInfo> getAll() { return ModelAnimationAPI.listAnimations(entity()); }
    public List<String> getVariants(String baseName) { return ModelAnimationAPI.listAnimationVariants(entity(), baseName); }
    public boolean play(String name) { return ModelAnimationAPI.playAnimation(entity(), name); }
    public boolean playWithMode(String name, ModelAnimationPlaybackMode mode, ModelAnimationTransition transition) {
        return ModelAnimationAPI.playAnimation(entity(), name, mode, transition);
    }
    public boolean playWithPlayback(String name, ModelAnimationPlayback playback, ModelAnimationTransition transition) {
        return ModelAnimationAPI.playAnimation(entity(), name, playback, transition);
    }
    public boolean stop() { return ModelAnimationAPI.stopAnimation(entity()); }
    public boolean stopWithTransition(ModelAnimationTransition transition) { return ModelAnimationAPI.stopAnimation(entity(), transition); }
    public boolean restart() { return ModelAnimationAPI.restartAnimation(entity()); }
}
