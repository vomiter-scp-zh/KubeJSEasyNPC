package com.vomiter.kubejseasynpc.preset;

import net.minecraft.resources.ResourceLocation;

/** File operation result. Invalid arguments and wrong-thread calls throw instead. */
public record PresetFileResult(boolean success, ResourceLocation id, String path,
                               String status, String message) {}
