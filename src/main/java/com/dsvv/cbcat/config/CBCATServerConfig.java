package com.dsvv.cbcat.config;


import net.createmod.catnip.config.ConfigBase;

public class CBCATServerConfig extends ConfigBase
{
    public final CBCATBigCannons bigCannons = nested(0, CBCATBigCannons::new, Comments.bigCannons);

    @Override
    public String getName() {
        return "server";
    }

    private static class Comments
    {
        static String bigCannons = "Here are the settings for big cannons.";
    }
}
