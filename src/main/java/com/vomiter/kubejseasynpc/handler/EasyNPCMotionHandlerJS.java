package com.vomiter.kubejseasynpc.handler;

import com.vomiter.kubejseasynpc.util.ScriptValidation;
import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.api.handler.EasyNPCMotionHandler;
import net.minecraft.world.phys.Vec3;

public final class EasyNPCMotionHandlerJS extends NPCComponent {
    public EasyNPCMotionHandlerJS(NPCHandle npc) { super(npc); }
    public boolean snapTo(Vec3 target) { return EasyNPCMotionHandler.snapTo(entity(), position(target)); }
    public boolean faceTowards(Vec3 target) { return EasyNPCMotionHandler.faceTowards(entity(), position(target)); }
    public boolean stepTowards(Vec3 target, double stepLength, double arrivalTolerance) {
        ScriptValidation.finite(stepLength, "step length");
        ScriptValidation.finite(arrivalTolerance, "arrival tolerance");
        return EasyNPCMotionHandler.stepTowards(entity(), position(target), stepLength, arrivalTolerance);
    }
}
