package com.vomiter.kubejseasynpc;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(KubeJSEasyNPC.MOD_ID)
public class KubeJSEasyNPC {
    public static final String MOD_ID = "kubejs_easynpc";

    public KubeJSEasyNPC() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EasyNPCBridge::setup);
    }
}
