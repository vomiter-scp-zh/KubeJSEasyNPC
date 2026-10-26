package com.vomiter.kubejseasynpc;

import com.vomiter.kubejseasynpc.util.ScriptValidation;
import de.markusbordihn.easynpc.data.action.ActionContext;
import de.markusbordihn.easynpc.entity.easynpc.EasyNPC;
import java.util.Objects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public abstract class NPCComponent {
    protected final NPCHandle npc;
    protected NPCComponent(NPCHandle npc) { this.npc = Objects.requireNonNull(npc); }
    protected final EasyNPC<?> entity() { return npc.requireLoaded(); }
    protected final void checkPlayer(ServerPlayer player) { npc.checkPlayer(player); }
    protected final void checkContext(ActionContext context) { npc.checkContext(context); }
    protected final void checkThread() { EasyNPCBindings.checkThread(npc.getServer()); }
    protected static Vec3 position(Vec3 value) {
        return ScriptValidation.position(value);
    }
}
