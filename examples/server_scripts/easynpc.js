// Copy to kubejs/server_scripts/easynpc.js, then /reload.
// A persistent per-player UUID prevents creating another guide on each login.
PlayerEvents.loggedIn(event => {
    let player = event.player
    if (player.persistentData.contains('kjsenGuide')) return
    let npc = EasyNPC.spawn(player.serverLevel(),
        'kubejs_easynpc:easy_npc/preset/guide.npc.snbt',
        player.x + 2, player.y, player.z,
        EasyNPC.spawnOptions().type('data').owner(player).identifier('example:guide'))
    player.persistentData.putString('kjsenGuide', String(npc.uuid))
})

EasyNPCEvents.scriptAction(event => {
    let player = event.player
    if (player == null) return // Some actions have no initiating player.
    switch (String(event.details.key)) {
        case 'example:greet':
            event.npc.getActions().getMessage().sendTo(player, 'This message came from KubeJS.')
            let visits = event.npc.getActions().getState().get('example:visits')
            event.npc.getActions().getState().setNumber('example:visits', (visits == null ? 0 : visits.asNumber()) + 1)
            break
        case 'example:follow':
            event.npc.getObjectives().add('FOLLOW_OWNER')
            break
        case 'example:stop':
            event.npc.getObjectives().remove('FOLLOW_OWNER')
            break
    }
})

EasyNPCEvents.stateChanged(event => {
    console.info(`[EasyNPC] ${event.npc.uuid}: ${event.details.stateId} changed`)
})
