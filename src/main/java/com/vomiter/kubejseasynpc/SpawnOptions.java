package com.vomiter.kubejseasynpc;

import de.markusbordihn.easynpc.data.preset.PresetType;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

/** Typed, reusable spawn settings. Omitted UUIDs are generated for each spawn. */
public final class SpawnOptions {
    private PresetType type = PresetType.DATA;
    private UUID uuid;
    private ServerPlayer owner;
    private ResourceLocation identifier;

    public SpawnOptions type(PresetType type) {
        this.type = Objects.requireNonNull(type, "preset type");
        return this;
    }
    public SpawnOptions uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }
    public SpawnOptions owner(ServerPlayer owner) {
        this.owner = owner;
        return this;
    }
    public SpawnOptions identifier(ResourceLocation identifier) {
        this.identifier = identifier;
        return this;
    }

    public PresetType getType() { return type; }
    @Nullable
    public UUID getUuid() { return uuid; }
    @Nullable
    public ServerPlayer getOwner() { return owner; }
    @Nullable
    public ResourceLocation getIdentifier() { return identifier; }
}
