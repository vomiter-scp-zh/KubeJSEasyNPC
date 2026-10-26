package com.vomiter.kubejseasynpc.handler;

import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import com.vomiter.kubejseasynpc.preset.PresetFileResult;
import com.vomiter.kubejseasynpc.preset.WorldPresetFiles;
import de.markusbordihn.easynpc.api.handler.EasyNPCPresetHandler;
import de.markusbordihn.easynpc.handler.PresetHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public final class EasyNPCPresetHandlerJS extends NPCComponent {
    public EasyNPCPresetHandlerJS(NPCHandle npc) { super(npc); }
    public ItemStack createItem() { return EasyNPCPresetHandler.createPresetItem(entity()); }
    /** Upstream sanitized/normalized/compacted export data, detached from the NPC. */
    public CompoundTag createTag() {
        CompoundTag tag = PresetHandler.prepareExportData(entity());
        if (tag == null || tag.isEmpty()) throw new IllegalStateException("No exportable preset data");
        return tag.copy();
    }
    public PresetFileResult exportWorld(ResourceLocation id) {
        return WorldPresetFiles.save(npc.getServer(), id, createTag(), false);
    }
    public PresetFileResult exportWorldReplacing(ResourceLocation id) {
        return WorldPresetFiles.save(npc.getServer(), id, createTag(), true);
    }
}
