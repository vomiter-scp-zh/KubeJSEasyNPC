package com.vomiter.kubejseasynpc.preset;

import com.vomiter.kubejseasynpc.util.ScriptValidation;
import de.markusbordihn.easynpc.io.PresetFileHandler;
import de.markusbordihn.easynpc.io.WorldPresetDataFiles;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

/** Uses the same world preset directory and IDs as Easy NPC's file index. */
public final class WorldPresetFiles {
    private WorldPresetFiles() {}

    private static Path resolve(ResourceLocation id) throws IOException {
        Objects.requireNonNull(id, "preset ID");
        String prefix = "preset/";
        String value = id.getPath();
        if (!"easy_npc".equals(id.getNamespace()) || !value.startsWith(prefix)
                || !(value.endsWith(".nbt") || value.endsWith(".snbt"))) {
            throw new IllegalArgumentException("Expected easy_npc:preset/<path>.nbt or .snbt");
        }
        String relative = value.substring(prefix.length());
        for (String part : relative.split("/", -1)) {
            if (part.isEmpty() || part.equals(".") || part.equals("..")) {
                throw new IllegalArgumentException("Preset path contains an empty or relative segment");
            }
        }
        Path root = WorldPresetDataFiles.getPresetDataFolder().toAbsolutePath().normalize();
        Path target = root.resolve(relative).normalize();
        if (!target.startsWith(root)) throw new IllegalArgumentException("Preset path escapes world preset directory");
        Path cursor = root;
        if (Files.isSymbolicLink(cursor)) throw new IOException("Preset directory is a symbolic link");
        for (Path part : root.relativize(target)) {
            cursor = cursor.resolve(part);
            if (Files.isSymbolicLink(cursor)) throw new IOException("Preset path contains a symbolic link: " + cursor);
        }
        return target;
    }

    public static boolean exists(MinecraftServer server, ResourceLocation id) {
        ScriptValidation.serverThread(server);
        try {
            return Files.isRegularFile(resolve(id), LinkOption.NOFOLLOW_LINKS);
        } catch (IOException exception) {
            throw new IllegalStateException("Cannot inspect world preset " + id, exception);
        }
    }

    public static PresetFileResult save(MinecraftServer server, ResourceLocation id,
                                        CompoundTag tag, boolean replace) {
        ScriptValidation.serverThread(server);
        Path target = null;
        Path temporary = null;
        try {
            target = resolve(id);
            if (tag == null || tag.isEmpty()) return result(false, id, target, "invalid_data", "No exportable preset data");
            if (Files.exists(target, LinkOption.NOFOLLOW_LINKS)) {
                if (!Files.isRegularFile(target, LinkOption.NOFOLLOW_LINKS)) {
                    return result(false, id, target, "invalid_target", "Target is not a regular file");
                }
                if (!replace) return result(false, id, target, "already_exists", "Preset already exists");
            }
            Files.createDirectories(target.getParent());
            String extension = id.getPath().endsWith(".snbt") ? ".snbt" : ".nbt";
            temporary = Files.createTempFile(target.getParent(), ".kjs-preset-", extension);
            if (!PresetFileHandler.save(temporary.toFile(), tag)) {
                return result(false, id, target, "write_failed", "Easy NPC could not write preset data; see server log");
            }
            if (replace) Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING);
            else Files.move(temporary, target);
            temporary = null;
            WorldPresetDataFiles.refreshPresetResourceLocations();
            return result(true, id, target, "saved", "Preset saved");
        } catch (FileAlreadyExistsException exception) {
            return result(false, id, target, "already_exists", exception.getMessage());
        } catch (IOException exception) {
            return result(false, id, target, "io_error", exception.getMessage());
        } finally {
            if (temporary != null) {
                try {
                    Files.deleteIfExists(temporary);
                    Files.deleteIfExists(temporary.resolveSibling(temporary.getFileName() + ".tmp"));
                } catch (IOException exception) {
                    // Do not conceal the original operation result with a cleanup error.
                    System.getLogger(WorldPresetFiles.class.getName()).log(System.Logger.Level.WARNING,
                        "Could not remove temporary preset file " + temporary, exception);
                }
            }
        }
    }

    public static PresetFileResult delete(MinecraftServer server, ResourceLocation id) {
        ScriptValidation.serverThread(server);
        Path target = null;
        try {
            target = resolve(id);
            if (!Files.exists(target, LinkOption.NOFOLLOW_LINKS)) return result(false, id, target, "not_found", "Preset does not exist");
            if (!Files.isRegularFile(target, LinkOption.NOFOLLOW_LINKS)) return result(false, id, target, "invalid_target", "Target is not a regular file");
            Files.delete(target);
            WorldPresetDataFiles.refreshPresetResourceLocations();
            return result(true, id, target, "deleted", "Preset deleted");
        } catch (IOException exception) {
            return result(false, id, target, "io_error", exception.getMessage());
        }
    }

    private static PresetFileResult result(boolean success, ResourceLocation id, Path path, String status, String message) {
        return new PresetFileResult(success, id, path == null ? "" : path.toString(), status, message == null ? "" : message);
    }
}
