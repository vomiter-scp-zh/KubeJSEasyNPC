package com.vomiter.kubejseasynpc;

import com.vomiter.kubejseasynpc.util.ScriptValidation;
import com.vomiter.kubejseasynpc.event.EasyNPCEvents;
import com.vomiter.kubejseasynpc.event.NPCEventJS;
import com.vomiter.kubejseasynpc.event.NPCStateEventJS;
import de.markusbordihn.easynpc.api.action.ActionRegistry;
import de.markusbordihn.easynpc.api.action.CustomActionExecutor;
import de.markusbordihn.easynpc.api.event.ActionEventListener;
import de.markusbordihn.easynpc.api.event.EasyNPCEventRegistry;
import de.markusbordihn.easynpc.api.event.EntityLifecycleListener;
import de.markusbordihn.easynpc.api.event.StateEventListener;
import de.markusbordihn.easynpc.data.action.ActionContext;
import de.markusbordihn.easynpc.data.action.ActionDataEntry;
import de.markusbordihn.easynpc.data.npc.NPCRemovalReason;
import de.markusbordihn.easynpc.data.state.StateEntry;
import de.markusbordihn.easynpc.entity.easynpc.EasyNPC;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.script.ScriptType;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public final class EasyNPCBridge {
    private static boolean initialized;
    private EasyNPCBridge() {}

    public static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(EasyNPCBridge::register);
    }

    // Once per process. No JS closures are retained by Easy NPC's static registries.
    private static synchronized void register() {
        if (initialized) return;
        initialized = true;
        EasyNPCEventRegistry.registerEntityLifecycleListener(new EntityLifecycleListener() {
            @Override
            public void onNPCSpawned(EasyNPC<?> npc) {
                post(EasyNPCEvents.SPAWNED, npc, ActionContext.EMPTY, Map.of());
            }
            @Override
            public void onNPCRemoved(EasyNPC<?> npc, NPCRemovalReason reason) {
                post(EasyNPCEvents.REMOVED, npc, ActionContext.EMPTY,
                    Map.of("reason", reason));
            }
        });

        EasyNPCEventRegistry.registerDialogEventListener((npc, player, dialog) ->
            post(EasyNPCEvents.DIALOG_OPENED, npc, ActionContext.of(player),
                Map.of("dialogId", dialog.getId(), "label", dialog.getLabel())));

        EasyNPCEventRegistry.registerActionEventListener(new ActionEventListener() {
            @Override
            @SuppressWarnings("deprecation")
            public void onActionExecuted(EasyNPC<?> npc, ServerPlayer player, ActionDataEntry action) {
                onActionExecuted(npc, action, ActionContext.of(player));
            }
            @Override
            public void onActionExecuted(EasyNPC<?> npc, ActionDataEntry action, ActionContext context) {
                post(EasyNPCEvents.ACTION_EXECUTED, npc, context,
                    Map.of("actionType", action.actionDataType(), "command", action.command()));
            }
        });

        EasyNPCEventRegistry.registerStateEventListener(new StateEventListener() {
            @Override
            @SuppressWarnings("deprecation")
            public void onStateChanged(EasyNPC<?> npc, ResourceLocation id, StateEntry oldValue, StateEntry value) {
                onStateChanged(npc, id, oldValue, value, ActionContext.EMPTY);
            }
            @Override
            public void onStateChanged(EasyNPC<?> npc, ResourceLocation id, StateEntry oldValue,
                                       StateEntry value, ActionContext context) {
                post(EasyNPCEvents.STATE_CHANGED,
                    new NPCStateEventJS(npc, context, id, oldValue, value));
            }
        });

        ActionRegistry.register(new ResourceLocation(KubeJSEasyNPC.MOD_ID, "script"), new CustomActionExecutor() {
            @Override
            @SuppressWarnings("deprecation")
            public void execute(ActionDataEntry action, EasyNPC<?> npc, ServerPlayer player, List<String> args) {
                execute(action, npc, args, ActionContext.of(player));
            }
            @Override
            public void execute(ActionDataEntry action, EasyNPC<?> npc, List<String> args, ActionContext context) {
                if (args.isEmpty()) {
                    throw new IllegalArgumentException("kubejs_easynpc:script requires an action key");
                }
                post(EasyNPCEvents.SCRIPT_ACTION, npc, context,
                    Map.of("key", args.get(0), "arguments", List.copyOf(args.subList(1, args.size()))));
            }
        });
    }

    private static void post(EventHandler handler, EasyNPC<?> npc, ActionContext context,
                             Map<String, Object> details) {
        post(handler, new NPCEventJS<>(npc, context, details));
    }

    private static void post(EventHandler handler, NPCEventJS<?> event) {
        if (!(event.getEntity().level() instanceof ServerLevel level)) return;
        // Execute inline: these events contain live objects and must preserve ordering.
        ScriptValidation.serverThread(level.getServer());
        if (handler.hasListeners()) {
            handler.post(ScriptType.SERVER, event);
        }
    }
}
