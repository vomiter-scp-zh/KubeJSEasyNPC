package com.vomiter.kubejseasynpc.handler.action;

import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.api.action.EasyNPCActionHandler;
import net.minecraft.server.level.ServerPlayer;

public final class DialogActionHandlerJS extends NPCComponent {
    public DialogActionHandlerJS(NPCHandle npc) { super(npc); }
    public boolean openDefault(ServerPlayer player) { checkPlayer(player); return EasyNPCActionHandler.openDialog(entity(), player); }
    public boolean open(ServerPlayer player, String label) { checkPlayer(player); return EasyNPCActionHandler.openDialog(entity(), player, label); }
    public boolean openIfConditionsMet(ServerPlayer player, String label) { checkPlayer(player); return EasyNPCActionHandler.openDialogIfConditionsMet(entity(), player, label); }
    public boolean openTrading(ServerPlayer player) { checkPlayer(player); return EasyNPCActionHandler.openTradingScreen(entity(), player); }
}
