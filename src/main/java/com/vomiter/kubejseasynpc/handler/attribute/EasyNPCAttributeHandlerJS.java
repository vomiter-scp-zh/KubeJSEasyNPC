package com.vomiter.kubejseasynpc.handler.attribute;

import com.vomiter.kubejseasynpc.NPCHandle;

/** Groups the NPC-bound operations of the corresponding upstream handler. */
public final class EasyNPCAttributeHandlerJS {
    private final CombatAttributeHandlerJS combat;
    private final MovementAttributeHandlerJS movement;
    private final InteractionAttributeHandlerJS interaction;
    private final EnvironmentalAttributeHandlerJS environmental;
    private final InventoryAttributeHandlerJS inventory;
    private final DisplayAttributeHandlerJS display;
    private final MinecraftAttributeHandlerJS minecraftAttributes;

    public EasyNPCAttributeHandlerJS(NPCHandle npc) {
        combat = new CombatAttributeHandlerJS(npc);
        movement = new MovementAttributeHandlerJS(npc);
        interaction = new InteractionAttributeHandlerJS(npc);
        environmental = new EnvironmentalAttributeHandlerJS(npc);
        inventory = new InventoryAttributeHandlerJS(npc);
        display = new DisplayAttributeHandlerJS(npc);
        minecraftAttributes = new MinecraftAttributeHandlerJS(npc);
    }

    public CombatAttributeHandlerJS getCombat() { return combat; }
    public MovementAttributeHandlerJS getMovement() { return movement; }
    public InteractionAttributeHandlerJS getInteraction() { return interaction; }
    public EnvironmentalAttributeHandlerJS getEnvironmental() { return environmental; }
    public InventoryAttributeHandlerJS getInventory() { return inventory; }
    public DisplayAttributeHandlerJS getDisplay() { return display; }
    public MinecraftAttributeHandlerJS getMinecraftAttributes() { return minecraftAttributes; }
}
