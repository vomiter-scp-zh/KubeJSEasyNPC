package com.vomiter.kubejseasynpc;

import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;

@Mod(KubeJSEasyNPC.MOD_ID)
public class KubeJSEasyNPC {
    public static final String MOD_ID = "kubejs_easynpc";

    public KubeJSEasyNPC(IEventBus modBus) {
        modBus.addListener(EasyNPCBridge::setup);
    }
}
