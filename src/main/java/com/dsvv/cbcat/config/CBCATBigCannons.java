package com.dsvv.cbcat.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CBCATBigCannons extends ConfigBase
{
    public final ConfigBool disablePhysicRework = b(false, "disablePhysicRework", Comments.disablePhysicReworkComment);

    @Override
    public String getName() {
        return "big_cannons";
    }

    private static class Comments
    {
        static String disablePhysicReworkComment = "If true the default Create Big Cannons calculation for the projectile velocity is used.";
    }
}
