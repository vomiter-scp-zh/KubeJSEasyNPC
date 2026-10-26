package com.vomiter.kubejseasynpc.handler.action;

import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.api.action.EasyNPCActionHandler;
import de.markusbordihn.easynpc.data.action.SoundActionData;
import net.minecraft.sounds.SoundSource;

public final class SoundActionHandlerJS extends NPCComponent {
    public SoundActionHandlerJS(NPCHandle npc) { super(npc); }
    public SoundActionData createData(String soundId) { return new SoundActionData(soundId); }
    public boolean play(String soundId) { return EasyNPCActionHandler.playSound(entity(), soundId); }
    public boolean playWithSettings(String soundId, SoundSource source, float volume, float pitch) {
        return EasyNPCActionHandler.playSound(entity(), soundId, source, volume, pitch);
    }
    public boolean playData(SoundActionData data) { return EasyNPCActionHandler.playSound(entity(), data); }
}
