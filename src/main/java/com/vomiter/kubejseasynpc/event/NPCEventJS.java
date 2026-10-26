package com.vomiter.kubejseasynpc.event;

import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.data.action.ActionContext;
import de.markusbordihn.easynpc.data.action.ActionEventType;
import de.markusbordihn.easynpc.entity.easynpc.EasyNPC;
import dev.latvian.mods.kubejs.event.EventJS;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

/** Notification events. Cancellation is deliberately not supported. */
public class NPCEventJS<D> extends EventJS {
    private final EasyNPC<?> source;
    private final ActionContext context;
    private final D details;

    public NPCEventJS(EasyNPC<?> source, ActionContext context, D details) {
        this.source = source;
        this.context = context;
        this.details = details;
    }

    public NPCHandle getNpc() { return new NPCHandle(getServer(), source.getEntityUUID()); }
    // Useful during removal, when resolving the handle may no longer find the entity.
    public Entity getEntity() { return source.getEntity(); }
    public ServerLevel getLevel() { return (ServerLevel) source.getEntity().level(); }
    public MinecraftServer getServer() { return getLevel().getServer(); }
    @Nullable
    public ServerPlayer getPlayer() { return context.initiator(); }
    public List<ServerPlayer> getAudience() { return context.audienceOrInitiator(); }
    public ActionEventType getEventType() { return context.eventType(); }
    public D getDetails() { return details; }
}
