package com.vomiter.kubejseasynpc;

import com.vomiter.kubejseasynpc.util.ScriptValidation;
import com.vomiter.kubejseasynpc.handler.EasyNPCMotionHandlerJS;
import com.vomiter.kubejseasynpc.handler.EasyNPCObjectiveHandlerJS;
import com.vomiter.kubejseasynpc.handler.EasyNPCPauseHandlerJS;
import com.vomiter.kubejseasynpc.handler.EasyNPCPresetHandlerJS;
import com.vomiter.kubejseasynpc.handler.EasyNPCShoulderHandlerJS;
import com.vomiter.kubejseasynpc.handler.EquipmentHandlerJS;
import com.vomiter.kubejseasynpc.handler.DialogHandlerJS;
import com.vomiter.kubejseasynpc.handler.InventoryHandlerJS;
import com.vomiter.kubejseasynpc.handler.TradingHandlerJS;
import com.vomiter.kubejseasynpc.handler.action.EasyNPCActionHandlerJS;
import com.vomiter.kubejseasynpc.handler.attribute.EasyNPCAttributeHandlerJS;
import com.vomiter.kubejseasynpc.handler.model.ModelAnimationHandlerJS;
import com.vomiter.kubejseasynpc.handler.model.ModelPoseHandlerJS;
import de.markusbordihn.easynpc.api.handler.EasyNPCEntityHandler;
import de.markusbordihn.easynpc.data.action.ActionContext;
import de.markusbordihn.easynpc.data.npc.NPCRemovalReason;
import de.markusbordihn.easynpc.entity.easynpc.EasyNPC;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** Stable UUID reference. Components re-resolve the entity on every operation. */
public final class NPCHandle {
    private final MinecraftServer server;
    private final UUID uuid;
    private final EasyNPCAttributeHandlerJS attributes = new EasyNPCAttributeHandlerJS(this);
    private final EasyNPCActionHandlerJS actions = new EasyNPCActionHandlerJS(this);
    private final EasyNPCObjectiveHandlerJS objectives = new EasyNPCObjectiveHandlerJS(this);
    private final EasyNPCPauseHandlerJS pause = new EasyNPCPauseHandlerJS(this);
    private final EasyNPCMotionHandlerJS motion = new EasyNPCMotionHandlerJS(this);
    private final EasyNPCPresetHandlerJS preset = new EasyNPCPresetHandlerJS(this);
    private final EasyNPCShoulderHandlerJS shoulder = new EasyNPCShoulderHandlerJS(this);
    private final ModelAnimationHandlerJS animation = new ModelAnimationHandlerJS(this);
    private final ModelPoseHandlerJS pose = new ModelPoseHandlerJS(this);
    private final TradingHandlerJS trading = new TradingHandlerJS(this);
    private final InventoryHandlerJS inventory = new InventoryHandlerJS(this);
    private final EquipmentHandlerJS equipment = new EquipmentHandlerJS(this);
    private final DialogHandlerJS dialog = new DialogHandlerJS(this);
    public NPCHandle(MinecraftServer server, UUID uuid) {
        this.server = Objects.requireNonNull(server, "server");
        this.uuid = Objects.requireNonNull(uuid, "NPC UUID");
    }
    public UUID getUuid() { return uuid; }
    public MinecraftServer getServer() { return server; }
    private EasyNPC<?> resolve() {
        EasyNPCBindings.checkThread(server);
        for (ServerLevel level : server.getAllLevels()) {
            EasyNPC<?> value = EasyNPCEntityHandler.find(uuid, level).orElse(null);
            if (value != null) return value;
        }
        return null;
    }
    EasyNPC<?> requireLoaded() {
        EasyNPC<?> value = resolve();
        if (value == null) throw new IllegalStateException("NPC " + uuid + " is not loaded; this operation does not load chunks");
        return value;
    }
    void checkLevel(ServerLevel level) {
        EasyNPCBindings.checkThread(server);
        if (level == null || level.getServer() != server) throw new IllegalArgumentException("Level belongs to a different server");
    }
    void checkPlayer(ServerPlayer player) {
        EasyNPCBindings.checkThread(server);
        ScriptValidation.playerOnServer(server, player);
    }
    void checkContext(ActionContext context) {
        EasyNPCBindings.checkThread(server);
        if (context.initiator() != null) checkPlayer(context.initiator());
        context.audienceOrInitiator().forEach(this::checkPlayer);
    }
    public boolean isLoaded() { return resolve() != null; }
    @Nullable
    public Entity getEntity() { EasyNPC<?> value = resolve(); return value == null ? null : value.getEntity(); }
    public boolean exists() {
        EasyNPCBindings.checkThread(server);
        return EasyNPCEntityHandler.getAll().stream().anyMatch(e -> e.entityUUID().equals(uuid));
    }
    public boolean restore(ServerLevel level) { return restore(level, false); }
    public boolean restore(ServerLevel level, boolean force) { checkLevel(level); return EasyNPCEntityHandler.spawn(uuid, level, force); }
    public boolean restoreAt(ServerLevel level, Vec3 position) { return restoreAt(level, position, false); }
    public boolean restoreAt(ServerLevel level, Vec3 position, boolean force) {
        checkLevel(level); return EasyNPCEntityHandler.spawn(uuid, level, NPCComponent.position(position), force);
    }
    public String restoreRejection(boolean force) { EasyNPCBindings.checkThread(server); return EasyNPCEntityHandler.getSpawnRejection(uuid, force).orElse(""); }
    public String reviveRejection() { EasyNPCBindings.checkThread(server); return EasyNPCEntityHandler.getReviveRejection(uuid).orElse(""); }
    public boolean revive(ServerLevel level) { checkLevel(level); return EasyNPCEntityHandler.revive(uuid, level); }
    public boolean reviveAt(ServerLevel level, Vec3 position) { checkLevel(level); return EasyNPCEntityHandler.revive(uuid, level, NPCComponent.position(position)); }
    public boolean respawn(ServerLevel level) { checkLevel(level); return EasyNPCEntityHandler.respawn(requireLoaded(), level); }
    public boolean despawn() { return EasyNPCEntityHandler.despawn(requireLoaded(), NPCRemovalReason.DESPAWNED); }
    public boolean despawnWithReason(NPCRemovalReason reason) { return EasyNPCEntityHandler.despawn(requireLoaded(), Objects.requireNonNull(reason)); }
    public boolean delete(ServerLevel level) { checkLevel(level); return EasyNPCEntityHandler.delete(uuid, level); }
    public EasyNPCAttributeHandlerJS getAttributes() { return attributes; }
    public EasyNPCActionHandlerJS getActions() { return actions; }
    public EasyNPCObjectiveHandlerJS getObjectives() { return objectives; }
    public EasyNPCPauseHandlerJS getPause() { return pause; }
    public EasyNPCMotionHandlerJS getMotion() { return motion; }
    public EasyNPCPresetHandlerJS getPreset() { return preset; }
    public EasyNPCShoulderHandlerJS getShoulder() { return shoulder; }
    public ModelAnimationHandlerJS getAnimation() { return animation; }
    public ModelPoseHandlerJS getPose() { return pose; }
    public DialogHandlerJS getDialog() { return dialog; }
    public TradingHandlerJS getTrading() { return trading; }
    public InventoryHandlerJS getInventory() { return inventory; }
    public EquipmentHandlerJS getEquipment() { return equipment; }
}
