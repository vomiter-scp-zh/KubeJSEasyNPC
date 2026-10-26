package com.vomiter.kubejseasynpc.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

/** Shared addon contracts only; upstream parameter policies stay with upstream. */
public final class ScriptValidation {
    private ScriptValidation() {}

    public static void serverThread(MinecraftServer server) {
        if (server == null || !server.isSameThread()) {
            throw new IllegalStateException("EasyNPC scripting operations require the server thread");
        }
    }

    public static void playerOnServer(MinecraftServer server, ServerPlayer player) {
        if (player == null || player.server != server) {
            throw new IllegalArgumentException("Expected a player on this server");
        }
    }

    public static double finite(double value, String name) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(name + " must be finite: " + value);
        }
        return value;
    }

    public static Vec3 position(Vec3 value) {
        finite(value.x, "position.x");
        finite(value.y, "position.y");
        finite(value.z, "position.z");
        return value;
    }

    public static void index(int index, int size, String name) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(name + " " + index + " outside [0, " + size + ")");
        }
    }
}
