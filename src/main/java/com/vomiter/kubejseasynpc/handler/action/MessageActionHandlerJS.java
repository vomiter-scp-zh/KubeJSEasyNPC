package com.vomiter.kubejseasynpc.handler.action;

import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.api.action.EasyNPCActionHandler;
import de.markusbordihn.easynpc.data.action.ActionContext;
import de.markusbordihn.easynpc.data.action.MessageActionData;
import java.util.Collection;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;

public final class MessageActionHandlerJS extends NPCComponent {
    public MessageActionHandlerJS(NPCHandle npc) { super(npc); }
    public MessageActionData createData() { return MessageActionData.DEFAULT; }
    public boolean say(String text) { return EasyNPCActionHandler.say(entity(), text); }
    public boolean sayLines(List<String> texts) { return EasyNPCActionHandler.say(entity(), List.copyOf(texts)); }
    public boolean sendTo(ServerPlayer player, String text) {
        checkPlayer(player); return EasyNPCActionHandler.sayTo(entity(), player, text);
    }
    public boolean sendLinesTo(ServerPlayer player, List<String> texts) {
        checkPlayer(player); return EasyNPCActionHandler.sayTo(entity(), player, List.copyOf(texts));
    }
    public boolean sendToPlayers(Collection<ServerPlayer> players, String text) {
        players.forEach(this::checkPlayer);
        return EasyNPCActionHandler.sayTo(entity(), List.copyOf(players), text);
    }
    public boolean sendLinesToPlayers(Collection<ServerPlayer> players, List<String> texts) {
        players.forEach(this::checkPlayer);
        return EasyNPCActionHandler.sayTo(entity(), List.copyOf(players), List.copyOf(texts));
    }
    public boolean showBubble(String text) { return EasyNPCActionHandler.showSpeechBubble(entity(), text); }
    public boolean showBubbleFor(String text, int ticks) { return EasyNPCActionHandler.showSpeechBubble(entity(), text, ticks); }
    public boolean showBubbleLines(List<String> texts) { return EasyNPCActionHandler.showSpeechBubble(entity(), List.copyOf(texts)); }
    public boolean showBubbleLinesFor(List<String> texts, int ticks) { return EasyNPCActionHandler.showSpeechBubble(entity(), List.copyOf(texts), ticks); }
    public boolean send(String text, MessageActionData data, ActionContext context) {
        checkContext(context);
        return EasyNPCActionHandler.sendMessage(entity(), text, data, context);
    }
    public boolean sendLines(List<String> texts, MessageActionData data, ActionContext context) {
        checkContext(context);
        return EasyNPCActionHandler.sendMessage(entity(), List.copyOf(texts), data, context);
    }
}
