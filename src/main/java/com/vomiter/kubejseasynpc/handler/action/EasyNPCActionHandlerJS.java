package com.vomiter.kubejseasynpc.handler.action;

import com.vomiter.kubejseasynpc.NPCHandle;

/** Groups the NPC-bound operations of the corresponding upstream handler. */
public final class EasyNPCActionHandlerJS {
    private final ActionHandlerJS action;
    private final MovementActionHandlerJS movement;
    private final StateActionHandlerJS state;
    private final MessageActionHandlerJS message;
    private final DialogActionHandlerJS dialog;
    private final SoundActionHandlerJS sound;
    private final DisplayActionHandlerJS display;

    public EasyNPCActionHandlerJS(NPCHandle npc) {
        action = new ActionHandlerJS(npc);
        movement = new MovementActionHandlerJS(npc);
        state = new StateActionHandlerJS(npc);
        message = new MessageActionHandlerJS(npc);
        dialog = new DialogActionHandlerJS(npc);
        sound = new SoundActionHandlerJS(npc);
        display = new DisplayActionHandlerJS(npc);
    }

    public ActionHandlerJS getAction() { return action; }
    public MovementActionHandlerJS getMovement() { return movement; }
    public StateActionHandlerJS getState() { return state; }
    public MessageActionHandlerJS getMessage() { return message; }
    public DialogActionHandlerJS getDialog() { return dialog; }
    public SoundActionHandlerJS getSound() { return sound; }
    public DisplayActionHandlerJS getDisplay() { return display; }
}
