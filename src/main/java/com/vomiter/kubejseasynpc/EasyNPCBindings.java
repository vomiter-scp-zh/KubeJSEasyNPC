package com.vomiter.kubejseasynpc;

import com.vomiter.kubejseasynpc.util.ScriptValidation;
import com.vomiter.kubejseasynpc.preset.PresetFileResult;
import com.vomiter.kubejseasynpc.preset.WorldPresetFiles;
import de.markusbordihn.easynpc.handler.PresetHandler;
import de.markusbordihn.easynpc.api.handler.EasyNPCEntityHandler;
import de.markusbordihn.easynpc.api.preset.PresetCatalog;
import de.markusbordihn.easynpc.data.action.ActionContext;
import de.markusbordihn.easynpc.data.action.ActionDataEntry;
import de.markusbordihn.easynpc.data.action.ActionDataSet;
import de.markusbordihn.easynpc.data.action.ActionDataType;
import de.markusbordihn.easynpc.data.condition.ConditionDataEntry;
import de.markusbordihn.easynpc.data.condition.ConditionDataSet;
import de.markusbordihn.easynpc.data.condition.ConditionType;
import de.markusbordihn.easynpc.data.preset.PresetType;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public final class EasyNPCBindings {
    private EasyNPCBindings() {}

    private static final String SCRIPT_ACTION_PREFIX = KubeJSEasyNPC.MOD_ID + ":script ";

    public static ActionDataEntry action(ActionDataType type) {
        return new ActionDataEntry(type);
    }
    public static ActionDataEntry scriptAction(String key) {
        return new ActionDataEntry(ActionDataType.CUSTOM, SCRIPT_ACTION_PREFIX + scriptToken(key, "script key"));
    }
    public static ActionDataEntry scriptActionWithArgs(String key, List<String> arguments) {
        scriptToken(key, "script key");
        for (int i = 0; i < arguments.size(); i++) scriptToken(arguments.get(i), "script argument " + i);
        String command = SCRIPT_ACTION_PREFIX + key;
        if (!arguments.isEmpty()) command += " " + String.join(" ", arguments);
        return new ActionDataEntry(ActionDataType.CUSTOM, command);
    }
    private static String scriptToken(String value, String label) {
        if (value.isEmpty() || value.codePoints().anyMatch(c -> Character.isWhitespace(c) || Character.isSpaceChar(c))) {
            throw new IllegalArgumentException(label + " must be a non-empty token without whitespace");
        }
        return value;
    }
    public static ActionDataSet actionSet(List<ActionDataEntry> entries) {
        ActionDataSet actions = new ActionDataSet();
        for (ActionDataEntry entry : entries) actions.add(entry);
        return actions;
    }
    public static ConditionDataEntry condition(ConditionType type) {
        return new ConditionDataEntry(type);
    }
    public static ConditionDataEntry customCondition(ResourceLocation id) {
        return new ConditionDataEntry(id);
    }
    public static ConditionDataSet conditionSet(List<ConditionDataEntry> entries) {
        ConditionDataSet conditions = new ConditionDataSet();
        for (ConditionDataEntry entry : entries) conditions.add(entry);
        return conditions;
    }
    public static ActionContext actionContext() { return ActionContext.EMPTY; }
    public static ActionContext actionContextFor(ServerPlayer player) {
        return ActionContext.of(player);
    }

    public static SpawnOptions spawnOptions() { return new SpawnOptions(); }

    public static NPCHandle spawn(ServerLevel level, ResourceLocation preset, double x, double y, double z) {
        return spawn(level, preset, x, y, z, spawnOptions());
    }

    public static NPCHandle spawn(ServerLevel level, ResourceLocation preset, double x, double y, double z,
                                  SpawnOptions options) {
        checkThread(level.getServer());
        ScriptValidation.finite(x, "position.x");
        ScriptValidation.finite(y, "position.y");
        ScriptValidation.finite(z, "position.z");
        PresetType type = serverPresetType(options.getType());
        ResourceLocation presetId = preset;
        UUID uuid = options.getUuid() == null ? UUID.randomUUID() : options.getUuid();
        ServerPlayer owner = options.getOwner();
        if (owner != null) {
            ScriptValidation.playerOnServer(level.getServer(), owner);
        }
        ResourceLocation identifier = options.getIdentifier();
        if (EasyNPCEntityHandler.getAll().stream().anyMatch(e -> e.entityUUID().equals(uuid))) {
            throw new IllegalArgumentException("NPC UUID already exists; use restore/revive: " + uuid);
        }
        EasyNPCEntityHandler.spawnFromPreset(type, presetId, level, new Vec3(x, y, z), uuid, owner, identifier)
            .orElseThrow(() -> new IllegalStateException("Could not create NPC from " + type + " preset " + presetId
                + " in " + level.dimension().location() + "; check the Easy NPC server log"));
        return new NPCHandle(level.getServer(), uuid);
    }

    /** Creates a handle without requiring a saved record or a loaded entity. */
    public static NPCHandle get(MinecraftServer server, UUID uuid) {
        checkThread(server);
        return new NPCHandle(server, uuid);
    }

    @Nullable
    public static NPCHandle findLoaded(MinecraftServer server, UUID uuid) {
        NPCHandle handle = get(server, uuid);
        return handle.isLoaded() ? handle : null;
    }

    public static List<UUID> findByIdentifier(MinecraftServer server, ResourceLocation identifier) {
        checkThread(server);
        return EasyNPCEntityHandler.getByCustomIdentifier(identifier).stream()
            .map(entry -> entry.entityUUID()).toList();
    }

    public static List<ResourceLocation> listPresets(MinecraftServer server, PresetType type) {
        checkThread(server);
        return PresetCatalog.listPresets(server, serverPresetType(type)).stream()
            .sorted(Comparator.comparing(ResourceLocation::toString)).toList();
    }

    /** Unlike listPresets, this also checks presets hidden from player listings. */
    public static boolean presetExists(MinecraftServer server, PresetType type, ResourceLocation id) {
        checkThread(server);
        PresetType source = serverPresetType(type);
        if (source == PresetType.WORLD) return WorldPresetFiles.exists(server, id);
        var data = PresetHandler.loadPreset(id, source, server);
        return data != null && data.hasValidData();
    }

    public static PresetFileResult deleteWorldPreset(MinecraftServer server, ResourceLocation id) {
        checkThread(server);
        return WorldPresetFiles.delete(server, id);
    }

    private static PresetType serverPresetType(PresetType type) {
        Objects.requireNonNull(type, "preset type");
        if (type == PresetType.LOCAL) throw new IllegalArgumentException("Client-local presets cannot be spawned on a server");
        return type;
    }

    static void checkThread(MinecraftServer server) {
        ScriptValidation.serverThread(server);
    }
}
